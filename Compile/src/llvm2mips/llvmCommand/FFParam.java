package llvm2mips.llvmCommand;

import Type.Type;

public class FFParam {
    Type type;
    String value;
    boolean typeHasP;

    public FFParam(Type type, String value, boolean typeHasP) {
        this.type = type;
        this.value = value;
        this.typeHasP = typeHasP;
    }

    public FFParam(Type type, String value) {
        this.type = type;
        this.value = value;
        this.typeHasP = false;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
