package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class ConstDecl implements Node{
    //  ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'

    private Token constToken;
    private BType bType;
    private List<ConstDef> constDefs;
    private List<Token> commas;
    private Token semicnToken;

    public ConstDecl(Token constToken, BType bType, List<ConstDef> constDefs, List<Token> commas, Token semicnToken) {
        this.constToken = constToken;
        this.bType = bType;
        this.constDefs = constDefs;
        this.commas = commas;
        this.semicnToken = semicnToken;
    }

    public Token getConstToken() {
        return constToken;
    }

    public BType getbType() {
        return bType;
    }

    public List<ConstDef> getConstDefs() {
        return constDefs;
    }

    public List<Token> getCommas() {
        return commas;
    }

    public Token getSemicnToken() {
        return semicnToken;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        writer.write(constToken.toString());
        bType.print(writer);
        constDefs.get(0).print(writer);
        for (int i = 1; i < constDefs.size(); i++) {
            writer.write(commas.get(i - 1).toString());
            constDefs.get(i).print(writer);
        }
        writer.write(semicnToken.toString());
        writer.write(Syner.nodeType.get(NodeType.ConstDecl));
    }
}
