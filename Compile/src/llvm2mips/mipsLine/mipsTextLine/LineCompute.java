package llvm2mips.mipsLine.mipsTextLine;

public class LineCompute extends MipsTextLine {
    private String op;
    private String result;
    private String on1;
    private String on2;

    public LineCompute(String op, String result, String on1, String on2) {
        this.op = op;
        this.result = result;
        this.on1 = on1;
        this.on2 = on2;
    }

    @Override
    public String toMips() {
        return op + " " + result + ", " + on1 + ", " + on2;
    }
}
