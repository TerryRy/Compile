package node;

import java.io.BufferedWriter;
import java.io.IOException;

public class BlockItem implements Node {
    // BlockItem → Decl | Stmt
    private Decl decl;
    private Stmt stmt;

    public BlockItem(Decl decl, Stmt stmt) {
        this.decl = decl;
        this.stmt = stmt;
    }

    public Decl getDecl() {
        return decl;
    }

    public Stmt getStmt() {
        return stmt;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        if (decl != null) {
            decl.print(writer);
        }
        else {
            stmt.print(writer);
        }
        // do nothing
    }
}
