package llvm2mips.mipsLine.mipsTextLine;

public class LineBeqz extends MipsTextLine {
    // beqz $t0, label_unique51
    String register; // 待判断寄存器
    String label; // 0是时跳转位置

    public LineBeqz(String register, String label) {
        this.register = register;
        this.label = label;
    }

    @Override
    public String toMips() {
        return "beqz " + register + ", " + label;
    }
}
