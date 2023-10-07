package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class ForStmt implements Node {
    // ForStmt → LVal '=' Exp
    private LVal lVal;
    private Token assignToken;
    private Exp exp;

    public ForStmt(LVal lVal, Token assignToken, Exp exp) {
        this.lVal = lVal;
        this.assignToken = assignToken;
        this.exp = exp;
    }

    public LVal getlVal() {
        return lVal;
    }

    public Token getAssignToken() {
        return assignToken;
    }

    public Exp getExp() {
        return exp;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        lVal.print(writer);
        writer.write(assignToken.toString());
        exp.print(writer);
        writer.write(Syner.nodeType.get(NodeType.ForStmt));
    }
}
