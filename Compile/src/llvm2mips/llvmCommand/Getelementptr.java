package llvm2mips.llvmCommand;

import Type.Type;

import java.util.List;

public class Getelementptr extends LlvmCom {
    String result;
    Type firstType; // 第一个偏移的类型
    boolean t1HasStar;
    Type valueType; // 数组的类型
    boolean t2HasStar;
    String value; // 数组寄存器
    List<String> tys; // 偏移量

    public Getelementptr(String result, Type firstType, boolean t1HasStar, Type valueType, boolean t2HasStar, String value, List<String> tys) {
        this.result = result;
        this.firstType = firstType;
        this.t1HasStar = t1HasStar;
        this.valueType = valueType;
        this.t2HasStar = t2HasStar;
        this.value = value;
        this.tys = tys;
    }

    public Getelementptr(String result, Type firstType, Type valueType, String value, List<String> tys) {
        this.result = result;
        this.firstType = firstType;
        this.t1HasStar = false;
        this.valueType = valueType;
        this.t2HasStar = false;
        this.value = value;
        this.tys = tys;
    }

    @Override
    public void analyzeCom() {
        // TODO:
    }
}
