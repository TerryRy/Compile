package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class ConstInitVal implements Node {
    // ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
    private ConstExp constExp;
    private Token lb;
    private List<ConstInitVal> constInitValList;
    private List<Token> commas;
    private Token rb;

    public ConstInitVal(ConstExp constExp, Token lb, List<ConstInitVal> constInitValList, List<Token> commas, Token rb) {
        this.constExp = constExp;
        this.lb = lb;
        this.constInitValList = constInitValList;
        this.commas = commas;
        this.rb = rb;
    }

    public ConstExp getConstExp() {
        return constExp;
    }

    public Token getLb() {
        return lb;
    }
    public List<ConstInitVal> getConstInitValList() {
        return constInitValList;
    }

    public List<Token> getCommas() {
        return commas;
    }

    public Token getRb() {
        return rb;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        if (constExp != null) {
            constExp.print(writer);
        }
        else {
            writer.write(lb.toString());
            constInitValList.get(0).print(writer);
            for (int i = 1; i < constInitValList.size(); i++) {
                writer.write(commas.get(i - 1).toString());
                constInitValList.get(i).print(writer);
            }
            writer.write(rb.toString());
            writer.write(Syner.nodeType.get(NodeType.ConstInitVal));
        }
    }
}
