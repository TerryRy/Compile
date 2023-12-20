package llvm2mips.llvmCommand;

import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsTextLine.LineBeqz;
import llvm2mips.mipsLine.mipsTextLine.LineExegesis;
import llvm2mips.mipsLine.mipsTextLine.LineJ;
import llvm2mips.mipsLine.mipsTextLine.LineLw;

public class Br extends LlvmCom {
    String checker;
    String labelT;
    String labelF;

    public Br(String checker, String labelT, String labelF) {
        this.checker = checker;
        this.labelT = labelT;
        this.labelF = labelF;
    }

    public Br(String label) {
        this.checker = null;
        this.labelT = label;
        this.labelF = null;
    }

    private int typeCheck() {
        if (this.checker == null) {
            // 无条件跳转
            return 1; // 只有一个label
        }
        else {
            return 2; // 有2个label
        }
    }

    @Override
    public String toString() {
        if (typeCheck() == 1) {
            return "br %" + labelT;
        }
        else {
            return "br " + checker + " , %" + labelT + ", %" + labelF;
        }
    }

    @Override
    public void analyzeCom() {
        // br label %a22
        // br i1 %a30, label %a23, label %a24
        if (this.typeCheck() == 1) {
            Mips.getMips().addTextLines(new LineExegesis(this.toString()));
            Mips.getMips().addTextLines(new LineJ(this.labelT));
        }
        else {
            // # br i1 %23, label %24, label %25
            Mips.getMips().addTextLines(new LineExegesis(this.toString()));
            /*
            lw $t0, -120($sp)
            beqz $t0, label_unique51
            j label_unique48
             */
            try {
                int ty = Mips.getMips().checkMipsTab(this.checker).getIndex();
                String rLw = null;
                rLw = Mips.getMips().getRegister();
                Mips.getMips().addTextLines(new LineLw(rLw, ty));
                Mips.getMips().addTextLines(new LineBeqz(rLw, this.labelF));
                Mips.getMips().addTextLines(new LineJ(this.labelT));
                Mips.getMips().freeRegister(rLw);
            } catch (Exception e) {
                System.err.println("寄存器申请、释放错误");
                e.printStackTrace();
            }
        }
    }
}
