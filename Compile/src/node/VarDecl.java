package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class VarDecl implements Node{
    // VarDecl → BType VarDef { ',' VarDef } ';'
    private BType bType;
    private List<VarDef> varDefList;
    private List<Token> commas;
    private Token semicn;

    public VarDecl(BType bType, List<VarDef> varDefList, List<Token> commas, Token semicn) {
        this.bType = bType;
        this.varDefList = varDefList;
        this.commas = commas;
        this.semicn = semicn;
    }

    public List<VarDef> getVarDefList() {
        return varDefList;
    }

    public List<Token> getCommas() {
        return commas;
    }

    public BType getbType() {
        return bType;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        bType.print(writer);
        varDefList.get(0).print(writer);
        for (int i = 1; i < varDefList.size(); i++) {
            writer.write(commas.get(i - 1).toString());
            varDefList.get(i).print(writer);
        }
        writer.write(semicn.toString());
        writer.write(Syner.nodeType.get(NodeType.VarDecl));
    }
}
