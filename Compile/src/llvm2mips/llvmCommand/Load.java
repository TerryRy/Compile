package llvm2mips.llvmCommand;

import Type.Type;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsDataLine.MacroLine;
import llvm2mips.mipsLine.mipsTextLine.LineExegesis;
import llvm2mips.mipsLine.mipsTextLine.LineLa;
import llvm2mips.mipsLine.mipsTextLine.LineLw;
import llvm2mips.mipsLine.mipsTextLine.LineSw;

public class Load extends LlvmCom {
    Type typeResult; // 寄存器中
    boolean t1HasStar;
    String result;
    Type typeValue; // 内存中
    boolean t2HasStar;
    String value;


    public Load(Type typeResult, String result, Type typeValue, String value) {
        this.typeResult = typeResult;
        this.t1HasStar = false;
        this.result = result;
        this.typeValue = typeValue;
        this.t2HasStar = false;
        this.value = value;
    }

    public Load(Type typeResult, boolean t1HasStar, String result, Type typeValue, boolean t2HasStar, String value) {
        this.typeResult = typeResult;
        this.t1HasStar = t1HasStar;
        this.result = result;
        this.typeValue = typeValue;
        this.t2HasStar = t2HasStar;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.result)
                .append(" = load ")
                .append(this.typeResult)
                .append(this.t1HasStar ? "*, " : ", ")
                .append(this.typeValue)
                .append(this.t2HasStar ? "* " : " ")
                .append(this.value);
        return sb.toString();
    }

    @Override
    public void analyzeCom() {
        // TODO:
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));

        String onType = super.getOnType(this.value);
        try {
            String rt = Mips.getMips().getRegister();
            if (onType.equals("register")) {
                // lw
                int ty1 = Mips.getMips().checkMipsTab(this.value).getIndex();
                Mips.getMips().addTextLines(new LineLw(rt, ty1));
            }
            else if (onType.equals("global")) {
                // la
                Mips.getMips().addTextLines(new LineLa(rt, this.value.substring(1)));
            }
            else {
                // 不可能
            }
            // sw
//            int ty2 = super.getTy(this.typeValue, t2HasStar);
            int len = Mips.getMips().allocLoc(1);
            Mips.getMips().addTextLines(new LineSw(rt, len));
            // 填表
            Mips.getMips().addMipsTab(this.result, this.typeResult, len);
            Mips.getMips().freeRegister(rt);
        } catch (Exception e) {
            System.err.println("寄存器申请、释放错误");
            e.printStackTrace();
        }
    }
}
