package node;

import Syntax.Syner;

import java.io.BufferedWriter;
import java.io.IOException;

public class ConstExp implements Node{
    //  ConstExp → AddExp
    private AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        addExp.print(writer);
        writer.write(Syner.nodeType.get(NodeType.ConstExp));
    }
}
