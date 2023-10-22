package error;

// 错误信息类
public class Error implements Comparable<Error> {
    private int lineNum;
    private ErrorType type;

    public Error(int lineNum, ErrorType type) {
        this.lineNum = lineNum;
        this.type = type;
    }

    @Override
    public int compareTo(Error o) {
        if (lineNum == o.lineNum) {
            return 0;
        } else if (lineNum < o.lineNum) {
            return -1;
        }
        return 1;
    }

    @Override
    public String toString() {
        return lineNum + " " + type.toString() + "\n";
    }

}
