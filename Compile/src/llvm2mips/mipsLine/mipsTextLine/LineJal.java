package llvm2mips.mipsLine.mipsTextLine;

public class LineJal extends MipsTextLine {
    private String label;

    public LineJal(String label) {
        this.label = label;
    }

    @Override
    public String toMips() {
        return "jal " + this.label;
    }
}
