package llvm2mips.mipsLine.mipsTextLine;

public class LineMacro extends MipsTextLine{
    private String name;

    public LineMacro(String name) {
        this.name = name;
    }

    @Override
    public String toMips() {
        return this.name;
    }
}
