package llvm2mips.llvmCommand;

import Type.Type;
import Type.FuncVoidType;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsTextLine.*;

import java.util.List;

public class Call extends LlvmCom {
    String result; // 可能的返回值
    Type returnType;
    String funcName;
    List<FRParam> parameters;

    public Call(Type returnType, String funcName, List<FRParam> parameters) {
        this.result = null;
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

    private boolean hasReturnValue() {
        return !(this.returnType.equals(FuncVoidType.typeVoid));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.hasReturnValue()) {
            // # %18 = call i32 @f1(i32 %14, i32* %16, [2 x i32]* %17)
            sb.append(this.result)
                    .append(" = ");
        }
        sb.append("call ")
                .append(this.returnType.toString())
                .append(" ")
                .append("@" + this.funcName)
                .append("(");
        if (this.parameters != null) {
            if (this.parameters.size() > 0) {
                sb.append(this.parameters.get(0).toString());
            }
            for (FRParam frParam : this.parameters) {
                sb.append(", ")
                        .append(frParam.toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void analyzeCom() {
        // # %18 = call i32 @f1(i32 %14, i32* %16, [2 x i32]* %17)
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        if (this.funcName.equals("getint")) {
            /*
            # %10 = call i32 @getint()

            GETINT()
            sw $v0, -148($sp)
             */
            Mips.getMips().addTextLines(new LineMacro("GETINT()"));
            int ty = Mips.getMips().allocLoc(1);
            Mips.getMips().addTextLines(new LineSw("$v0", ty));
            return;
        }
        if (this.funcName.equals("putint")) {
            /*
            # call void @putint(i32 %36)

            lw $a0, -288($sp)
            PUTINT()
            */
            String onType = super.getOnType(this.parameters.get(0).getValue());
            if (onType.equals("register")) {
                // 寄存器
                // lw $a0, -288($sp)
                int ty = Mips.getMips().checkMipsTab(this.parameters.get(0).getValue()).getIndex();
                Mips.getMips().addTextLines(new LineLw("$a0", ty));
            }
            else if (onType.equals("global")) {
                // 全局变量
                // la $a0, on
                Mips.getMips().addTextLines(new LineLa("$a0", this.parameters.get(0).getValue().substring(1)));
            }
            else {
                // 立即数
                // li $a0, on
                Mips.getMips().addTextLines(new LIneLi("$a0", this.parameters.get(0).getValue()));
            }
            Mips.getMips().addTextLines(new LineMacro("PUTINT()"));
            return;
        }
        if (this.funcName.equals("putch")) {
            /*
            # call void @putch(i32 1)

            li $a0, 1
            PUTCH()
             */
            Mips.getMips().addTextLines(new LIneLi("$a0", this.parameters.get(0).getValue()));
            Mips.getMips().addTextLines(new LineMacro("PUTCH()"));
            return;
        }
        // 传参
            // 预算所有参数需要的总字节数
        int paramLen = 0; //
        if (this.parameters != null) {
            for (FRParam frParam : this.parameters) {
                paramLen += super.getTy(frParam.getType(), frParam.getHasP());
            }
            for (FRParam frParam : this.parameters) {
                try {
                    String rt = Mips.getMips().getRegister();
                    String onType = super.getOnType(frParam.getValue());
                    if (onType.equals("register")) {
                        // 是寄存器
                        // lw $t0, -176($sp)
                        int ty = Mips.getMips().checkMipsTab(frParam.getValue()).getIndex();
                        Mips.getMips().addTextLines(new LineLw(rt, ty));
                    }
                    else if (onType.equals("global")) {
                        // 全局变量
                        // la $t0, a
                        Mips.getMips().addTextLines(new LineLa(rt, frParam.getValue().substring(1)));
                    }
                    else {
                        // 是立即数
                        // li $t0, 1
                        Mips.getMips().addTextLines(new LIneLi(rt, frParam.getValue()));
                    }
                    // sw $t0, -200($sp)
                    // 计算新的地址
                    int loc = Mips.getMips().allocLoc(super.getTy(frParam.getType(), frParam.getHasP()));
                    Mips.getMips().addTextLines(new LineSw(rt, loc));
                    Mips.getMips().freeRegister(rt);
                } catch (Exception e) {
                    System.err.println("寄存器申请、释放错误");
                    e.printStackTrace();
                }
            }
        }
        // 保存现场
        Mips.getMips().saveSite(-paramLen * 4);
        // jal lable
        Mips.getMips().addTextLines(new LineJal(this.funcName));
        // 恢复现场
        try {
            Mips.getMips().restoreSite();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.hasReturnValue()) {
            // 存返回值
            // sw $v0, -208($sp)
            // 这里有点麻烦，返回值存在$v0.但恢复现场时破坏了$v0,如果要提前将$v0存内存又没有$sp
            // 索性保证每次系统调用前手动设置$v0,这样$v0就不再算在现场内了
            int ty = Mips.getMips().allocLoc(1);
            Mips.getMips().addTextLines(new LineSw("$v0", ty));
        }
        else {
            // do nothing
        }
    }
}
