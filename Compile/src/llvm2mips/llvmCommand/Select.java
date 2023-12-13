package llvm2mips.llvmCommand;

import Type.Type;

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
    public void analyzeCom() {
        // TODO:
    }
}
