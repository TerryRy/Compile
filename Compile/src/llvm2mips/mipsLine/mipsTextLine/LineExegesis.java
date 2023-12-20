package llvm2mips.mipsLine.mipsTextLine;

// 注释
public class LineExegesis extends MipsTextLine {
    private String contains;

    public LineExegesis(String contents) {
        this.contains = contents;
    }

    @Override
    public String toMips() {
        if (contains == null || contains.equals("")) {
            return "";
        }
        else {
            return "# " + contains;
        }
    }
}
