package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class FuncType implements Node {
    // FuncType → 'void' | 'int'
    private Token voidToken;
    private Token intToken;

    public FuncType(Token voidToken, Token intToken) {
        this.voidToken = voidToken;
        this.intToken = intToken;
    }

    public Token getVoidToken() {
        return voidToken;
    }

    public Token getIntToken() {
        return intToken;
    }

    public int getType() {
        if (voidToken != null) {
            return 0;
        }
        else {
            return 1;
        }
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        if (voidToken != null) {
            writer.write(voidToken.toString());
        }
        else {
            writer.write(intToken.toString());
        }
        writer.write(Syner.nodeType.get(NodeType.FuncType));
    }
}
