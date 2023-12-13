package llvm2mips.MipsTable;

import Type.Type;
import Type.IntegerType;
import Type.LineArrayType;

public class MipsTabLin {
    String name; // llvm寄存器名
    Type type; // llvm寄存器所存类型
    int index; // 对应在mips内存区的地址

    public MipsTabLin(String name, Type type, int index) {
        this.name = name;
        this.type = type;
        this.index = index;
    }

    // 获取该值长度，实际使用时还需手动*4
    public int getLen() {
        if (type instanceof IntegerType) {
            return 1;
        }
        else if (type instanceof LineArrayType) {
            int length = 1;
            for (Integer len : ((LineArrayType) type).getLenList()) {
                length *= len;
            }
            return length;
        }
        else {
            throw new RuntimeException("Invalid num length");
        }
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
