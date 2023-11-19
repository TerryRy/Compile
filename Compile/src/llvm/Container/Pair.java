package llvm.Container;

import Type.Type;

public class Pair {
    Type first;
    String second; // 寄存器（带%）或数字

    public Pair(Type first, String second) {
        this.first = first;
        this.second = second;
    }

    public Type getType() {
        return first;
    }

    public void setType(Type first) {
        this.first = first;
    }

    public String getResult() {
        return second;
    }

    public void setTNum(String second) {
        this.second = second;
    }
}
