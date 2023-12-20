package llvm2mips.mipsLine.mipsTextLine;

public class LineLa extends MipsTextLine {
    private String result;
    private String value;

    public LineLa(String result, String value) {
        this.result = result;
        this.value = value;
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        sb.append("la ")
                .append(result)
                .append(", ")
                .append(value);
        return sb.toString();
    }
}
