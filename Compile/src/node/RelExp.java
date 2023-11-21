package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class RelExp implements Node{
    // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
    // RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp }
    private List<AddExp> addExpList;
    private List<Token> opList;

    public RelExp(List<AddExp> addExpList, List<Token> opList) {
        this.addExpList = addExpList;
        this.opList = opList;
    }

    public List<AddExp> getAddExpList() {
        return addExpList;
    }

    public List<Token> getOpList() {
        return opList;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        addExpList.get(0).print(writer);
        writer.write(Syner.nodeType.get(NodeType.RelExp));
        for (int i = 0; i < opList.size(); i++) {
            writer.write(opList.get(i).toString());
            addExpList.get(i + 1).print(writer);
            writer.write(Syner.nodeType.get(NodeType.RelExp));
        }
    }
}
