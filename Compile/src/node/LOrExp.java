package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class LOrExp implements Node {
    //  LOrExp → LAndExp | LOrExp '||' LAndExp
    //  LOrExp → LAndExp { '||' LAndExp }
    private List<LAndExp> lAndExpList;
    private List<Token> opList;

    public LOrExp(List<LAndExp> lAndExpList, List<Token> opList) {
        this.lAndExpList = lAndExpList;
        this.opList = opList;
    }

    public List<LAndExp> getLAndExpList() {
        return lAndExpList;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        lAndExpList.get(0).print(writer);
        writer.write(Syner.nodeType.get(NodeType.LOrExp));
        for (int i = 0; i < opList.size(); i++) {
            writer.write(opList.get(i).toString());
            lAndExpList.get(i + 1).print(writer);
            writer.write(Syner.nodeType.get(NodeType.LOrExp));
        }
    }
}
