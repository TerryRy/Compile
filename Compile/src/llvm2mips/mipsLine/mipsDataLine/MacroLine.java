package llvm2mips.mipsLine.mipsDataLine;

import llvm2mips.mipsLine.mipsTextLine.MipsTextLine;

import java.util.List;

public class MacroLine extends MipsDataLine {
    private String name;
    private List<MipsTextLine> lines;

    public MacroLine(String name, List<MipsTextLine> lines) {
        this.name = name;
        this.lines = lines;
    }

    @Override
    public String toMips() {
        // TODO:
        return super.toMips();
    }
}
