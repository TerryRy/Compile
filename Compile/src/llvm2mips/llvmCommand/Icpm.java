package llvm2mips.llvmCommand;

import Type.Type;

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
    public void analyzeCom() {
        // TODO:
    }
}
