package symbol;

public class FuncParam {
    private String name;
    private int dimension; // 0表示普通量，1表示一维数组，2表示二维数组

    public FuncParam(String name, int dimension) {
        this.name = name;
        this.dimension = dimension;
    }

    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    @Override
    public String toString() {
        return "FuncParam{" +
                "name='" + name + '\'' +
                ", dimension=" + dimension +
                '}';
    }
}
