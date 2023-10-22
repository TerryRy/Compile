import Lexical.Lexer;
import Syntax.Syner;
import config.Config;
import error.EM;

import java.io.*;

public class Compiler {
    // 关于测评的配置（主要是输出）
    // public static final boolean printLex = false;
//    public static final boolean printSyn = true;
//    public static Lexer lexer = Lexer.getLexer();
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./testfile.txt"))) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt"));
            BufferedWriter writerError = new BufferedWriter(new FileWriter("./error.txt"));
            writer.write("");
            writerError.write("");
            writer.close();
            writerError.close();
            writer = new BufferedWriter(new FileWriter("./output.txt", true));
            writerError = new BufferedWriter(new FileWriter("./error.txt", true));
            Lexer lexer = Lexer.getLexer();
            String line = "";
            for (int i = 0; (line = reader.readLine()) != null; i++) {
                lexer.initLexer();
                lexer.setSource(line);
                lexer.setPrintAble(Config.lexer);
                lexer.analyse();
            }

            Syner.getSyner().setTokens(lexer.getTokens());
            Syner.getSyner().analyze();
            if (Config.syner) {
                Syner.getSyner().SynerPrinter(writer);
            }

            EM.getEM().compUnitError(Syner.getSyner().getCompUnit());
            if (Config.error) {
                EM.getEM().printErrors(writerError);
                writerError.close();
            }
            if (EM.getEM().getErrors().size() > 0) {
                return;
            }

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
