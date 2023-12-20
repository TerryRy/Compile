package llvm2mips.mipsLine.mipsTextLine;

public class LineJr extends MipsTextLine {
    public LineJr() {
    }

    @Override
    public String toMips() {
        return "jr $ra";
    }
}
