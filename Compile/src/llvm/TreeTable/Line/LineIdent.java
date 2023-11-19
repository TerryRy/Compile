package llvm.TreeTable.Line;

import Type.Type;

/*
* 常量变量数组非数组的公共父类
 */
public class LineIdent extends TreeTableLine {
    private boolean isConst; // 是否是常量
    // 通过多态方式判断是否是array

    public LineIdent(String name, String tNum, boolean isConst, int bbNum) {
        super(name, tNum, bbNum);
        this.isConst = isConst;
    }

    public boolean getIsConst() {
        return this.isConst;
    }
}
