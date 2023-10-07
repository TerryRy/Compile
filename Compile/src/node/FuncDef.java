package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class FuncDef implements Node {
    // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
    private FuncType funcType;
    private Token ident;
    private Token lb;
    private FuncFParams funcFParams;
    private Token rb;
    private Block block;

    public FuncDef(FuncType funcType, Token ident, Token lb, FuncFParams funcFParams, Token rb, Block block) {
        this.funcType = funcType;
        this.ident = ident;
        this.lb = lb;
        this.funcFParams = funcFParams;
        this.rb = rb;
        this.block = block;
    }

    public FuncType getFuncType() {
        return funcType;
    }

    public Token getIdent() {
        return ident;
    }

    public Token getLb() {
        return lb;
    }

    public FuncFParams getFuncFParams() {
        return funcFParams;
    }

    public Token getRb() {
        return rb;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        funcType.print(writer);
        writer.write(ident.toString());
        writer.write(lb.toString());
        if (funcFParams != null) {
            funcFParams.print(writer);
        }
        writer.write(rb.toString());
        block.print(writer);
        writer.write(Syner.nodeType.get(NodeType.FuncDef));
    }
}
