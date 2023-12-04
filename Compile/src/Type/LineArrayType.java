package Type;

import java.util.List;

public class LineArrayType extends LineFuncType implements Type {
    private Type elementType; // 元素类型，多维数组元素类型为低维数组，即ArrayType
    private IntegerType baseType; // 最低维普通变量类型，暂时仅有IntegerType
    private int len; // 当前维度长度
    private int dimension; // 每个元素维度数，+1就是当前层级
    private List<Integer> lenList; // 所有维度长度,零维认为长度是1

    public LineArrayType(Type elementType, IntegerType baseType, int len, int dimension, List<Integer> lenList) {
        this.elementType = elementType;
        this.baseType = baseType;
        this.len = len;
        this.dimension = dimension;
        this.lenList = lenList;
    }

    // 可以通过判断elementType是不是IntegerType来判断有没有到底层维度
    // 也可以递归计数来得到当前维度层级


    public Type getElementType() {
        return elementType;
    }

    public IntegerType getBaseType() {
        return baseType;
    }

    public int getLen() {
        return len;
    }

    public int getDimension() {
        return dimension;
    }

    public List<Integer> getLenList() {
        return lenList;
    }

    @Override
    public String toString() {
        // [length x [length x IntegerType]]
        if (this.len == -1) {
            // 函数形参中的变长
            return this.elementType.toString() + "*";
        }
        else {
            return "[" + this.len + " x " + this.elementType.toString() + "]";
        }
    }

    @Override
    public String toStringForCall() {
        return this.elementType.toString() + "*";
    }

}
