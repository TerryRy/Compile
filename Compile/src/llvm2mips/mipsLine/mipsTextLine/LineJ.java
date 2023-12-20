package llvm2mips.mipsLine.mipsTextLine;

public class LineJ extends MipsTextLine {
    private String label;

    public LineJ(String label) {
        this.label = label;
    }

    @Override
    public String toMips() {
        return "j " + label;
    }
}
