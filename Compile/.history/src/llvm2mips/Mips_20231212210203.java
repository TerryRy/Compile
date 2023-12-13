package llvm2mips;

import Type.Type;
import llvm.Llvm;
import llvm2mips.MipsTable.MipsTabLin;
import llvm2mips.llvmCommand.LlvmCom;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class Mips {
    private static Mips mips;

    static {
        mips = new Mips();
    }

    public static Mips getMips() {
        return mips;
    }

    // 控制变量

    List<MipsTabLin> mipsTable = new ArrayList<>();

    List<String> registers = new ArrayList<>();

    // 控制方法

    private void initRegisters() {
        registers.add("t0");
        registers.add("t1");
        registers.add("t2");
        registers.add("t3");
        registers.add("t4");
        registers.add("t5");
        registers.add("t6");
        registers.add("t7");
        registers.add("s0");
        registers.add("s1");
        registers.add("s2");
        registers.add("s3");
        registers.add("s4");
        registers.add("s5");
        registers.add("s6");
        registers.add("s7");
    }

    public void addMipsTab(String name, Type type, int len) {
        mipsTable.add(new MipsTabLin(name, type, len));
    }

    public MipsTabLin checkMipsTab(String name) {
        for (int i = mipsTable.size() - 1; i >= 0; i--) {
            if (name.equals(mipsTable.get(i).getName())) {
                return mipsTable.get(i);
            }
        }
        return null; // 未查到
    }

    public String getRegister() {
        if ()
    }

    public void analyze(List<LlvmCom> llvmComs) {
        // TODO:
    }

    public void mipsPrinter(BufferedWriter writer) {
        // TODO:
    }
}
