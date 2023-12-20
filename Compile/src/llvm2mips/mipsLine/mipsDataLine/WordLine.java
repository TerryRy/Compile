package llvm2mips.mipsLine.mipsDataLine;

import java.util.List;

public class WordLine extends MipsDataLine {
    private String name;
    private List<String> values;

    public WordLine(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name)
                .append(": .word ");
        sb.append(this.values.get(0));
        for (int i = 1; i < values.size(); i++) {
            sb.append(", ")
                    .append(this.values.get(i));
        }
        return sb.toString();
    }
}
