package llvm2mips.llvmCommand;

import Type.Type;
import Type.IntegerType;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsDataLine.MacroLine;
import llvm2mips.mipsLine.mipsTextLine.*;

public class Select extends LlvmCom {
    String result;
    Type typeSelect; // 待筛选
    String valueSelect;
    Type typeTrue;
    String valueTrue;
    Type typeFalse;
    String valueFalse;

    public Select(String result, Type typeSelect, String valueSelect, Type typeTrue, String valueTrue, Type typeFalse, String valueFalse) {
        this.result = result;
        this.typeSelect = typeSelect;
        this.valueSelect = valueSelect;
        this.typeTrue = typeTrue;
        this.valueTrue = valueTrue;
        this.typeFalse = typeFalse;
        this.valueFalse = valueFalse;
    }

    @Override
    public String toString() {
        // %a7 = select i1 %a8, i32 1, i32 0
        StringBuilder sb = new StringBuilder();
        sb.append(result)
                .append(" = select i1 ")
                .append(valueSelect)
                .append(", ")
                .append(typeTrue.toString())
                .append(" 1")
                .append(valueFalse.toString())
                .append(" 0");
        return sb.toString();
    }

    @Override
    public void analyzeCom() {
        // select 的特点是on1=1 on2=0是固定的
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        try {
            String rt = Mips.getMips().getRegister();
            String onType = super.getOnType(this.valueSelect);
            if (onType.equals("register")) {
                // 寄存器
                int ty = Mips.getMips().checkMipsTab(this.valueSelect).getIndex();
                Mips.getMips().addTextLines(new LineLw(rt, ty));
            }
            else if (onType.equals("global")) {
                // 全局变量
                Mips.getMips().addTextLines(new LineLa(rt, this.valueSelect.substring(1)));
            }
            else {
                // 立即数
                Mips.getMips().addTextLines(new LIneLi(rt, this.valueSelect));
            }
            String rt2 = Mips.getMips().getRegister();
            // seq rt2, rt, 0
            Mips.getMips().addTextLines(new LineCompair("seq", rt2, rt, "0"));
            // sw rt2
            int ty = Mips.getMips().allocLoc(1);
            Mips.getMips().addTextLines(new LineSw(rt2, ty));
            Mips.getMips().addMipsTab(this.result, IntegerType.i1, ty);
            Mips.getMips().freeRegister(rt);
            Mips.getMips().freeRegister(rt2);
        } catch (Exception e) {
            System.err.println("寄存器申请、释放错误");
            e.printStackTrace();
        }
    }
}
