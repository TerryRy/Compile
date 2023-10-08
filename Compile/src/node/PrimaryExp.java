package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class PrimaryExp implements Node {
    // PrimaryExp → '(' Exp ')' | LVal | Number
    private Token lb;
    private Exp exp;
    private Token rb;
    private LVal lVal;
    private NumberNode number;

    public PrimaryExp(Token lb, Exp exp, Token rb, LVal lVal, NumberNode number) {
        this.lb = lb;
        this.exp = exp;
        this.rb = rb;
        this.lVal = lVal;
        this.number = number;
    }

    public Token getLb() {
        return lb;
    }

    public Exp getExp() {
        return exp;
    }

    public Token getRb() {
        return rb;
    }

    public LVal getLVal() {
        return lVal;
    }

    public NumberNode getNumber() {
        return number;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        if (lb != null) {
            writer.write(lb.toString());
            exp.print(writer);
            writer.write(rb.toString());
        }
        else if (lVal != null) {
            lVal.print(writer);
        }
        else {
            number.print(writer);
        }
        writer.write(Syner.nodeType.get(NodeType.PrimaryExp));
    }
}
