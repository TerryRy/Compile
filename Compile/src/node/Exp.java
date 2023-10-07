package node;

import Syntax.Syner;

import java.io.BufferedWriter;
import java.io.IOException;

public class Exp implements Node {
    // Exp → AddExp
    private AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        addExp.print(writer);
        writer.write(Syner.nodeType.get(NodeType.Exp));
    }
}
