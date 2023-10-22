package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class EqExp implements Node {
    //  EqExp → RelExp | EqExp ('==' | '!=') RelExp
    //  EqExp → RelExp { ('==' | '!=') RelExp }
    private List<RelExp> relExpList;
    private List<Token> opList;

    public EqExp(List<RelExp> relExpList, List<Token> opList) {
        this.relExpList = relExpList;
        this.opList = opList;
    }

    public List<RelExp> getRelExpList() {
        return relExpList;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        relExpList.get(0).print(writer);
        writer.write(Syner.nodeType.get(NodeType.EqExp));
        for (int i = 0; i < opList.size(); i++) {
            writer.write(opList.get(i).toString());
            relExpList.get(i + 1).print(writer);
            writer.write(Syner.nodeType.get(NodeType.EqExp));
        }
    }
}
