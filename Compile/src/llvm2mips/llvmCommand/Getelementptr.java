package llvm2mips.llvmCommand;

import Type.Type;
import Type.LineArrayType;
import Type.IntegerType;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsTextLine.*;

import java.util.List;

public class Getelementptr extends LlvmCom {
    String result;
    Type firstType; // 第一个偏移的类型
    boolean t1HasStar;
    Type valueType; // 数组的类型
    boolean t2HasStar;
    String value; // 数组寄存器
    List<String> tys; // 偏移量

    public Getelementptr(String result, Type firstType, boolean t1HasStar, Type valueType, boolean t2HasStar, String value, List<String> tys) {
        this.result = result;
        this.firstType = firstType;
        this.t1HasStar = t1HasStar;
        this.valueType = valueType;
        this.t2HasStar = t2HasStar;
        this.value = value;
        this.tys = tys;
    }

    public Getelementptr(String result, Type firstType, Type valueType, String value, List<String> tys) {
        this.result = result;
        this.firstType = firstType;
        this.t1HasStar = false;
        this.valueType = valueType;
        this.t2HasStar = false;
        this.value = value;
        this.tys = tys;
    }

    @Override
    public String toString() {
        // %a11 = getelementptr [2 x i32], [2 x i32]* %a6, i32 0, i32 0
        StringBuilder sb = new StringBuilder();
        sb.append(this.result)
                .append(" = getelementptr ")
                .append(this.firstType.toString())
                .append(this.t1HasStar ? "* , " : " , ")
                .append(this.valueType.toString())
                .append(this.t2HasStar ? "* " : " ")
                .append(this.value);
        for (String s : this.tys) {
            sb.append(", i32 ")
                    .append(s);
        }
        return sb.toString();
    }

    @Override
    public void analyzeCom() {
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        try {
            // 寻址方式：当前位的值*当前子层的rc
            String rt = Mips.getMips().getRegister();
            Mips.getMips().addTextLines(new LIneLi(rt, "0"));
            Type curType = this.valueType;
            int curRC = 0;
            for (int i = 0; i < this.tys.size(); i++) {
                curRC = -4;
                if (curType instanceof LineArrayType) {
                    for (Integer integer : ((LineArrayType) curType).getLenList()) {
                        curRC *= integer;
                    }
                }
                String onType = super.getOnType(this.tys.get(i));
                if (onType.equals("immediate")) {
                    // 立即数
                    int len = Integer.parseInt(this.tys.get(i)) * curRC;
                    Mips.getMips().addTextLines(new LineCompute("addu", rt, rt, String.valueOf(len)));
                }
                else if (onType.equals("global")) {
                    // 全局变量
                    int len = Integer.parseInt(this.tys.get(i).substring(1)) * curRC;
                    Mips.getMips().addTextLines(new LineCompute("addu", rt, rt, String.valueOf(len)));
                }
                else {
                    // 寄存器
                    // lw rt1, table
                    // mul rt1, rt1, RC
                    // addu rt, rt, rt1
                    int ty = Mips.getMips().checkMipsTab(this.tys.get(i)).getIndex();
                    String rt1 = Mips.getMips().getRegister();
                    Mips.getMips().addTextLines(new LineLw(rt1, ty));
                    Mips.getMips().addTextLines(new LineCompute("mul", rt1, rt1, String.valueOf(curRC)));
                    Mips.getMips().addTextLines(new LineCompute("addu", rt, rt, rt1));
                    Mips.getMips().freeRegister(rt1);
                }
                if (curType instanceof IntegerType) {
                    break;
                }
                else {
                    curType = ((LineArrayType) curType).getElementType();
                }
            }
            String rtBase = Mips.getMips().getRegister();
            int ty = Mips.getMips().checkMipsTab(this.value).getIndex();
            Mips.getMips().addTextLines(new LineLw(rtBase, ty));
            int ty1 = Mips.getMips().allocLoc(1);
            Mips.getMips().addTextLines(new LineCompute("addu", rtBase, rtBase, rt));
            Mips.getMips().addTextLines(new LineSw(rtBase, ty1));
            // 填符号表
            Mips.getMips().addMipsTab(this.result, curType, ty1);
            Mips.getMips().freeRegister(rt);
            Mips.getMips().freeRegister(rtBase);
        } catch (Exception e) {
            System.err.println("寄存器申请、释放错误");
            e.printStackTrace();
        }
    }
}
