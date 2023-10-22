package node;

import Syntax.Syner;
import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class Stmt implements Node {
    /*
    Stmt → LVal '=' Exp ';'
    | [Exp] ';'
    | Block
    | 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    | 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    | 'break' ';'
    | 'continue' ';'
    | 'return' [Exp] ';'
    | LVal '=' 'getint''('')'';'
    | 'printf''('FormatString{','Exp}')'';'
     */

    public enum StmtType {
        LValExp, Exp, Block, If, For, Break, Continue, Return, LValGetint, Printf
    }

    private StmtType type;
    private LVal lVal;
    private Token assignToken;
    private Exp exp;
    private Token semicnToken;
    private Block block;
    private Token ifToken;
    private Token lb;
    private Cond cond;
    private Token rb;
    private List<Stmt> stmtList;
    private Token elseToken;
    private Token forToken;
    private ForStmt forStmt1;
    private ForStmt forStmt2;
    private List<Token> semicnTokenList;
    private Token bOrCToken;
    private Token returnToken;
    private Token getintToken;
    private Token printfToken;
    private Token formatString;
    private List<Exp> expList;
    private List<Token> commas;

    // Stmt → LVal '=' Exp ';'
    public Stmt(StmtType type, LVal lVal, Token assignToken, Exp exp, Token semicnToken) {
        this.type = type;
        this.lVal = lVal;
        this.assignToken = assignToken;
        this.exp = exp;
        this.semicnToken = semicnToken;
    }

    // Stmt → [Exp] ';'
    public Stmt(StmtType type, Exp exp, Token semicnToken) {
        this.type = type;
        this.exp = exp;
        this.semicnToken = semicnToken;
    }

    // Stmt → Block
    public Stmt(StmtType type, Block block) {
        this.type = type;
        this.block = block;
    }

    // Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    public Stmt(StmtType type, Token ifToken, Token lb, Cond cond, Token rb, List<Stmt> stmtList, Token elseToken) {
        this.type = type;
        this.ifToken = ifToken;
        this.lb = lb;
        this.cond = cond;
        this.rb = rb;
        this.stmtList = stmtList;
        this.elseToken = elseToken;
    }

    // Stmt → 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
    public Stmt(StmtType type, Token forToken, Token lb, ForStmt forStmt1, ForStmt forStmt2, List<Token> semicnTokenList, Cond cond, Token rb, List<Stmt> stmtList) {
        this.type = type;
        this.forToken = forToken;
        this.lb = lb;
        this.forStmt1 = forStmt1;
        this.forStmt2 = forStmt2;
        this.semicnTokenList = semicnTokenList;
        this.cond = cond;
        this.rb = rb;
        this.stmtList = stmtList;
    }

    // Stmt → 'break' ';' | 'continue' ';'
    public Stmt(StmtType type, Token bOrCToken, Token semicnToken) {
        this.type = type;
        this.bOrCToken = bOrCToken;
        this.semicnToken = semicnToken;
    }

    // Stmt → 'return' [Exp] ';'
    public Stmt(StmtType type, Token returnToken, Exp exp, Token semicnToken) {
        this.type = type;
        this.returnToken = returnToken;
        this.exp = exp;
        this.semicnToken = semicnToken;
    }

    // Stmt → LVal '=' 'getint''('')'';'
    public Stmt(StmtType type, LVal lVal, Token assignToken, Token getintToken, Token lb, Token rb, Token semicnToken) {
        this.type = type;
        this.lVal = lVal;
        this.assignToken = assignToken;
        this.getintToken = getintToken;
        this.lb = lb;
        this.rb = rb;
        this.semicnToken = semicnToken;
    }

    // Stmt → 'printf''('FormatString{','Exp}')'';'
    public Stmt(StmtType type, Token printfToken, Token lb, Token formatString, List<Token> commas, List<Exp> expList, Token rb, Token semicnToken) {
        this.type = type;
        this.printfToken = printfToken;
        this.lb = lb;
        this.formatString = formatString;
        this.commas = commas;
        this.expList = expList;
        this.rb = rb;
        this.semicnToken = semicnToken;
    }

    public StmtType getType() {
        return type;
    }

    public LVal getLVal() {
        return lVal;
    }

    public Exp getExp() {
        return exp;
    }

    public Block getBlock() {
        return block;
    }

    public Token getLb() {
        return lb;
    }

    public Cond getCond() {
        return cond;
    }

    public Token getRb() {
        return rb;
    }

    public List<Stmt> getStmtList() {
        return stmtList;
    }

    public ForStmt getForStmt1() {
        return forStmt1;
    }

    public ForStmt getForStmt2() {
        return forStmt2;
    }

    public Token getBOrCToken() {
        return bOrCToken;
    }

    public Token getReturnToken() {
        return returnToken;
    }

    public Token getPrintfToken() {
        return printfToken;
    }

    public Token getFormatString() {
        return formatString;
    }

    public List<Exp> getExpList() {
        return expList;
    }

    @Override
    public void print(BufferedWriter writer) throws IOException {
        switch (type) {
            case LValExp : {
                lVal.print(writer);
                writer.write(assignToken.toString());
                exp.print(writer);
                writer.write(semicnToken.toString());
                break;
            }
            case Exp : {
                if (exp != null) {
                    exp.print(writer);
                }
                writer.write(semicnToken.toString());
                break;
            }
            case Block : {
                block.print(writer);
                break;
            }
            case If : {
                writer.write(ifToken.toString());
                writer.write(lb.toString());
                cond.print(writer);
                writer.write(rb.toString());
                stmtList.get(0).print(writer);
                for (int i = 1; i < stmtList.size(); i++) {
                    writer.write(elseToken.toString());
                    stmtList.get(i).print(writer);
                }
                break;
            }
            case For : {
                writer.write(forToken.toString());
                writer.write(lb.toString());
                if (forStmt1 != null) {
                    forStmt1.print(writer);
                }
                writer.write(semicnTokenList.get(0).toString());
                if (cond != null) {
                    cond.print(writer);
                }
                writer.write(semicnTokenList.get(1).toString());
                if (forStmt2 != null) {
                    forStmt2.print(writer);
                }
                writer.write(rb.toString());
                stmtList.get(0).print(writer);
                break;
            }
            case Break : {
            }
            case Continue : {
                writer.write(bOrCToken.toString());
                writer.write(semicnToken.toString());
                break;
            }
            case Return : {
                writer.write(returnToken.toString());
                if (exp != null) {
                    exp.print(writer);
                }
                writer.write(semicnToken.toString());
                break;
            }
            case LValGetint : {
                lVal.print(writer);
                writer.write(assignToken.toString());
                writer.write(getintToken.toString());
                writer.write(lb.toString());
                writer.write(rb.toString());
                writer.write(semicnToken.toString());
                break;
            }
            case Printf : {
                writer.write(printfToken.toString());
                writer.write(lb.toString());
                writer.write(formatString.toString());
                for (int i = 0; i < commas.size(); i++) {
                    writer.write(commas.get(i).toString());
                    expList.get(i).print(writer);
                }
                writer.write(rb.toString());
                writer.write(semicnToken.toString());
                break;
            }
        }
        writer.write(Syner.nodeType.get(NodeType.Stmt));
    }
}
