package llvm2mips.llvmCommand;

import Type.Type;
import Type.IntegerType;
import Type.LineArrayType;

public class LlvmCom {
    public void analyzeCom() {
    }

    // 判断type的类型，是常规变量、指针，还是数组
    // 由于llvm传来的判断方式不统一，需要综合考虑实例化类、hasP和len==-1
    private int getTypeT(Type type, boolean hasP) {
        if (type instanceof IntegerType) {
            // 常规变量
            return 0;
        } else if (type instanceof LineArrayType) {
            if (((LineArrayType) type).getLen() == -1) {
                // 指针
                return 0;
            }
            if (hasP) {
                // 指针
                return 0;
            }
            // 正常数组
            return 1;
        } else {
            System.err.println("Invalid type");
            return -1;
        }
    }

    // 返回存该变量需要的字节数，常规变量、指针需要1，数组需要basic元素个数
    // 返回的是正值
    public int getTy(Type type, boolean hasP) {
        int typeT = this.getTypeT(type, hasP);
        if (typeT == 0) {
            // 两个字节
            return 2;
        } else if (typeT == 1) {
            // 正常数组
            // -RC/4 + 1, 连乘lenList再+1
            int ty = 1;
            for (Integer i : ((LineArrayType) type).getLenList()) {
                ty *= i;
            }
            ty += 1;
            return ty;
        } else {
            System.err.println("Invalid type");
            return -1;
        }
    }

    protected String getOnType(String value) {
        if (value.charAt(0) == '@') {
            return "global";
        }
        else if (value.charAt(0) == '%') {
            return "register";
        }
        else {
            return "immediate";
        }
    }
}
