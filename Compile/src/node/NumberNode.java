package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class NumberNode implements Node {
    // Number → IntConst
    private Token intConstToken;

    public NumberNode(Token intConstToken) {
        this.intConstToken = intConstToken;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(intConstToken.toString());
        writer.write(Syner.nodeType.get(NodeType.Number));
    }
}
