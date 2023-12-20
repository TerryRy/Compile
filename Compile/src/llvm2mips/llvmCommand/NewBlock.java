package llvm2mips.llvmCommand;

import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsTextLine.LineExegesis;
import llvm2mips.mipsLine.mipsTextLine.LineNewBlock;

public class NewBlock extends LlvmCom {
    String name;

    public NewBlock(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ":";
    }

    @Override
    public void analyzeCom() {
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        Mips.getMips().addTextLines(new LineNewBlock(name));
    }
}
