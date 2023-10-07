package node;

import Syntax.Syner;

import java.io.BufferedWriter;
import java.io.IOException;

public class Cond implements Node {
    // Cond → LOrExp
    private LOrExp lOrExp;

    public Cond(LOrExp lOrExp) {
        this.lOrExp = lOrExp;
    }

    public LOrExp getLOrExp() {
        return lOrExp;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        lOrExp.print(writer);
        writer.write(Syner.nodeType.get(NodeType.Cond));
    }
}
