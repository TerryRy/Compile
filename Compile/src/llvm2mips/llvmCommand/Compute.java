package llvm2mips.llvmCommand;

import Type.Type;

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
    public void analyzeCom() {
        // TODO:
    }
}
