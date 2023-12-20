package llvm2mips.mipsLine.mipsTextLine;

public class LineSyscall extends MipsTextLine {

    @Override
    public String toMips() {
        return "syscall";
    }
}
