package llvm2mips.mipsLine.mipsTextLine;

public class LineCompair extends MipsTextLine{
    private String op;
    private String result;
    private String on1;
    private String on2;

    public LineCompair(String op, String result, String on1, String on2) {
        this.op = op;
        this.result = result;
        this.on1 = on1;
        this.on2 = on2;
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.op)
                .append(" ")
                .append(this.result)
                .append(", ")
                .append(this.on1)
                .append(", ")
                .append(this.on2);
        return sb.toString();
    }
}
