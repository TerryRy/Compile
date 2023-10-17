package Syntax;

import Lexical.LexType;
import Token.Token;
import node.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Syner {
    private static Syner syner;
    private List<Token> tokens = new ArrayList<>();
    private int index = 0; // 在tokens中的指针
    private Token now;
    private CompUnit compUnit;

    public static Map<NodeType, String> nodeType = new HashMap<>() {{
        put(NodeType.CompUnit, "<CompUnit>\n");
        put(NodeType.Decl, "<Decl>\n");
        put(NodeType.ConstDecl, "<ConstDecl>\n");
        put(NodeType.BType, "<BType>\n");
        put(NodeType.ConstDef, "<ConstDef>\n");
        put(NodeType.ConstInitVal, "<ConstInitVal>\n");
        put(NodeType.VarDecl, "<VarDecl>\n");
        put(NodeType.VarDef, "<VarDef>\n");
        put(NodeType.InitVal, "<InitVal>\n");
        put(NodeType.FuncDef, "<FuncDef>\n");
        put(NodeType.MainFuncDef, "<MainFuncDef>\n");
        put(NodeType.FuncType, "<FuncType>\n");
        put(NodeType.FuncFParams, "<FuncFParams>\n");
        put(NodeType.FuncFParam, "<FuncFParam>\n");
        put(NodeType.Block, "<Block>\n");
        put(NodeType.BlockItem, "<BlockItem>\n");
        put(NodeType.Stmt, "<Stmt>\n");
        put(NodeType.ForStmt, "<ForStmt>\n");
        put(NodeType.Exp, "<Exp>\n");
        put(NodeType.Cond, "<Cond>\n");
        put(NodeType.LVal, "<LVal>\n");
        put(NodeType.PrimaryExp, "<PrimaryExp>\n");
        put(NodeType.Number, "<Number>\n");
        put(NodeType.UnaryExp, "<UnaryExp>\n");
        put(NodeType.UnaryOp, "<UnaryOp>\n");
        put(NodeType.FuncRParams, "<FuncRParams>\n");
        put(NodeType.MulExp, "<MulExp>\n");
        put(NodeType.AddExp, "<AddExp>\n");
        put(NodeType.RelExp, "<RelExp>\n");
        put(NodeType.EqExp, "<EqExp>\n");
        put(NodeType.LAndExp, "<LAndExp>\n");
        put(NodeType.LOrExp, "<LOrExp>\n");
        put(NodeType.ConstExp, "<ConstExp>\n");
    }};

    static {
        syner = new Syner();
    }

    public static Syner getSyner() {
        return syner;
    }

    // 封装后的打印函数
    public void SynerPrinter(BufferedWriter writer) throws IOException {
        compUnit.print(writer);
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
        setNow(this.tokens.get(index));
    }

    public void setNow(Token now) {
        this.now = now;
    }

    public CompUnit getCompUnit() {
        return compUnit;
    }

    public void analyze() {
        this.compUnit = aCompUnit();
    }

    private Token match(LexType tokenType) {
        if (now.getType() == tokenType) {
            Token tmpNow = now;
            if (index < tokens.size() - 1) {
                now = tokens.get(++index);
            }
            return tmpNow;
        } else if (tokenType == LexType.SEMICN) {
            ErrorHandler.getInstance().addError(new Error(tokens.get(index - 1).getLineNumber(), ErrorType.i));
            return new Token(TokenType.SEMICN, tokens.get(index - 1).getLineNumber(), ";");
        } else if (tokenType == LexType.RPARENT) {
            ErrorHandler.getInstance().addError(new Error(tokens.get(index - 1).getLineNumber(), ErrorType.j));
            return new Token(TokenType.RPARENT, tokens.get(index - 1).getLineNumber(), ")");
        } else if (tokenType == LexType.RBRACK) {
            ErrorHandler.getInstance().addError(new Error(tokens.get(index - 1).getLineNumber(), ErrorType.k));
            return new Token(TokenType.RBRACK, tokens.get(index - 1).getLineNumber(), "]");
        } else {
            throw new RuntimeException("Syntax error at line " + now.getLineNumber() + ": " + now.getToken() + " is not " + tokenType);
        }
    }

    private boolean isExp() {
        return now.getType() == LexType.INTCON ||
                now.getType() == LexType.IDENFR ||
                now.getType() == LexType.NOT ||
                now.getType() == LexType.PLUS ||
                now.getType() == LexType.MINU ||
                now.getType() == LexType.LPARENT;
    }

    private boolean isForStmt() {
        return now.getType() == LexType.IDENFR;
    }

    private boolean isCond() {
        return isExp();
    }

    private CompUnit aCompUnit() {
        // CompUnit -> {Decl} {FuncDef} MainFuncDef
        List<Decl> declList = new ArrayList<>();
        List<FuncDef> funcDefList = new ArrayList<>();
        MainFuncDef mainFuncDef;
        while (tokens.get(index + 1).getType() != LexType.MAINTK && tokens.get(index + 2).getType() != LexType.LPARENT) {
            Decl decl = aDecl();
            declList.add(decl);
        }
        while (tokens.get(index + 1).getType() != LexType.MAINTK) {
            FuncDef funcDef = aFuncDef();
            funcDefList.add(funcDef);
        }
        mainFuncDef = aMainFuncDef();
        return new CompUnit(declList, funcDefList, mainFuncDef);
    }

    private Decl aDecl() {
        // Decl → ConstDecl | VarDecl
        ConstDecl constDecl = null;
        VarDecl varDecl = null;
        if (now.getType() == LexType.CONSTTK) {
            constDecl = aConstDecl();
        }
        else {
            varDecl = aVarDecl();
        }
        return new Decl(constDecl, varDecl);
    }

    private ConstDecl aConstDecl() {
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
        Token constToken = match(LexType.CONSTTK);
        BType bType = aBType();
        List<ConstDef> constDefList = new ArrayList<>();
        List<Token> commas = new ArrayList<>();
        Token semicnToken;
        constDefList.add(aConstDef());
        while (now.getType() == LexType.COMMA) {
            commas.add(match(LexType.COMMA));
            constDefList.add(aConstDef());
        }
        semicnToken = match(LexType.SEMICN);
        return new ConstDecl(constToken, bType, constDefList, commas, semicnToken);
    }

    private BType aBType() {
        // BType → 'int'
        Token intToken = match(LexType.INTTK);
        return new BType(intToken);
    }

    private ConstDef aConstDef() {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
        Token identToken = match(LexType.IDENFR);
        List<Token> lb = new ArrayList<>();
        List<ConstExp> constExpList = new ArrayList<>();
        List<Token> rb = new ArrayList<>();
        Token assignToken;
        ConstInitVal constInitVal;
        while (now.getType() == LexType.LBRACK) {
            lb.add(match(LexType.LBRACK));
            constExpList.add(aConstExp());
            rb.add(match(LexType.RBRACK));
        }
        assignToken = match(LexType.ASSIGN);
        constInitVal = aConstInitVal();
        return new ConstDef(identToken, lb, constExpList, rb, assignToken, constInitVal);
    }

    private ConstInitVal aConstInitVal() {
        // ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        ConstExp constExp = null;
        Token lb = null;
        List<ConstInitVal> constInitValList = new ArrayList<>();
        List<Token> commas = new ArrayList<>();
        Token rb = null;
        if (now.getType() != LexType.LBRACE) {
            constExp = aConstExp();
        }
        else {
            lb = match(LexType.LBRACE);
            if (now.getType() != LexType.RBRACE) {
                constInitValList.add(aConstInitVal());
                while (now.getType() == LexType.COMMA) {
                    commas.add(match(LexType.COMMA));
                    constInitValList.add(aConstInitVal());
                }
            }
            rb = match(LexType.RBRACE);
        }
        return new ConstInitVal(constExp, lb, constInitValList, commas, rb);
    }

    private VarDecl aVarDecl() {
        // VarDecl → BType VarDef { ',' VarDef } ';'
        BType bType = aBType();
        List<VarDef> varDefList = new ArrayList<>();
        List<Token> commas = new ArrayList<>();
        Token semicnToken;
        varDefList.add(aVarDef());
        while (now.getType() == LexType.COMMA) {
            commas.add(match(LexType.COMMA));
            varDefList.add(aVarDef());
        }
        semicnToken = match(LexType.SEMICN);
        return new VarDecl(bType, varDefList, commas, semicnToken);
    }

    private VarDef aVarDef() {
        // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
        // VarDef → Ident { '[' ConstExp ']' } [ '=' InitVal ]
        Token ident = match(LexType.IDENFR);
        List<Token> lb = new ArrayList<>();
        List<ConstExp> constExpList = new ArrayList<>();
        List<Token> rb = new ArrayList<>();
        Token assignToken = null;
        InitVal initVal = null;

        while (now.getType() == LexType.LBRACK) {
            lb.add(match(LexType.LBRACK));
            constExpList.add(aConstExp());
            rb.add(match(LexType.RBRACK));
        }
        if (now.getType() == LexType.ASSIGN) {
            assignToken = match(LexType.ASSIGN);
            initVal = aInitVal();
        }
        return new VarDef(ident, lb, constExpList, rb, assignToken, initVal);
    }

    private InitVal aInitVal() {
        //  InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
        Exp exp = null;
        Token lb = null;
        List<InitVal> initValList = new ArrayList<>();
        List<Token> commas = new ArrayList<>();
        Token rb = null;

        if (now.getType() != LexType.LBRACE) {
            exp = aExp();
        }
        else {
            lb = match(LexType.LBRACE);
            if (now.getType() != LexType.RBRACE) {
                initValList.add(aInitVal());
                while (now.getType() == LexType.COMMA) {
                    commas.add(match(LexType.COMMA));
                    initValList.add(aInitVal());
                }
            }
            rb = match(LexType.RBRACE);
        }
        return new InitVal(exp, lb, initValList, commas, rb);
    }

    private FuncDef aFuncDef() {
        //  FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        FuncType funcType = aFuncType();
        Token ident = match(LexType.IDENFR);
        Token lb = match(LexType.LPARENT);
        FuncFParams funcFParams = null;
        Token rb;
        Block block;

        if (now.getType() == LexType.INTTK) {
            funcFParams = aFuncFParams();
        }
        rb = match(LexType.RPARENT);
        block = aBlock();
        return new FuncDef(funcType, ident, lb, funcFParams, rb, block);
    }

    private MainFuncDef aMainFuncDef() {
        // MainFuncDef → 'int' 'main' '(' ')' Block
        Token intToken = match(LexType.INTTK);
        Token mainToken = match(LexType.MAINTK);
        Token lb = match(LexType.LPARENT);
        Token rb = match(LexType.RPARENT);
        Block block = aBlock();

        return new MainFuncDef(intToken, mainToken, lb, rb, block);
    }

    private FuncType aFuncType() {
        // FuncType → 'void' | 'int'
        Token voidToken = null;
        Token intToken = null;

        if (now.getType() == LexType.VOIDTK) {
            voidToken = match(LexType.VOIDTK);
        }
        else {
            intToken = match(LexType.INTTK);
        }
        return new FuncType(voidToken, intToken);
    }

    private FuncFParams aFuncFParams() {
        // FuncFParams → FuncFParam { ',' FuncFParam }
        List<FuncFParam> funcFParamList = new ArrayList<>();
        List<Token> commas = new ArrayList<>();

        funcFParamList.add(aFuncFParam());
        while (now.getType() == LexType.COMMA) {
            commas.add(match(LexType.COMMA));
            funcFParamList.add(aFuncFParam());
        }
        return new FuncFParams(funcFParamList, commas);
    }

    private FuncFParam aFuncFParam() {
        //  FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
        BType bType = aBType();
        Token ident = match(LexType.IDENFR);
        List<Token> lb = new ArrayList<>();
        List<Token> rb = new ArrayList<>();
        List<ConstExp> constExpList = new ArrayList<>();

        if (now.getType() == LexType.LBRACK) {
            lb.add(match(LexType.LBRACK));
            rb.add(match(LexType.RBRACK));
            while (now.getType() == LexType.LBRACK) {
                lb.add(match(LexType.LBRACK));
                constExpList.add(aConstExp());
                rb.add(match(LexType.RBRACK));
            }
        }
        return new FuncFParam(bType, ident, lb, rb, constExpList);
    }

    private Block aBlock() {
        // Block → '{' { BlockItem } '}'
        Token lb = match(LexType.LBRACE);
        List<BlockItem> blockItemList = new ArrayList<>();
        Token rb;

        while (now.getType() != LexType.RBRACE) {
            blockItemList.add(aBlockItem());
        }
        rb = match(LexType.RBRACE);
        return new Block(lb, blockItemList, rb);
    }

    private BlockItem aBlockItem() {
        // BlockItem → Decl | Stmt
        Decl decl = null;
        Stmt stmt = null;

        if (now.getType() == LexType.CONSTTK || now.getType() == LexType.INTTK) {
            decl = aDecl();
        }
        else {
            stmt = aStmt();
        }
        return new BlockItem(decl, stmt);
    }

    private Stmt aStmt() {
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
        if (now.getType() == LexType.LBRACE) {
            // Block
            Block block = aBlock();
            return new Stmt(Stmt.StmtType.Block, block);
        }
        else if (now.getType() == LexType.IFTK) {
            // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            Token ifToken = match(LexType.IFTK);
            Token lb = match(LexType.LPARENT);
            Cond cond = aCond();
            Token rb = match(LexType.RPARENT);
            List<Stmt> stmtList = new ArrayList<>();
            stmtList.add(aStmt());
            Token elseToken = null;
            if (now.getType() == LexType.ELSETK) {
                elseToken = match(LexType.ELSETK);
                stmtList.add(aStmt());
            }
            return new Stmt(Stmt.StmtType.If, ifToken, lb, cond, rb, stmtList, elseToken);
        }
        else if (now.getType() == LexType.FORTK) {
            // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
            Token forToken = match(LexType.FORTK);
            Token lb = match(LexType.LPARENT);
            ForStmt forStmt1 = null;
            List<Token> semicnList = new ArrayList<>();
            Cond cond = null;
            ForStmt forStmt2 = null;
            Token rb;
            List<Stmt> stmtList = new ArrayList<>();
            if (isForStmt()) {
                forStmt1 = aForStmt();
            }
            semicnList.add(match(LexType.SEMICN));
            if (isCond()) {
                cond = aCond();
            }
            semicnList.add(match(LexType.SEMICN));
            if (isForStmt()) {
                forStmt2 = aForStmt();
            }
            rb = match(LexType.RPARENT);
            stmtList.add(aStmt());
            return new Stmt(Stmt.StmtType.For, forToken, lb, forStmt1, forStmt2, semicnList, cond, rb, stmtList);
        }
        else if (now.getType() == LexType.BREAKTK) {
            // break' ';'
            Token breakToken = match(LexType.BREAKTK);
            Token semicn = match(LexType.SEMICN);
            return new Stmt(Stmt.StmtType.Break, breakToken, semicn);
        }
        else if (now.getType() == LexType.CONTINUETK) {
            // 'continue' ';'
            Token continueToken = match(LexType.CONTINUETK);
            Token semicn = match(LexType.SEMICN);
            return new Stmt(Stmt.StmtType.Continue, continueToken, semicn);
        }
        else if (now.getType() == LexType.RETURNTK) {
            // 'return' [Exp] ';'
            Token returnToken = match(LexType.RETURNTK);
            Exp exp = null;
            Token semicn;

            if (isExp()) {
                exp = aExp();
            }
            semicn = match(LexType.SEMICN);
            return new Stmt(Stmt.StmtType.Return, returnToken, exp, semicn);
        }
        else if (now.getType() == LexType.PRINTFTK) {
            // 'printf''('FormatString{','Exp}')'';'
            Token printfToken = match(LexType.PRINTFTK);
            Token lb = match(LexType.LPARENT);
            Token formatString = match(LexType.STRCON);
            List<Token> commas = new ArrayList<>();
            List<Exp> expList = new ArrayList<>();
            Token rb;
            Token semicn;

            while (now.getType() == LexType.COMMA) {
                commas.add(match(LexType.COMMA));
                expList.add(aExp());
            }
            rb = match(LexType.RPARENT);
            semicn = match(LexType.SEMICN);
            return new Stmt(Stmt.StmtType.Printf, printfToken, lb, formatString, commas, expList, rb, semicn);
        }
        else {
            int assign = index;
            for (int i = index; i < tokens.size() && tokens.get(i).getLineNumber() == now.getLineNumber(); i++) {
                if (tokens.get(i).getType() == LexType.ASSIGN) {
                    assign = i;
                    break;
                }
            }
            if (assign > index) {
                // LVal '=' Exp ';'
                // LVal '=' 'getint '(' ')' ';'
                LVal lVal = aLVal();
                Token assignToken = match(LexType.ASSIGN);
                if (now.getType() != LexType.GETINTTK) {
                    Exp exp = aExp();
                    Token semicn = match(LexType.SEMICN);
                    return new Stmt(Stmt.StmtType.LValExp, lVal, assignToken, exp, semicn);
                }
                else {
                    Token getintToken = match(LexType.GETINTTK);
                    Token lb = match(LexType.LPARENT);
                    Token rb = match(LexType.RPARENT);
                    Token semicnToken = match(LexType.SEMICN);
                    return new Stmt(Stmt.StmtType.LValGetint, lVal, assignToken, getintToken, lb, rb, semicnToken);
                }
            }
            else {
                // [Exp] ';'
                Exp exp = null;
                Token semicnToken;

                if (isExp()) {
                    exp = aExp();
                }
                semicnToken = match(LexType.SEMICN);
                return new Stmt(Stmt.StmtType.Exp, exp, semicnToken);
            }
        }
    }

    private ForStmt aForStmt() {
        //  ForStmt → LVal '=' Exp
        LVal lVal = aLVal();
        Token assignToken = match(LexType.ASSIGN);
        Exp exp = aExp();
        return new ForStmt(lVal, assignToken, exp);
    }

    private Exp aExp() {
        //  Exp → AddExp
        AddExp addExp = aAddExp();
        return new Exp(addExp);
    }

    private Cond aCond() {
        // Cond → LOrExp
        LOrExp lOrExp = aLOrExp();
        return new Cond(lOrExp);
    }

    private LVal aLVal() {
        // LVal → Ident {'[' Exp ']'}
        Token identToken = match(LexType.IDENFR);
        List<Token> lb = new ArrayList<>();
        List<Exp> expList = new ArrayList<>();
        List<Token> rb = new ArrayList<>();

        while (now.getType() == LexType.LBRACK) {
            lb.add(match(LexType.LBRACK));
            expList.add(aExp());
            rb.add(match(LexType.RBRACK));
        }
        return new LVal(identToken, lb, expList, rb);
    }

    private PrimaryExp aPrimaryExp() {
        //  PrimaryExp → '(' Exp ')' | LVal | Number
        Token lb = null;
        Exp exp = null;
        Token rb = null;
        LVal lVal = null;
        NumberNode numberNode = null;

        if (now.getType() == LexType.LPARENT) {
            // '(' Exp ')'
            lb = match(LexType.LPARENT);
            exp = aExp();
            rb = match(LexType.RPARENT);
        }
        else if (now.getType() != LexType.INTCON) {
            // LVal
            lVal = aLVal();
        }
        else {
            // Number
            numberNode = aNumberNode();
        }
        return new PrimaryExp(lb, exp, rb, lVal,numberNode);
    }

    private NumberNode aNumberNode() {
        //  Number → IntConst
        Token intConstToken = match(LexType.INTCON);
        return new NumberNode(intConstToken);
    }

    private UnaryExp aUnaryExp() {
        //  UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        PrimaryExp primaryExp = null;
        Token identToken = null;
        Token lb = null;
        FuncRParams funcRParams = null;
        Token rb = null;
        UnaryOp unaryOp = null;
        UnaryExp unaryExp = null;

        if (now.getType() == LexType.IDENFR && tokens.get(index + 1).getType() == LexType.LPARENT) {
            // Ident '(' [FuncRParams] ')'
            identToken = match(LexType.IDENFR);
            lb = match(LexType.LPARENT);
            if (isExp()) {
                funcRParams = aFuncRParams();
            }
            rb = match(LexType.RPARENT);
        }
        else if (now.getType() == LexType.PLUS || now.getType() == LexType.MINU || now.getType() == LexType.NOT) {
            // UnaryOp UnaryExp
            unaryOp = aUnaryOp();
            unaryExp = aUnaryExp();
        }
        else {
            // PrimaryExp
            primaryExp = aPrimaryExp();
        }
        return new UnaryExp(primaryExp, identToken, lb, funcRParams, rb, unaryOp, unaryExp);
    }

    private UnaryOp aUnaryOp() {
        //  UnaryOp → '+' | '−' | '!'
        Token opToken;

        if (now.getType() == LexType.PLUS) {
            opToken = match(LexType.PLUS);
        }
        else if (now.getType() == LexType.MINU) {
            opToken = match(LexType.MINU);
        }
        else {
            opToken = match(LexType.NOT);
        }
        return new UnaryOp(opToken);
    }

    private FuncRParams aFuncRParams() {
        // FuncRParams → Exp { ',' Exp }
        List<Exp> expList = new ArrayList<>();
        List<Token> commas = new ArrayList<>();

        expList.add(aExp());
        while (now.getType() == LexType.COMMA) {
            commas.add(match(LexType.COMMA));
            expList.add(aExp());
        }
        return new FuncRParams(expList, commas);
    }

    private MulExp aMulExp() {
        // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        // MulExp → UnaryExp { ('*' | '/' | '%') UnaryExp }
        List<UnaryExp> unaryExpList = new ArrayList<>();
        List<Token> opList = new ArrayList<>();

        unaryExpList.add(aUnaryExp());
        while (now.getType() == LexType.MULT || now.getType() == LexType.DIV || now.getType() == LexType.MOD) {
            if (now.getType() == LexType.MULT) {
                opList.add(match(LexType.MULT));
            }
            else if (now.getType() == LexType.DIV) {
                opList.add(match(LexType.DIV));
            }
            else {
                opList.add(match(LexType.MOD));
            }
            unaryExpList.add(aUnaryExp());
        }
        return new MulExp(unaryExpList, opList);
    }

    private AddExp aAddExp() {
        // AddExp → MulExp | AddExp ('+' | '−') MulExp
        // AddExp → MulExp{ ('+' | '−') MulExp }
        List<MulExp> mulExpList = new ArrayList<>();
        List<Token> opList = new ArrayList<>();

        mulExpList.add(aMulExp());
        while (now.getType() == LexType.PLUS || now.getType() == LexType.MINU) {
            if (now.getType() == LexType.PLUS) {
                opList.add(match(LexType.PLUS));
            }
            else {
                opList.add(match(LexType.MINU));
            }
            mulExpList.add(aMulExp());
        }
        return new AddExp(mulExpList, opList);
    }

    private RelExp aRelExp() {
        // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
        // RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp }
        List<AddExp> addExpList = new ArrayList<>();
        List<Token> opList = new ArrayList<>();

        addExpList.add(aAddExp());
        while (now.getType() == LexType.LSS
                || now.getType() == LexType.LEQ
                || now.getType() == LexType.GRE
                || now.getType() == LexType.GEQ
        ) {
            if (now.getType() == LexType.LSS) {
                opList.add(match(LexType.LSS));
            }
            else if (now.getType() == LexType.LEQ) {
                opList.add(match(LexType.LEQ));
            }
            else if (now.getType() == LexType.GRE) {
                opList.add(match(LexType.GRE));
            }
            else {
                opList.add(match(LexType.GEQ));
            }
            addExpList.add(aAddExp());
        }
        return new RelExp(addExpList, opList);
    }

    private EqExp aEqExp() {
        //  EqExp → RelExp | EqExp ('==' | '!=') RelExp
        //  EqExp → RelExp { ('==' | '!=') RelExp }
        List<RelExp> relExpList = new ArrayList<>();
        List<Token> opList = new ArrayList<>();

        relExpList.add(aRelExp());
        while (now.getType() == LexType.EQL || now.getType() == LexType.NEQ) {
            if (now.getType() == LexType.EQL) {
                opList.add(match(LexType.EQL));
            }
            else {
                opList.add(match(LexType.NEQ));
            }
            relExpList.add(aRelExp());
        }
        return new EqExp(relExpList, opList);
    }

    private LAndExp aLAndExp() {
        // LAndExp → EqExp | LAndExp '&&' EqExp
        // LAndExp → EqExp { '&&' EqExp }
        List<EqExp> eqExpList = new ArrayList<>();
        List<Token> opList = new ArrayList<>();

        eqExpList.add(aEqExp());
        while (now.getType() == LexType.AND) {
            opList.add(match(LexType.AND));
            eqExpList.add(aEqExp());
        }
        return new LAndExp(eqExpList, opList);
    }

    private LOrExp aLOrExp() {
        //  LOrExp → LAndExp | LOrExp '||' LAndExp
        //  LOrExp → LAndExp { '||' LAndExp }
        List<LAndExp> lAndExpList = new ArrayList<>();
        List<Token> opList = new ArrayList<>();

        lAndExpList.add(aLAndExp());
        while (now.getType() == LexType.OR) {
            opList.add(match(LexType.OR));
            lAndExpList.add(aLAndExp());
        }
        return new LOrExp(lAndExpList, opList);
    }

    private ConstExp aConstExp() {
        //  ConstExp → AddExp
        AddExp aAddExp = aAddExp();
        return new ConstExp(aAddExp);
    }
}
