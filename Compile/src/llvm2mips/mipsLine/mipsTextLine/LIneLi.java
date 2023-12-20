package llvm2mips.mipsLine.mipsTextLine;

public class LIneLi extends MipsTextLine {
    private String result;
    private String value;

    public LIneLi(String result, String value) {
        this.result = result;
        this.value = value;
    }

    public String toMips() {
        return "li " + result + ", " + value;
    }
}
