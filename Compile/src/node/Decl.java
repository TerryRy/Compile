package node;

import java.io.BufferedWriter;
import java.io.IOException;

public class Decl implements Node {
    // Decl → ConstDecl | VarDecl

    private ConstDecl constDecl;
    private VarDecl varDecl;

    public Decl(ConstDecl constDecl, VarDecl varDecl) {
        this.constDecl = constDecl;
        this.varDecl = varDecl;
    }

    public ConstDecl getConstDecl() {
        return constDecl;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }


    @Override
    public void print(BufferedWriter writer) throws IOException {
        if (constDecl != null) {
            constDecl.print(writer);
        }
        else {
            varDecl.print(writer);
        }
//        writer.write(Syner.nodeType.get(NodeType.VarDecl));
    }
}
