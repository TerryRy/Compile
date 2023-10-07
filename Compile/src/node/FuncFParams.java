package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class FuncFParams implements Node {
    // FuncFParams → FuncFParam { ',' FuncFParam }
    private List<FuncFParam> funcFParamList;
    private List<Token> commas;

    public FuncFParams(List<FuncFParam> funcFParamList, List<Token> commas) {
        this.funcFParamList = funcFParamList;
        this.commas = commas;
    }

    public List<FuncFParam> getFuncFParamList() {
        return funcFParamList;
    }

    public List<Token> getCommas() {
        return commas;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        funcFParamList.get(0).print(writer);
        for (int i = 1; i < funcFParamList.size(); i++) {
            writer.write(commas.get(i - 1).toString());
            funcFParamList.get(i).print(writer);
        }
        writer.write(Syner.nodeType.get(NodeType.FuncFParams));
    }
}
