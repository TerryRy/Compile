package llvm2mips.mipsLine.mipsDataLine;

// 注释
public class LineDataExegesis extends MipsDataLine {
    private String contains;

    public LineDataExegesis(String contents) {
        this.contains = contents;
    }

    @Override
    public String toMips() {
        return "# " + contains;
    }
}
