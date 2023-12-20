package llvm2mips.mipsLine.mipsTextLine;

public class LineLw extends MipsTextLine {
    private String result;
    private int ty;

    public LineLw(String result, int ty) {
        this.result = result;
        this.ty = ty;
    }

    @Override
    public String toMips() {
        // lw $t0, -8($sp)
        return "lw " + result + ", " + ty + "($sp)";
    }
}
