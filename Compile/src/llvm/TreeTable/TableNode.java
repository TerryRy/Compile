package llvm.TreeTable;

import llvm.TreeTable.Line.TreeTableLine;

import java.util.ArrayList;
import java.util.List;

/*
* 树装符号表的节点，以block为分割单位
* if、else、for后的stmt不单独开节点，相当于基本块只以数字的形式存在
 */
public class TableNode {
    private int parentNode; // 外层节点，由该节点进入当前节点
    private List<TreeTableLine> lines; // 内部的变量
    private List<Integer> sonNodeList; // 子节点，可由当前节点进入
    // 临时寄存器计数器，基本块内部递增，子基本块继承父基本块
    private int curTCounter;

    public TableNode(int parentNode, List<TreeTableLine> lines, List<Integer> sonNodeList, int curTCounter) {
        this.parentNode = parentNode;
        this.lines = lines;
        this.sonNodeList = sonNodeList;
        this.curTCounter = curTCounter;
    }

    // add所用声明（常用声明）
    public TableNode(int parentNode, int curTCounter) {
        this.parentNode = parentNode;
        this.lines = new ArrayList<>();
        this.sonNodeList = new ArrayList<>();
        this.curTCounter = curTCounter;
    }

    public void addLine(TreeTableLine newLine) {
        this.lines.add(newLine);
    }

    public void addSonNode(int sonNode) {
        this.sonNodeList.add(sonNode);
    }

    public int getParentNode() {
        return parentNode;
    }

    public void setParentNode(int parentNode) {
        this.parentNode = parentNode;
    }

    public List<TreeTableLine> getLines() {
        return lines;
    }

    public void setLines(List<TreeTableLine> lines) {
        this.lines = lines;
    }

    public List<Integer> getSonNodeList() {
        return sonNodeList;
    }

    public void setSonNodeList(List<Integer> sonNodeList) {
        this.sonNodeList = sonNodeList;
    }

    public int getCurTCounter() {
        return curTCounter;
    }

    public void addCurTCounter() {
        curTCounter++;
    }
}
