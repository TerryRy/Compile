package node;

import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class BType implements Node {
    // BType → 'int'
    private Token intToken;

    public BType(Token intToken) {
        this.intToken = intToken;
    }

    public Token getIntToken() {
        return intToken;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(intToken.toString());
        // do nothing
    }
}
