package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class FuncFParam implements Node {
    //  FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
    private BType bType;
    private Token ident;
    private List<Token> lb;
    private List<Token> rb;
    private List<ConstExp> constExpList;

    public FuncFParam(BType bType, Token ident, List<Token> lb, List<Token> rb, List<ConstExp> constExpList) {
        this.bType = bType;
        this.ident = ident;
        this.lb = lb;
        this.rb = rb;
        this.constExpList = constExpList;
    }

    public BType getbType() {
        return bType;
    }

    public Token getIdent() {
        return ident;
    }

    public List<Token> getLb() {
        return lb;
    }

    public List<Token> getRb() {
        return rb;
    }

    public List<ConstExp> getConstExpList() {
        return constExpList;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        bType.print(writer);
        writer.write(ident.toString());
        if (lb.size() > 0) {
            writer.write(lb.get(0).toString());
            writer.write(rb.get(0).toString());
            for (int i = 1; i < lb.size(); i++) {
                writer.write(lb.get(i).toString());
                constExpList.get(i - 1).print(writer);
                writer.write(rb.get(i).toString());
            }
        }
        writer.write(Syner.nodeType.get(NodeType.FuncFParam));
    }
}
