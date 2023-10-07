package node;

import java.io.BufferedWriter;
import java.io.IOException;

public interface Node {
    public void print(BufferedWriter writer) throws IOException;
}
