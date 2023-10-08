package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class FuncRParams implements Node {
    //  FuncRParams → Exp { ',' Exp }
    private List<Exp> expList;
    private List<Token> commas;

    public FuncRParams(List<Exp> expList, List<Token> commas) {
        this.expList = expList;
        this.commas = commas;
    }

    public List<Exp> getExpList() {
        return expList;
    }

    public List<Token> getCommas() {
        return commas;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        expList.get(0).print(writer);
        for (int i = 1; i < expList.size(); i++) {
            writer.write(commas.get(i - 1).toString());
            expList.get(i).print(writer);
        }
        writer.write(Syner.nodeType.get(NodeType.FuncRParams));
    }
}
