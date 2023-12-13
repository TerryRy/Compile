package llvm2mips.llvmCommand;

import Type.Type;

public class Load extends LlvmCom {
    Type typeResult; // 寄存器中
    boolean t1HasStar;
    String result;
    Type typeValue; // 内存中
    boolean t2HasStar;
    String value;


    public Load(Type typeResult, String result, Type typeValue, String value) {
        this.typeResult = typeResult;
        this.t1HasStar = false;
        this.result = result;
        this.typeValue = typeValue;
        this.t2HasStar = false;
        this.value = value;
    }

    public Load(Type typeResult, boolean t1HasStar, String result, Type typeValue, boolean t2HasStar, String value) {
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
