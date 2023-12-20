package llvm2mips;

import Type.Type;
import Type.IntegerType;
import llvm2mips.MipsTable.MipsTabLin;
import llvm2mips.llvmCommand.LlvmCom;
import llvm2mips.mipsLine.mipsDataLine.MacroLine;
import llvm2mips.mipsLine.mipsDataLine.MipsDataLine;
import llvm2mips.mipsLine.mipsTextLine.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Mips {
    private static Mips mips;

    static {
        mips = new Mips();
        mips.initRegisters();
        mips.memory_flag = 0;
    }

    public static Mips getMips() {
        return mips;
    }

    // 控制变量

    List<MipsDataLine> dataLines = new ArrayList<>();

    List<MipsTextLine> textLines = new ArrayList<>();

    // 符号表，直接存到偏移量
    List<MipsTabLin> mipsTable = new ArrayList<>(); // 保存现场也在其中，意味着可能有多行ra,sp,v0等

    List<String> registers = new ArrayList<>();

    int memory_flag; // 记录内存占用到何处， 从0开始，其值为负，表示sp-memory_flag是当前最近可用的4字节开头，sp改变时同时改变，即加入现场维护中

    int llvmCodsReader = 0; // llvm的阅读位置

    // 控制方法

    public LlvmCom readLlvm(List<LlvmCom> llvmComs) {
        llvmCodsReader++;
        return llvmComs.get(llvmCodsReader - 1);
    }

    int stackTop = 0; // 符号表栈顶，非调用过程中时其值指代的栈位可直接使用

    private void initRegisters() {
        registers.add("$t0");
        registers.add("$t1");
        registers.add("$t2");
        registers.add("$t3");
        registers.add("$t4");
        registers.add("$t5");
        registers.add("$t6");
        registers.add("$t7");
        registers.add("$s0");
        registers.add("$s1");
        registers.add("$s2");
        registers.add("$s3");
        registers.add("$s4");
        registers.add("$s5");
        registers.add("$s6");
        registers.add("$s7");
    }

    public void addDataLines(MipsDataLine dataLine) {
        dataLines.add(dataLine);
    }

    public void addTextLines(MipsTextLine textLine) {
        textLines.add(textLine);
    }

    public void addMipsTab(String name, Type type, int len) {
        mipsTable.add(stackTop, new MipsTabLin(name, type, len));
        stackTop++;
    }

    public MipsTabLin checkMipsTab(String name) {
        for (int i = mipsTable.size() - 1; i >= 0; i--) {
            if (name.equals(mipsTable.get(i).getName())) {
                return mipsTable.get(i);
            }
        }
        return null; // 未查到
    }

    // 唯一用在恢复现场回退stackTop时查询刚刚被恢复的ra之前保存在符号表中的位置
    public int getMipsTabIndex(String name) {
        for (int i = mipsTable.size() - 1; i >= 0; i--) {
            if (name.equals(mipsTable.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    public String getRegister() throws Exception {
        if (registers.size() > 0) {
            String register = registers.get(0);
            registers.remove(0);
            return register;
        }
        else {
            throw new Exception("No Spared Register Left");
        }
    }

    public void freeRegister(String name) throws Exception {
        if (/*Pattern.compile("^t|s[0-7]").matcher(name).matches()*/!registers.contains(name)) {
//            System.out.println("回收寄存器成功！");
            registers.add(name);
        }
        else {
            throw new Exception("Invalid register");
        }
    }

    // 保存现场，在传入参数后保存，这样进入函数读参数时都是sp+
    public void saveSite(int spLen) {
        addTextLines(new LineSw("$ra", memory_flag));
        // 保存ra, v0, 存入的值是更新前sp下的偏移
        addMipsTab("$ra", IntegerType.i32, memory_flag);
        memory_flag -= 4;
//        addTextLines(new LineSw("$v0", memory_flag));
//        addMipsTab("$v0", IntegerType.i32, memory_flag);
//        memory_flag -= 4;
        // 保存sp， 这次存的是sp减去的值，sp使用加减法来更新的
        // 其中sp_Len视参数情况而定，其值为负，调用函数时使用加法，恢复现场时使用减法
        addTextLines(new LineCompute("addu", "$sp", "$sp", String.valueOf(spLen)));
        addMipsTab("$sp", IntegerType.i32, spLen);
        // 更新memory_lage,似的在sp更新后仍然能够正常使用
        memory_flag = memory_flag + spLen;
    }

    public void restoreSite() throws Exception {
        // 查符号表，取回最近的ra和v0
        MipsTabLin lineRa = checkMipsTab("$ra");
//        MipsTabLin lineV0 = checkMipsTab("$v0");
        // 更新ra和v0的值
        addTextLines(new LineLw("$ra", lineRa.getIndex()));
//        addTextLines(new LineLw("$v0", lineV0.getIndex()));

        // 查表读出最近的sp的spLen
        MipsTabLin LineSp = checkMipsTab("$sp");
        // 更新sp的值
        addTextLines(new LineCompute("subu", "$sp", "$sp", String.valueOf(LineSp.getIndex())));
        // 根据spLen恢复memory_flag，额外＋两个4，相当于清掉ra和v0，避免后续回退时查错
        memory_flag = memory_flag - LineSp.getIndex() + 4 + 4;

        // 压栈顶至ra处，即清空函数临时栈
        stackTop = getMipsTabIndex("$ra");
        if (stackTop == -1) {
            throw new Exception("Restore Site ERROR!");
        }
    }

    // 申请指定字节的空地址, 返回不用*4，直接使用
    public int allocLoc(int bytes) {
        memory_flag -= bytes * 4;
        return  memory_flag + bytes * 4; // 真正分配的
    }

    // 根据偏移字节数计算地址,当然是存在寄存器里，需要调用者用完释放
    public String getLoc(int bytes) throws Exception {
        // addu rt, $sp, -bytes*4
        int ty = bytes * -4;
        String rt = getRegister();
        Mips.getMips().addTextLines(new LineCompute("addu", rt, "$sp", "" + ty));
        return rt;
    }

    public void analyze(List<LlvmCom> llvmComs) {
        addTextLines(new LineJal("main"));
        addTextLines(new LineJ("return"));
        LlvmCom curLlvm;
        while (llvmCodsReader < llvmComs.size()) {
            curLlvm = readLlvm(llvmComs);
            curLlvm.analyzeCom();
        }
        addTextLines(new LineNewBlock("return"));
        /*
        .macro GETINT()
        li $v0, 5
        syscall
        .end_macro
         */
        List<MipsTextLine> mTLsGETINT = new ArrayList<>();
        mTLsGETINT.add(new LIneLi("$v0", "5"));
        mTLsGETINT.add(new LineSyscall());
        addDataLines(new MacroLine("GETINT", mTLsGETINT));
        /*
        .macro PUTINT()
        li $v0, 1
        syscall
        .end_macro
         */
        List<MipsTextLine> mTLsPUTINT = new ArrayList<>();
        mTLsPUTINT.add(new LIneLi("$v0", "1"));
        mTLsPUTINT.add(new LineSyscall());
        addDataLines(new MacroLine("PUTINT", mTLsPUTINT));
        /*
        .macro PUTCH()
        li $v0, 11
        syscall
        .end_macro
         */
        List<MipsTextLine> mTLsPUTCH = new ArrayList<>();
        mTLsPUTCH.add(new LIneLi("$v0", "11"));
        mTLsPUTCH.add(new LineSyscall());
        addDataLines(new MacroLine("PUTCH", mTLsPUTCH));
        /*
        .macro PUTSTR()
        li $v0, 4
        syscall
        .end_macro
         */
        List<MipsTextLine> mTLsPUTSTR = new ArrayList<>();
        mTLsPUTSTR.add(new LIneLi("$v0", "4"));
        mTLsPUTSTR.add(new LineSyscall());
        addDataLines(new MacroLine("PUTSTR", mTLsPUTSTR));
    }

    public void mipsPrinter(BufferedWriter writer) throws IOException {
        writer.write(".data\n\n");
        for (MipsDataLine dataLine : mips.dataLines) {
            if (dataLine.toMips() != "") {
                writer.write(dataLine.toMips() + "\n\n");
            }
        }
        writer.write(".text\n\n");
        for (MipsTextLine textLine : mips.textLines) {
            if (textLine.toMips() != "") {
                writer.write(textLine.toMips() + "\n\n");
            }
        }
    }
}
