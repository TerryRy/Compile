package llvm.TreeTable.Line;

public class LineFuncFParam {
    private LineIdent lineIdent; // 内容，形参只能是量、直接放量的父类

    public LineFuncFParam(LineIdent lineIdent) {
        this.lineIdent = lineIdent;
    }

    public LineIdent getLineIdent() {
        return lineIdent;
    }

    public void setLineIdent(LineIdent lineIdent) {
        this.lineIdent = lineIdent;
    }

    @Override
    public String toString() {
        return this.lineIdent.toString();
    }
}
