package llvm2mips.llvmCommand;

import Type.Type;

public class Ret extends LlvmCom {
    Type type;
    String value;

    public Ret(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Ret(Type type) {
        this.type = type;
    }

    @Override
    public void analyzeCom() {
        // TODO:
    }
}
