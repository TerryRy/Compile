package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class MulExp implements Node {
    // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
    // MulExp → UnaryExp { ('*' | '/' | '%') UnaryExp }
    private List<UnaryExp> unaryExpList;
    private List<Token> opList;

    public MulExp(List<UnaryExp> unaryExpList, List<Token> opList) {
        this.unaryExpList = unaryExpList;
        this.opList = opList;
    }

    public List<UnaryExp> getUnaryExpList() {
        return unaryExpList;
    }

    public List<Token> getOpList() {
        return opList;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        unaryExpList.get(0).print(writer);
        writer.write(Syner.nodeType.get(NodeType.MulExp));
        for (int i = 0; i < opList.size(); i++) {
            writer.write(opList.get(i).toString());
            unaryExpList.get(i + 1).print(writer);
            writer.write(Syner.nodeType.get(NodeType.MulExp));
        }
    }
}
