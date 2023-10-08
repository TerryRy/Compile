package node;

import java.io.BufferedWriter;
import java.io.IOException;

public interface Node {
    void print(BufferedWriter writer) throws IOException;
}
