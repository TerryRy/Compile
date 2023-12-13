package llvm2mips.llvmCommand;

public class Br extends LlvmCom {
    String checker;
    String label1;
    String label0;

    public Br(String checker, String label1, String label0) {
        this.checker = checker;
        this.label1 = label1;
        this.label0 = label0;
    }

    public Br(String label1) {
        this.label1 = label1;
    }

    @Override
    public void analyzeCom() {
        // TODO:
    }
}
