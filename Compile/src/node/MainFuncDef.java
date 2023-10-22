package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;

public class MainFuncDef implements Node {
    //  MainFuncDef → 'int' 'main' '(' ')' Block
    private Token intToken;
    private Token mainToken;
    private Token lb;
    private Token rb;
    private Block block;

    public MainFuncDef(Token intToken, Token mainToken, Token lb, Token rb, Block block) {
        this.intToken = intToken;
        this.mainToken = mainToken;
        this.lb = lb;
        this.rb = rb;
        this.block = block;
    }

    public Token getLb() {
        return lb;
    }

    public Token getRb() {
        return rb;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(intToken.toString());
        writer.write(mainToken.toString());
        writer.write(lb.toString());
        writer.write(rb.toString());
        block.print(writer);
        writer.write(Syner.nodeType.get(NodeType.MainFuncDef));
    }
}
