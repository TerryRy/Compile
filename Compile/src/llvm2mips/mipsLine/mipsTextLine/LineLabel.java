package llvm2mips.mipsLine.mipsTextLine;

public class LineLabel extends MipsTextLine {
    private String name;

    public LineLabel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toMips() {
        return this.name + ":";
    }
}
