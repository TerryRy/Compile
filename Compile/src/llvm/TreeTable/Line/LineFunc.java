package llvm.TreeTable.Line;

import Type.Type;

import java.util.List;

public class LineFunc extends TreeTableLine {
    private Type funcType; // 返回值类型
    private List<LineFuncFParam> funcFParamList; // 形参表

    public LineFunc(String name, String tNum, Type type, List<LineFuncFParam> funcFParamList, int bbNum) {
        super(name, tNum, bbNum);
        this.funcType = type;
        this.funcFParamList = funcFParamList;
    }

    public Type getType() {
        return funcType;
    }

    public void setType(Type type) {
        this.funcType = type;
    }

    public List<LineFuncFParam> getFuncFParamList() {
        return funcFParamList;
    }

    public void setFuncFParamList(List<LineFuncFParam> funcFParamList) {
        this.funcFParamList = funcFParamList;
    }
}
