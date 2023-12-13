package llvm2mips.llvmCommand;

import Type.Type;

import java.util.List;

public class DeclareFunc extends LlvmCom {
    boolean is_dso_local;
    Type type;
    String name;
    List<FFParam> ffParams;

    public DeclareFunc(boolean is_dso_local, Type type, String name, List<FFParam> ffParams) {
        this.is_dso_local = is_dso_local;
        this.type = type;
        this.name = name;
        this.ffParams = ffParams;
    }

    @Override
    public void analyzeCom() {
        // TODO
    }
}
