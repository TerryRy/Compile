package llvm.Container;

import Type.Type;

import java.util.List;

public class Pair {
    Type first;
    String second; // 寄存器（带%）或数字
    List<Pair> pairs; // 针对数组，以二维数组为例，一个二维数组量是一个pair，里面包含的pairs是一列一维数组量，再里面才是一列常量
    boolean isZero; // 针对数组，记录当前维度元素是否全0
    // 数组维度，普通变量为0 可由pairs.size得到，或判断为null

    public Pair(Type first, String second, List<Pair> pairs, boolean isZero) {
        this.first = first;
        this.second = second;
        this.pairs = pairs;
        this.isZero = isZero;
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

    public List<Pair> getPairs() {
        return pairs;
    }

    public int getDimension() {
        if (this.pairs == null) {
            return 0;
        }
        else {
            return this.pairs.size();
        }
    }

    public boolean isArray() {
        return getDimension() != 0;
    }

    public boolean getIsZero() {
        return this.isZero;
    }

    @Override
    public String toString() {
        // [[3 x i32] [i32 1, i32 2, i32 0], [3 x i32] zeroinitializer]
        if (this.pairs == null) {
            // 普通变量
            return first + " " + second;
        }
        else {
            // 数组
            StringBuilder str = new StringBuilder();
            str.append("[");
            if (pairs.size() > 0) {
                str.append(pairs.get(0).toString());
                for (int i = 1; i < pairs.size(); i++) {
                    str.append(pairs.get(i).toString());
                }

            }
            str.append("]");
            return str.toString();
        }
    }
}
