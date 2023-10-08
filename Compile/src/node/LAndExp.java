package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class LAndExp implements Node {
    // LAndExp → EqExp | LAndExp '&&' EqExp
    // LAndExp → EqExp { '&&' EqExp }
    private List<EqExp> eqExpList;
    private List<Token> opList;

    public LAndExp(List<EqExp> eqExpList, List<Token> opList) {
        this.eqExpList = eqExpList;
        this.opList = opList;
    }

    public List<EqExp> getEqExpList() {
        return eqExpList;
    }

    public List<Token> getOpList() {
        return opList;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        eqExpList.get(0).print(writer);
        writer.write(Syner.nodeType.get(NodeType.LAndExp));
        for (int i = 0; i < opList.size(); i++) {
            writer.write(opList.get(i).toString());
            eqExpList.get(i + 1).print(writer);
            writer.write(Syner.nodeType.get(NodeType.LAndExp));
        }
    }
}
