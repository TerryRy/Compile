package llvm2mips.llvmCommand;

import Type.Type;

public class Alloca extends LlvmCom {
    String result; // 左值
    Type type; // type

    public Alloca(String result, Type type) {
        this.result = result;
        this.type = type;
    }

    @Override
    public void analyzeCom() {
        // %a1 = alloca i32
        // TODO:
    }
}
