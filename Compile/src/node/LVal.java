package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class LVal implements Node {
    //  LVal → Ident {'[' Exp ']'}
    private Token ident;
    private List<Token> lb;
    private List<Exp> expList;
    private List<Token> rb;

    public LVal(Token ident, List<Token> lb, List<Exp> expList, List<Token> rb) {
        this.ident = ident;
        this.lb = lb;
        this.expList = expList;
        this.rb = rb;
    }

    public Token getIdent() {
        return ident;
    }

    public List<Token> getLb() {
        return lb;
    }

    public List<Exp> getExpList() {
        return expList;
    }

    public List<Token> getRb() {
        return rb;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(ident.toString());
        for (int i = 0; i < expList.size(); i++) {
            writer.write(lb.get(i).toString());
            expList.get(i).print(writer);
            writer.write(rb.get(i).toString());
        }
        writer.write(Syner.nodeType.get(NodeType.LVal));
    }
}
