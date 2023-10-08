package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class UnaryExp implements Node {
    // UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    private PrimaryExp primaryExp;
    private Token ident;
    private Token lb;
    private FuncRParams funcRParams;
    private Token rb;
    private UnaryOp unaryOp;
    private UnaryExp unaryExp;

    public UnaryExp(PrimaryExp primaryExp, Token ident, Token lb, FuncRParams funcRParams, Token rb, UnaryOp unaryOp, UnaryExp unaryExp) {
        this.primaryExp = primaryExp;
        this.ident = ident;
        this.lb = lb;
        this.funcRParams = funcRParams;
        this.rb = rb;
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public PrimaryExp getPrimaryExp() {
        return primaryExp;
    }

    public Token getIdent() {
        return ident;
    }

    public Token getLb() {
        return lb;
    }

    public FuncRParams getFuncRParams() {
        return funcRParams;
    }

    public Token getRb() {
        return rb;
    }

    public UnaryOp getUnaryOp() {
        return unaryOp;
    }

    public UnaryExp getUnaryExp() {
        return unaryExp;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        if (primaryExp != null) {
            primaryExp.print(writer);
        }
        else if (ident != null) {
            writer.write(ident.toString());
            writer.write(lb.toString());
            if (funcRParams != null) {
                funcRParams.print(writer);
            }
            writer.write(rb.toString());
        }
        else {
            unaryOp.print(writer);
            unaryExp.print(writer);
        }
        writer.write(Syner.nodeType.get(NodeType.UnaryExp));
    }
}
