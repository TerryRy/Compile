package llvm2mips.mipsLine.mipsTextLine;

public class LineSw extends MipsTextLine {
    private String value;
    private int ty;

    public LineSw(String value, int ty) {
        this.value = value;
        this.ty = ty;
    }

    @Override
    public String toMips() {
        return "sw " + value + ", " + ty + "($sp)";
    }
}
