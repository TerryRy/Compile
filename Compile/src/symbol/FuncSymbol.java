package symbol;

import java.util.List;

public class FuncSymbol extends Symbol {
    private int type; // 返回值类型。0为void，1为int
    private List<FuncParam> funcParamList; // 参数列表

    public FuncSymbol(String name, int type, List<FuncParam> funcParamList) {
        super(name);
        this.type = type;
        this.funcParamList = funcParamList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<FuncParam> getFuncParamList() {
        return funcParamList;
    }
}
