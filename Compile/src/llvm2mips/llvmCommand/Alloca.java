package llvm2mips.llvmCommand;

import Type.Type;
import Type.IntegerType;
import Type.LineArrayType;
import llvm2mips.Mips;
import llvm2mips.MipsTable.MipsTabLin;
import llvm2mips.mipsLine.mipsTextLine.LineExegesis;
import llvm2mips.mipsLine.mipsTextLine.LineSw;

public class Alloca extends LlvmCom {
    String result; // 左值
    Type type; // type

    public Alloca(String result, Type type) {
        this.result = result;
        this.type = type;
    }

//    private int checkTypeT() {
//        // 我犯浑给arrayFunc toString加了*，这里用类辨别
//        // arrayFunc内部还是传统的用len=-1分辨，len=-1时其实是传指针
//        if (this.type instanceof IntegerType) {
//            // 常规变量
//            return 0;
//        }
//        else if (this.type instanceof LineArrayType) {
//            // 数组
//            if (((LineArrayType) this.type).getLen() == -1) {
//                // 数组指针
//                return 0; // 指针和常规变量相同，都只需要一个字节
//            }
//            else {
//                // 正常数组
//                return 1;
//            }
//        }
//        return 0;
//    }

    // 计算需要的字节数,返回正值
//    private int getTy() {
//        int typeT = this.checkTypeT();
//        if (typeT == 0) {
//            // 常规变量或指针
//            return 2;
//        }
//        else if (typeT == 2) {
//            // 正常数组, -RC/4 + 1, 连乘lenList再+1
//            int ty = 1;
//            for (Integer i : ((LineArrayType) this.type).getLenList()) {
//                ty *= i;
//            }
//            ty += 1;
//            return ty;
//        }
//        else {
//            System.err.println("Invalid type");
//            return -1;
//        }
//    }

    @Override
    public String toString() {
        return this.result + " = alloca " + this.type.toString();
    }

    @Override
    public void analyzeCom() {
        // %a1 = alloca i32
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        /* 作为“指针”
        一般变量存一个变量真是所在的地址
        数组存数组首位的地址
        注意先填数组指针再填数组内容，并且是向上增长的，相当于
        a(value)
        // a(ptr) (=loc(a(value))

        a[0]
        a[1]
        a[2]
        // a(ptr) (=loc(a[0])

        对数组，需要跨过RC个bit，即申请的是$sp + RC(RC是负的嘛)
        即一共k个元素的数组，申请跨过k个字节，sp-4k的地址（sp当前值就是空地址，所以不用再额外加一个
        */
        try {
            // 计算需要的字节数
            int ty = super.getTy(this.type, false);
            int len = Mips.getMips().allocLoc(ty);
//            // 计算指针位置需要存储的地址（getLoc+4
//            String valueRt = Mips.getMips().getLoc(ty - 1);
//            // sw rt, -ty($sp)
//            Mips.getMips().addTextLines(new LineSw(valueRt, len));
            // 填符号表
            Mips.getMips().addMipsTab(this.result, this.type, len);
//            // 释放寄存器
//            Mips.getMips().freeRegister(valueRt);
        } catch (Exception e) {
            System.err.println("寄存器申请、释放错误");
            e.printStackTrace();
        }
    }
}
