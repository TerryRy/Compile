package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class ConstDef implements Node {
    // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
    private Token ident;
    private List<Token> lb;
    private List<ConstExp> constExps;
    private List<Token> rb;
    private Token equalToken;
    private ConstInitVal constInitVal;

    public ConstDef(Token ident, List<Token> lb, List<ConstExp> constExps, List<Token> rb, Token equalToken, ConstInitVal constInitVal) {
        this.ident = ident;
        this.lb = lb;
        this.constExps = constExps;
        this.rb = rb;
        this.equalToken = equalToken;
        this.constInitVal = constInitVal;
    }

    public Token getIdent() {
        return ident;
    }

    public List<Token> getLb() {
        return lb;
    }

    public List<ConstExp> getConstExps() {
        return constExps;
    }

    public List<Token> getRb() {
        return rb;
    }

    public Token getEqualToken() {
        return equalToken;
    }

    public ConstInitVal getConstInitVal() {
        return constInitVal;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(ident.toString());
        for (int i = 0; i < lb.size(); i++) {
            writer.write(lb.get(i).toString());
            constExps.get(i).print(writer);
            writer.write(rb.get(i).toString());
        }
        writer.write(equalToken.toString());
        constInitVal.print(writer);
        writer.write(Syner.nodeType.get(NodeType.ConstDef));
    }
}
