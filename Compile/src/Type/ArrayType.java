package Type;

public class ArrayType implements Type {
    private Type elementType; // 元素类型，多维数组元素类型为低维数组，即ArrayType
    private int len; // 当前维度长度

    // 可以通过判断elementType是不是IntegerType来判断有没有到底层维度
    // 也可以递归计数来得到当前维度层级
}
