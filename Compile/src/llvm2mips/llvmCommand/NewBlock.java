package llvm2mips.llvmCommand;

public class NewBlock extends LlvmCom {
    String name;

    public NewBlock(String name) {
        this.name = name;
    }

    @Override
    public void analyzeCom() {
        // TODO: analyze llvm command
    }
}
