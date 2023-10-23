import Lexical.Lexer;
import Syntax.Syner;
import config.Config;
import error.EM;

import java.io.*;

public class Compiler {
    // 关于测评的配置（主要是输出）
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./testfile.txt"))) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt"));
            writer.write("");
            writer.close();
            writer = new BufferedWriter(new FileWriter("./output.txt", true));
            Lexer lexer = Lexer.getLexer();
            String line;
            while ((line = reader.readLine()) != null) {
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
                BufferedWriter writerError = new BufferedWriter(new FileWriter("./error.txt"));
                writerError.write("");
                writerError.close();
                writerError = new BufferedWriter(new FileWriter("./error.txt", true));
                EM.getEM().printErrors(writerError);
                writerError.close();
                if (EM.getEM().getErrors().size() > 0) {
                    return;
                }
            }

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
