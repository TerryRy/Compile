package llvm2mips.mipsLine.mipsTextLine;

public class LineAdd extends MipsTextLine {
    private String result;
    private String on1;
    private String on2;

    public LineAdd(String result, String on1, String on2) {
        this.result = result;
        this.on1 = on1;
        this.on2 = on2;
    }

    @Override
    public String toMips() {
        return "add " + result + ", " + on1 + ", " + on2;
    }
}
