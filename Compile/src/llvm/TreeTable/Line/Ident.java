package llvm.TreeTable.Line;

import Type.Type;

// 普通量
public class Ident extends LineIdent {
    private Type identType;
    private String value; // 值，主要给const用的，存String是方便应对各种类型

    public Ident(String name, String tNum, boolean isConst, int bbNum, Type type, String value) {
        super(name, tNum, isConst, bbNum);
        this.identType = type;
        this.value = value;
    }

    public Type getLineIdentType() {
        return identType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return identType.toString() + " " + super.getName();
    }
}
