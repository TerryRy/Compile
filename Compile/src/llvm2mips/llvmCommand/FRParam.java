package llvm2mips.llvmCommand;

import Type.Type;

public class FRParam {
    Type type;
    boolean tHasP;
    String value;

    public FRParam(Type type, String value) {
        this.type = type;
        this.tHasP = false;
        this.value = value;
    }

    public FRParam(Type type, boolean tHasP, String value) {
        this.type = type;
        this.tHasP = tHasP;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean getHasP() {
        return tHasP;
    }

    @Override
    public String toString() {
        return this.type + (this.tHasP ? "*" : "") + " " + this.value;
    }
}
