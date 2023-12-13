package llvm2mips.llvmCommand;

import Type.Type;

import java.util.List;

public class DefineIdent extends LlvmCom {
    boolean isConst;
    Type type;
    String name;
    String values;

    public DefineIdent(boolean isConst, Type type, String name, String values) {
        this.isConst = isConst;
        this.type = type;
        this.name = name;
        this.values = values;
    }

    @Override
    public void analyzeCom() {
        // TODO:
    }
}
