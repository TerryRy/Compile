import Lexical.Lexer;
import Syntax.Syner;
import config.Config;

import java.io.*;

public class Compiler {
    // 关于测评的配置（主要是输出）
    public static final boolean printLex = true;
//    public static final boolean printSyn = true;
//    public static Lexer lexer = Lexer.getLexer();
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./testfile.txt"))) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt"));
            writer.write("");
            writer.close();
            writer = new BufferedWriter(new FileWriter("./output.txt", true));
            Lexer lexer = Lexer.getLexer();
            String line = "";
            for (int i = 0; (line = reader.readLine()) != null; i++) {
                lexer.initLexer();
                lexer.setSource(line);
                lexer.setPrintAble(printLex);
                lexer.analyse();
            }

            Syner.getSyner().setTokens(lexer.getTokens());
            Syner.getSyner().analyze();
            if (Config.syner) {
                Syner.getSyner().SynerPrinter(writer);
            }

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
