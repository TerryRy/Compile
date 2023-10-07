package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class Block implements Node {
    // Block → '{' { BlockItem } '}'
    private Token lb;
    private List<BlockItem> blockItemList;
    private Token rb;

    public Block(Token lb,List<BlockItem> blockItemList ,Token rb) {
        this.lb = lb;
        this.blockItemList = blockItemList;
        this.rb = rb;
    }

    public Token getLb() {
        return lb;
    }

    public List<BlockItem> getBlockItemList() {
        return blockItemList;
    }

    public Token getRb() {
        return rb;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(lb.toString());
        for (BlockItem blockItem : blockItemList) {
            blockItem.print(writer);
        }
        writer.write(rb.toString());
        writer.write(Syner.nodeType.get(NodeType.Block));
    }
}
