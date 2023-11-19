package llvm.TreeTable.Line;

import Type.Type;

public class Array extends LineIdent {
    private Type arrayType; // 类型，最高维长度也在其中
    // private List<Array> values; // 值，给const用的，存String是方便应对各种类型
    // values的类型还没想好，或许可以是List<Array>, 记得加入构造函数
    // 是否需要一个List<Integer>记录数组全部维度的长度

    public Array(String name, String tNum, boolean isConst, Type arrayType, int bbNum) {
        super(name, tNum, isConst, bbNum);
        this.arrayType = arrayType;
    }

    public Type getLineIdentType() {
        return arrayType;
    }

    public void setArrayType(Type arrayType) {
        this.arrayType = arrayType;
    }

    // TODO: toString
}
