package llvm2mips.llvmCommand;

import Type.Type;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsTextLine.*;

public class Compute extends LlvmCom {
    String result;
    String op;
    Type type;
    String on1;
    String on2;

    public Compute(String result, String op, Type type, String on1, String on2) {
        this.result = result;
        this.op = op;
        this.type = type;
        this.on1 = on1;
        this.on2 = on2;
    }

    @Override
    public String toString() {
        return result + " = " + op + " " + type + " " + on1 + ", " + on2;
    }

    @Override
    public void analyzeCom() {
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        // add: addu $t0, $t0, $t1
        // sub: subu $t0, $t0, 1
        // mul: mul $t0, $t0, $t1
        // sdiv: div $t0, $t0, $t1
        // srem: rem $t0, $t0, $t1
        String op = "";
        String rt = "";
        String on1 = this.on1;
        String on2 = this.on2;

        switch (this.op) {
            case "add":
                op = "addu";
                break;
            case "sub":
                op = "subu";
                break;
            case "mul":
                op = "mul";
                break;
            case "sdiv":
                op = "div";
                break;
            case "srem":
                op = "rem";
                break;
            default:
                System.err.println("Invalid operation");
        }
        try {
            rt = Mips.getMips().getRegister();
            // on1
            String rt1 = Mips.getMips().getRegister();
            String onType = super.getOnType(on1);
            if (onType.equals("register")) {
                // 是寄存器
                // lw $t0, -176($sp)
                int ty = Mips.getMips().checkMipsTab(on1).getIndex();
                Mips.getMips().addTextLines(new LineLw(rt1, ty));
            }
            else if (onType.equals("global")) {
                // 全局变量
                // la $t0, a
                Mips.getMips().addTextLines(new LineLa(rt1, on1.substring(1)));
            }
            else {
                // 是立即数
                // li $t0, 1
                Mips.getMips().addTextLines(new LIneLi(rt1, on1));
            }
            // on2
            String rt2 = Mips.getMips().getRegister();
            onType = super.getOnType(on2);
            if (onType.equals("register")) {
                // 是寄存器
                // lw $t0, -176($sp)
                int ty = Mips.getMips().checkMipsTab(on2).getIndex();
                Mips.getMips().addTextLines(new LineLw(rt2, ty));
            }
            else if (onType.equals("global")) {
                // 全局变量
                // la $t0, a
                Mips.getMips().addTextLines(new LineLa(rt2, on2.substring(1)));
            }
            else {
                // 是立即数
                // li $t0, 1
                Mips.getMips().addTextLines(new LIneLi(rt2, on2));
            }
            // 计算语句
            Mips.getMips().addTextLines(new LineCompute(op, rt, rt1, rt2));
            // sw $t0, -232($sp)
            int ty = Mips.getMips().allocLoc(1);
            Mips.getMips().addTextLines(new LineSw(rt, ty));
            // 填符号表
            Mips.getMips().addMipsTab(this.result, this.type, ty);
            Mips.getMips().freeRegister(rt1);
            Mips.getMips().freeRegister(rt2);
            Mips.getMips().freeRegister(rt);
        } catch (Exception e) {
            System.err.println("寄存器申请、释放错误");
            e.printStackTrace();
        }

    }
}
