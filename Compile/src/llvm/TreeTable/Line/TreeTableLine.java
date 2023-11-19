package llvm.TreeTable.Line;

/*
* 树状表项父类，只有两种子类：变量和函数
 */
public class TreeTableLine {
    private String name; // 表项名
    private String tNum; // 临时寄存器编号
    private int bbNum; // 所属block编号,意义不大

    public TreeTableLine(String name, String tNum,  int bbNum) {
        this.name = name;
        this.tNum = tNum;
        this.bbNum = bbNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTNum() {
        return tNum;
    }

    public void settNum(String tNum) {
        this.tNum = tNum;
    }
}
