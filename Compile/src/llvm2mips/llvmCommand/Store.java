package llvm2mips.llvmCommand;

import Type.Type;

public class Store extends LlvmCom {
    Type typeResult; // 内存中
    boolean t1HasStar;
    String result;
    Type typeValue; // 寄存器中
    boolean t2HasStar;
    String value;

    public Store(Type typeValue, String value, Type typeResult, String result) {
        this.typeValue = typeValue;
        this.t1HasStar = false;
        this.value = value;
        this.typeResult = typeResult;
        this.t2HasStar = false;
        this.result = result;
    }

    public Store(Type typeResult, boolean t1HasStar, String result, Type typeValue, boolean t2HasStar, String value) {
        this.typeResult = typeResult;
        this.t1HasStar = t1HasStar;
        this.result = result;
        this.typeValue = typeValue;
        this.t2HasStar = t2HasStar;
        this.value = value;
    }

    @Override
    public void analyzeCom() {
        // TODO:
    }
}
