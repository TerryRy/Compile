package node;

import Syntax.Syner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class CompUnit implements Node {
    // CompUnit -> {Decl} {FuncDef} MainFuncDef

    private List<Decl> decls;
    private List<FuncDef> funcDefs;
    private MainFuncDef mainFuncDef;

    public CompUnit(List<Decl> decls, List<FuncDef> funcDefs, MainFuncDef mainFuncDef) {
        this.decls = decls;
        this.funcDefs = funcDefs;
        this.mainFuncDef = mainFuncDef;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        for (Decl decl : decls) {
            decl.print(writer);
        }
        for (FuncDef funcDef : funcDefs) {
            funcDef.print(writer);
        }
        mainFuncDef.print(writer);
        writer.write(Syner.nodeType.get(NodeType.CompUnit));
    }

    public List<Decl> getDecls() {
        return decls;
    }

    public List<FuncDef> getFuncDefs() {
        return funcDefs;
    }

    public MainFuncDef getMainFuncDef() {
        return mainFuncDef;
    }

}
