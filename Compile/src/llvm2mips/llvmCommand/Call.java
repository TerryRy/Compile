package llvm2mips.llvmCommand;

import Type.Type;

import java.util.List;

public class Call extends LlvmCom {
    String result; // 可能的返回值
    Type returnType;
    String funcName;
    List<FRParam> parameters;

    public Call(Type returnType, String funcName, List<FRParam> parameters) {
        result = null;
        this.returnType = returnType;
        this.funcName = funcName;
        this.parameters = parameters;
    }

    public Call(String result, Type returnType, String funcName, List<FRParam> parameters) {
        this.result = result;
        this.returnType = returnType;
        this.funcName = funcName;
        this.parameters = parameters;
    }

    @Override
    public void analyzeCom() {
        // TODO:
    }
}
