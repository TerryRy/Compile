package node;

import Syntax.Syner;
import Token.Token;
import llvm.Container.Pair;

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
    private Pair pair; // 仅在llvm的visitEqExp和visitRelExp临时手动生成UnaryExp使用，对语法树无影响

    public UnaryExp(PrimaryExp primaryExp, Token ident, Token lb, FuncRParams funcRParams, Token rb, UnaryOp unaryOp, UnaryExp unaryExp) {
        this.primaryExp = primaryExp;
        this.ident = ident;
        this.lb = lb;
        this.funcRParams = funcRParams;
        this.rb = rb;
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
        this.pair = null;
    }

    public UnaryExp(Pair pair) {
        this.primaryExp = null;
        this.ident = null;
        this.lb = null;
        this.funcRParams = null;
        this.rb = null;
        this.unaryOp = null;
        this.unaryExp = null;
        this.pair = pair;
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

    public UnaryExp getUnaryExp() {
        return unaryExp;
    }

    public UnaryOp getUnaryOp() {
        return unaryOp;
    }

    public Pair getPair() {
        return pair;
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
