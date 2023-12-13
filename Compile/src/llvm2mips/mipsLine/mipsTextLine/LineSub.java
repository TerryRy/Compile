package llvm2mips.mipsLine.mipsTextLine;

public class LineSub extends MipsTextLine {
    private String result;
    private String on1;
    private String on2;

    public LineSub(String result, String on1, String on2) {
        this.result = result;
        this.on1 = on1;
        this.on2 = on2;
    }

    @Override
    String toMips() {
        return "sub " + result + ", " + on1 + ", " + on2;
    }
}
