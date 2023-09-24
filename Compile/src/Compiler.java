import Lexical.Lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Compiler {
    // 关于测评的配置（主要是输出）
    public static final boolean printLex = true;
//    public static Lexer lexer = Lexer.getLexer();
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./testfile.txt"))) {
            Lexer lexer = Lexer.getLexer();
            String line = "";
            for (int i = 0; (line = reader.readLine()) != null; i++) {
                lexer.initLexer();
                lexer.setSource(line);
                lexer.setPrintAble(printLex);
                lexer.analyse();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
