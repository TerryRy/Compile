package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class VarDef implements Node {
    //  VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
    //  VarDef → Ident { '[' ConstExp ']' } [ '=' InitVal ]
    private Token ident;
    private List<Token> lb;
    private List<ConstExp> constExpList;
    private List<Token> rb;
    private Token assignToken;
    private InitVal initVal;

    public VarDef(Token ident, List<Token> lb, List<ConstExp> constExpList, List<Token> rb, Token equalToken, InitVal initVal) {
        this.ident = ident;
        this.lb = lb;
        this.constExpList = constExpList;
        this.rb = rb;
        this.assignToken = equalToken;
        this.initVal = initVal;
    }

    public Token getIdent() {
        return ident;
    }

    public List<Token> getLb() {
        return lb;
    }

    public List<ConstExp> getConstExpList() {
        return constExpList;
    }

    public List<Token> getRb() {
        return rb;
    }

    public Token getAssignToken() {
        return assignToken;
    }

    public InitVal getInitVal() {
        return initVal;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(ident.toString());
        for (int i = 0; i < rb.size(); i++) {
            writer.write(lb.get(i).toString());
            constExpList.get(i).print(writer);
            writer.write(rb.get(i).toString());
        }
        if (assignToken != null) {
            writer.write(assignToken.toString());
            initVal.print(writer);
        }
        writer.write(Syner.nodeType.get(NodeType.VarDef));
    }
}
