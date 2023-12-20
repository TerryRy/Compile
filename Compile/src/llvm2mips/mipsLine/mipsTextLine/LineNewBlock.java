package llvm2mips.mipsLine.mipsTextLine;

public class LineNewBlock extends MipsTextLine {
    private String name;

    public LineNewBlock(String name) {
        this.name = name;
    }

    @Override
    public String toMips() {
        return name + ":";
    }
}
