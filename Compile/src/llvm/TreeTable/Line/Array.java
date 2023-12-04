package llvm.TreeTable.Line;

import Type.Type;

import java.util.List;

public class Array extends LineIdent {
    private Type arrayType; // 类型，最高维长度也在其中
    private String value; // 值，失误存成字符串了
//    private List<LineIdent> values; // 值，认为高维数组是低维数组的一维数组
    // values的类型还没想好，或许可以是List<Array>, 记得加入构造函数
    // 是否需要一个List<Integer>记录数组全部维度的长度，进type了

    public Array(String name, String tNum, boolean isConst, Type arrayType, int bbNum, String value) {
        super(name, tNum, isConst, bbNum);
        this.arrayType = arrayType;
        this.value = value;
    }

    public Type getLineIdentType() {
        return arrayType;
    }

    public void setArrayType(Type arrayType) {
        this.arrayType = arrayType;
    }

    public String getValue() {
        return value;
    }

}
