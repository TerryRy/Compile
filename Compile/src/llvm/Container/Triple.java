package llvm.Container;

public class Triple {
    String name; // 名字
    Pair pair; // 类型和寄存器编号/值

    public Triple(String name, Pair pair) {
        this.name = name;
        this.pair = pair;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }
}
