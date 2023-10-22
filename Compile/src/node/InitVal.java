package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class InitVal implements Node {
    // InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
    private Exp exp;
    private Token lb;
    private List<InitVal> initValList;
    private List<Token> commas;
    private Token rb;

    public InitVal(Exp exp, Token lb, List<InitVal> initValList, List<Token> commas, Token rb) {
        this.exp = exp;
        this.lb = lb;
        this.initValList = initValList;
        this.commas = commas;
        this.rb = rb;
    }

    public Exp getExp() {
        return exp;
    }

    public Token getLb() {
        return lb;
    }

    public List<InitVal> getInitValList() {
        return initValList;
    }

    public Token getRb() {
        return rb;

    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        if (exp != null) {
            exp.print(writer);
        }
        else {
            writer.write(lb.toString());
            if (initValList.size() > 0) {
                initValList.get(0).print(writer);
                for (int i = 1; i < initValList.size(); i++) {
                    writer.write(commas.get(i - 1).toString());
                    initValList.get(i).print(writer);
                }
            }
            writer.write(rb.toString());
        }
        writer.write(Syner.nodeType.get(NodeType.InitVal));
    }
}
