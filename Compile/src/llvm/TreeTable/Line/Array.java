package llvm.TreeTable.Line;

import Type.Type;

import java.util.List;

public class Array extends LineIdent {
    private Type arrayType; // 类型，最高维长度也在其中
    private List<Array> values; // 值，认为高维数组是低维数组的一维数组
    // values的类型还没想好，或许可以是List<Array>, 记得加入构造函数
    // 是否需要一个List<Integer>记录数组全部维度的长度，进type了

    public Array(String name, String tNum, boolean isConst, Type arrayType, int bbNum, List<Array> values) {
        super(name, tNum, isConst, bbNum);
        this.arrayType = arrayType;
        this.values = values;

    }

    public Type getLineIdentType() {
        return arrayType;
    }

    public void setArrayType(Type arrayType) {
        this.arrayType = arrayType;
    }
}
