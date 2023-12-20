package llvm2mips.llvmCommand;

import Type.Type;
import Type.IntegerType;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsTextLine.*;

public class Icpm extends LlvmCom {
    String result;
    String op;
    Type type;
    String value1;
    String value2;

    public Icpm(String result, String op, Type type, String value1, String value2) {
        this.result = result;
        this.op = op;
        this.type = type;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public String toString() {
        // %a59 = icmp slt i32 %a57, %a58
        StringBuilder sb = new StringBuilder();
        sb.append(result)
                .append(" = icmp ")
                .append(this.op)
                .append(" ")
                .append(this.type.toString())
                .append(" ")
                .append(this.value1)
                .append(", ")
                .append(this.value2);
        return sb.toString();
    }

    @Override
    public void analyzeCom() {
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        String op;
        switch (this.op) {
            case "eq":
                op = "seq";
                break;
            case "ne":
                op = "sne";
                break;
            case "sle":
                op = "sle";
                break;
            case "sge":
                op = "sge";
                break;
            case "slt":
                op = "slt";
                break;
            case "sgt":
                op = "sgt";
                break;
            default:
                System.err.println("Unknown icmp op");
                return;
        }
        /*
        # %12 = icmp ne i32 %10, %11

        lw $t0, -120($sp)
        lw $t1, -124($sp)
        sne $t0, $t0, $t1
        sw $t0, -128($sp)
         */
        String rt1;
        String rt2;
        String rt;
        int ty;
        try {
            rt1 = Mips.getMips().getRegister();
            String on1Type = super.getOnType(this.value1);
            if (on1Type.equals("register")) {
                // 寄存器
                // 查表
                int ty1 = Mips.getMips().checkMipsTab(this.value1).getIndex();
                // 取数
                Mips.getMips().addTextLines(new LineLw(rt1, ty1));
            }
            else if (on1Type.equals("global")) {
                // 全局变量
                // 赋值
                Mips.getMips().addTextLines(new LineLa(rt1, this.value1.substring(1)));
            }
            else {
                // 立即数
                // 赋值
                Mips.getMips().addTextLines(new LIneLi(rt1, this.value1));
            }
            rt2 = Mips.getMips().getRegister();
            String on2Type = super.getOnType(this.value2);
            if (on2Type.equals("register")) {
                // 寄存器
                // 查表
                int ty2 = Mips.getMips().checkMipsTab(this.value2).getIndex();
                // 取数
                Mips.getMips().addTextLines(new LineLw(rt2, ty2));
            }
            else if (on2Type.equals("global")) {
                // 全局变量
                Mips.getMips().addTextLines(new LineLa(rt2, this.value2.substring(1)));
            }
            else {
                // 立即数
                // 赋值
                Mips.getMips().addTextLines(new LIneLi(rt2, this.value2));
            }
            rt = Mips.getMips().getRegister();
            Mips.getMips().addTextLines(new LineCompair(op, rt, rt1, rt2));
            Mips.getMips().freeRegister(rt1);
            Mips.getMips().freeRegister(rt2);
            ty = Mips.getMips().allocLoc(1);
            Mips.getMips().addTextLines(new LineSw(rt, ty));
            // 填符号表
            Mips.getMips().addMipsTab(this.result, IntegerType.i1, ty);
            Mips.getMips().freeRegister(rt);
        } catch (Exception e) {
            System.err.println("寄存器申请、释放错误");
            e.printStackTrace();
        }
    }
}
