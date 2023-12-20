package llvm2mips.llvmCommand;

import Type.Type;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsDataLine.MacroLine;
import llvm2mips.mipsLine.mipsTextLine.*;

public class Store extends LlvmCom {
    Type typeResult; // 内存中
    boolean t1HasStar;
    String result;
    Type typeValue; // 寄存器中
    boolean t2HasStar;
    String value;

    public Store(Type typeResult, String result, Type typeValue, String value) {
        this.typeValue = typeValue;
        this.t1HasStar = false;
        this.value = value;
        this.typeResult = typeResult;
        this.t2HasStar = false;
        this.result = result;
    }

    public Store(Type typeValue, boolean t2HasStar, String value, Type typeResult, boolean t1HasStar, String result) {
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
        sb.append("store ")
                .append(this.typeValue)
                .append(this.t2HasStar ? "* " : " ")
                .append(this.value)
                .append(", ")
                .append(this.typeValue)
                .append(this.t1HasStar ? "* " : " ")
                .append(this.result);
        return sb.toString();
    }

    @Override
    public void analyzeCom() {
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        try {
            String rt = Mips.getMips().getRegister();
            String onType = super.getOnType(this.value);
            if (onType.equals("register")) {
                // 寄存器
                int ty = Mips.getMips().checkMipsTab(this.value).getIndex();
                Mips.getMips().addTextLines(new LineLw(rt, ty));
            }
            else if (onType.equals("global")) {
                // 全局变量
                Mips.getMips().addTextLines(new LineLa(rt, this.value.substring(1)));
            }
            else {
                // 立即数
                Mips.getMips().addTextLines(new LIneLi(rt, this.value));
            }
            // sw
            int ty = Mips.getMips().checkMipsTab(this.result).getIndex();
            Mips.getMips().addTextLines(new LineSw(rt, ty));
        } catch (Exception e) {
            System.err.println("寄存器申请、释放错误");
            e.printStackTrace();
        }
    }
}
