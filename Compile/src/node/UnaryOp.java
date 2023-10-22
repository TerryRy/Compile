package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class UnaryOp implements Node {
    //  UnaryOp → '+' | '−' | '!'
    private Token opToken;

    public UnaryOp(Token opToken) {
        this.opToken = opToken;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(opToken.toString());
        writer.write(Syner.nodeType.get(NodeType.UnaryOp));
    }
}
