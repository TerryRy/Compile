package llvm2mips.llvmCommand;

import Type.Type;
import Type.LineArrayType;
import Type.IntegerType;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsDataLine.LineDataExegesis;
import llvm2mips.mipsLine.mipsDataLine.WordLine;
import llvm2mips.mipsLine.mipsTextLine.LineExegesis;

import java.util.ArrayList;
import java.util.List;

public class DefineIdent extends LlvmCom {
    boolean isConst;
    Type type;
    String name;
    String values;

    public DefineIdent(boolean isConst, Type type, String name, String values) {
        this.isConst = isConst;
        this.type = type;
        this.name = name;
        this.values = values;
    }

    private List<String> readValues() {
        // [[i32 1, i32 2], [i32 3, i32 4]]
        // [[i32 1, i32 2], zeroinitializer]
        List<String> values = new ArrayList<>();
        if (this.type instanceof IntegerType) {
            values.add(this.values);
        }
        else {
            int stack = 0; // 已经吃进去的'['数
            for (int i = 0; i < this.values.length(); i++) {
                if (this.values.charAt(i) == '[') {
                    stack++;
                    continue;
                }
                else if (this.values.charAt(i) == ']') {
                    stack--;
                    continue;
                }
                else if (this.values.charAt(i) == 'i') {
                    i++;
                    while (Character.isDigit(this.values.charAt(i))) {
                        i++;
                    }
                    i--;
                }
                else if (Character.isDigit(this.values.charAt(i))) {
                    StringBuilder sb = new StringBuilder();
                    while (Character.isDigit(this.values.charAt(i))) {
                        sb.append(this.values.charAt(i));
                        i++;
                    }
                    values.add(sb.toString());
                    i--;
                }
                else if (this.values.charAt(i) == 'z') {
                    // zeroinitializer
                    Type curType = this.type;
                    for (int k = 0; k < stack; k++) {
                        curType = ((LineArrayType) this.type).getElementType();
                    }
                    for (int l = 0; l < ((LineArrayType) curType).getLen(); l++) {
                        values.add("0");
                    }
                }
                else {
                    continue;
                }
            }
        }
        return values;
    }

    @Override
    public void analyzeCom() {
        /*
        @a = global i32 0
        @b = constant i32 1
        @c = global [2 x i32] [i32 1, i32 2]
         */
        StringBuilder sb_e = new StringBuilder("@");
        sb_e.append(this.name)
                .append(" = ")
                .append(this.isConst ? "constant " : "global ")
                .append(this.type.toString())
                .append(" ")
                .append(this.values);
        Mips.getMips().addDataLines(new LineDataExegesis(sb_e.toString()));
        // c: .word 1, 2
        List<String> values = this.readValues();
        Mips.getMips().addDataLines(new WordLine(this.name, values));
        // 填符号表,全局变量不用填符号表
    }
}
