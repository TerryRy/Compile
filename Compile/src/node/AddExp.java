package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class AddExp implements Node {
    // AddExp → MulExp | AddExp ('+' | '−') MulExp
    // AddExp → MulExp { ('+' | '−') MulExp }
    private List<MulExp> mulExpList;
    private List<Token> opList;

    public AddExp(List<MulExp> mulExpList, List<Token> opList) {
        this.mulExpList = mulExpList;
        this.opList = opList;
    }

    public List<MulExp> getMulExpList() {
        return mulExpList;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        mulExpList.get(0).print(writer);
        writer.write(Syner.nodeType.get(NodeType.AddExp));
        for (int i = 0; i < opList.size(); i++) {
            writer.write(opList.get(i).toString());
            mulExpList.get(i + 1).print(writer);
            writer.write(Syner.nodeType.get(NodeType.AddExp));
        }
    }
}
