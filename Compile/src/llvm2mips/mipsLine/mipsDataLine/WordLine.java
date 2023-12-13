package llvm2mips.mipsLine.mipsDataLine;

import java.util.List;

public class WordLine extends MipsDataLine {
    private String name;
    private List<Integer> values;

    public WordLine(String name, List<Integer> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String toMips() {
        // TODO:
        return null;
    }
}
