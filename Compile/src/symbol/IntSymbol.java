package symbol;

public class IntSymbol extends Symbol {
    private boolean constInt; // 是否是常量
    private int dimension; // 0表示普通量，1表示一维数组，2表示二维数组

    public IntSymbol(String name, boolean constInt, int dimension) {
        super(name);
        this.constInt = constInt;
        this.dimension = dimension;
    }

    public boolean isConstInt() {
        return constInt;
    }

    public void setConstInt(boolean constInt) {
        this.constInt = constInt;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
