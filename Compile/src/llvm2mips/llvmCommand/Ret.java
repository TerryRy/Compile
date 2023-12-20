package llvm2mips.llvmCommand;

import Type.Type;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsDataLine.MacroLine;
import llvm2mips.mipsLine.mipsTextLine.*;

public class Ret extends LlvmCom {
    Type type;
    String value;

    public Ret(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Ret(Type type) {
        this.type = type;
        value = "";
    }

    @Override
    public String toString() {
        return "ret " + this.type.toString() + this.value;
    }

    @Override
    public void analyzeCom() {
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        if (this.value != null) {
            String onType = super.getOnType(this.value);
            if (onType.equals("register")) {
                // 寄存器
                int ty = Mips.getMips().checkMipsTab(this.value).getIndex();
                Mips.getMips().addTextLines(new LineLw("$v0", ty));
            }
            else if (onType.equals("global")) {
                // 全局变量
                Mips.getMips().addTextLines(new LineLa("$v0", this.value.substring(1)));
            }
            else {
                // 立即数
                Mips.getMips().addTextLines(new LIneLi("$v0", this.value));
            }
        }
        Mips.getMips().addTextLines(new LineJr());
    }
}
