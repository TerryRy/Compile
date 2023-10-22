package error;

import node.*;
import symbol.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

// 错误检测机
public class EM {
    private static EM em;
    private static List<Error> errors = new ArrayList<>(); // 错误信息表，需要排序后输出
    // 符号表，从右向左看，以函数为单位，函数内部以符号为单位
    private static List<SbTable<Map<String, Symbol>, Boolean, Integer>> symbolTable = new ArrayList<>();
    private static int loop = 0;
    static {
        em = new EM();
    }

    public static EM getEM(){
        return em;
    }

    public void addError(Error error){
        errors.add(error);
    }

    public List<Error> getErrors() {
        return errors;
    }

    public static void setErrors(List<Error> errors) {
        EM.errors = errors;
    }

    public static List<SbTable<Map<String, Symbol>, Boolean, Integer>> getSymbolTable() {
        return symbolTable;
    }

    public static void setSymbolTable(List<SbTable<Map<String, Symbol>, Boolean, Integer>> symbolTable) {
        EM.symbolTable = symbolTable;
    }

    public void printErrors(BufferedWriter writer) throws IOException {
        errors.sort(Error::compareTo);
        for (Error error : errors) {
            writer.write(error.toString());
            System.out.print(error.toString());
        }
    }

    // 指定string是否在当前层表内
    public boolean inCurBlock(String token) {
        return symbolTable.get(symbolTable.size() - 1).getSb().containsKey(token);
    }

    // 将指定符号填入当前层表
    public void putIn(String token, Symbol symbol) {
        symbolTable.get(symbolTable.size() - 1).getSb().put(token, symbol);
    }

    // 删除当前块表（最深层块）
    public void removeCurBolck() {
        symbolTable.remove(symbolTable.size() - 1);
    }

    // check if is in loop now
    public boolean inLoop() {
        return loop > 0;
    }

    // check if is in a function now, maybe go through many blocks
    public boolean inFunc() {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (symbolTable.get(i).getIsFunc()) {
                return true;
            }
        }
        return false;
    }

    // check the type of current function, if not in a function, return -1
    public int getInFunType() {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (symbolTable.get(i).getIsFunc()) {
                return symbolTable.get(i).getFuncType();
            }
        }
        return -1;
    }

    // get the newest intSymbol to use
    public IntSymbol getLastInt(String token) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (symbolTable.get(i).getSb().containsKey(token) && symbolTable.get(i).getSb().get(token) instanceof IntSymbol) {
                return (IntSymbol) symbolTable.get(i).getSb().get(token);
            }
        }
        return null;
    }

    // check if the last blockItem is a returnTK
    public boolean blockWithoutReturn(Block block) {
        return block.getBlockItemList() == null ||
                block.getBlockItemList().size() == 0 || // 空块
                block.getBlockItemList().get(block.getBlockItemList().size() - 1).getStmt() == null || // TODO:题干说只用考虑最后是不是
                block.getBlockItemList().get(block.getBlockItemList().size() - 1).getStmt().getReturnToken() == null;
    }

    // check if the ident tobe read is already defined
    public boolean containIdent(String token) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (symbolTable.get(i).getSb().containsKey(token)) {
                return true;
            }
        }
        return false;
    }

    // get last function in name of token (in fact there will not be two functions have the same name in symbol table)
    public FuncSymbol getFunc(String token) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (symbolTable.get(i).getSb().containsKey(token) && symbolTable.get(i).getSb().get(token) instanceof FuncSymbol) {
                return (FuncSymbol) symbolTable.get(i).getSb().get(token);
            }
        }
        return null;
    }

    public Symbol getSymbol(String token) {
        for (int i = symbolTable.size() - 1; i >= 0; i--) {
            if (symbolTable.get(i).getSb().containsKey(token)) {
                return symbolTable.get(i).getSb().get(token);
            }
        }
        return null;
    }

    public void compUnitError(CompUnit compUnit) {
        // CompUnit -> {Decl} {FuncDef} MainFuncDef
        // 顶级块，新建块表
        symbolTable.add(new SbTable<>(new HashMap<>(), false, null));
        for (Decl decl : compUnit.getDecls()) {
            declError(decl);
        }
        for (FuncDef funcDef : compUnit.getFuncDefs()) {
            funcDefError(funcDef);
        }
        mainFuncDefError(compUnit.getMainFuncDef());
    }

    public void declError(Decl decl) {
        // Decl → ConstDecl | VarDecl
        if (decl.getConstDecl() != null) {
            constDeclError(decl.getConstDecl());
        }
        else {
            varDeclError(decl.getVarDecl());
        }
    }

    public void constDeclError(ConstDecl constDecl) {
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
        for (ConstDef constDef : constDecl.getConstDefs()) {
            constDefError(constDef);
        }
    }

    public void constDefError(ConstDef constDef) {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
        // 声明变量，先查符号表
        if (inCurBlock(constDef.getIdent().getToken())) {
            EM.getEM().addError(new Error(constDef.getIdent().getLineNumber(), ErrorType.b));
            return;
        }
        if (constDef.getConstExps() != null) {
            for (ConstExp constExp : constDef.getConstExps()) {
                constExpError(constExp);
            }
        }
        // 声明结束（不用管初值），填符号表
        putIn(constDef.getIdent().getToken(), new IntSymbol(constDef.getIdent().getToken(), true, constDef.getConstExps().size()));
        constInitValError(constDef.getConstInitVal());
    }

    public void constInitValError(ConstInitVal constInitVal) {
        // ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        if (constInitVal.getConstExp() != null) {
            constExpError(constInitVal.getConstExp());
        }
        else {
            for (ConstInitVal aConstInitVal : constInitVal.getConstInitValList()) {
                constInitValError(aConstInitVal);
            }
        }
    }

    public void varDeclError(VarDecl varDecl) {
        // VarDecl → BType VarDef { ',' VarDef } ';'
        for (VarDef varDef : varDecl.getVarDefList()) {
            varDefError(varDef);
        }
    }

    public void varDefError(VarDef varDef) {
        // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
        // VarDef → Ident { '[' ConstExp ']' } [ '=' InitVal ]
        if (inCurBlock(varDef.getIdent().getToken())) {
            EM.getEM().addError(new Error(varDef.getIdent().getLineNumber(), ErrorType.b));
            return;
        }
        for (ConstExp constExp : varDef.getConstExpList()) {
            constExpError(constExp);
        }
        // 声明结束（不用管初值），填符号表
        putIn(varDef.getIdent().getToken(), new IntSymbol(varDef.getIdent().getToken(), false, varDef.getConstExpList().size()));
        if (varDef.getInitVal() != null) {
            initValError(varDef.getInitVal());
        }
    }

    public void initValError(InitVal initVal) {
        //  InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
        if (initVal.getExp() != null) {
            expError(initVal.getExp());
        }
        else {
            for (InitVal aInitVal : initVal.getInitValList()) {
                initValError(aInitVal);
            }
        }
    }

    public void funcDefError(FuncDef funcDef) {
        //  FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        /*  函数定义，先查符号表
        TODO：文法不存在函数内定义函数，因此无需查更多层
        * */
        if (inCurBlock(funcDef.getIdent().getToken())) {
            EM.getEM().addError(new Error(funcDef.getIdent().getLineNumber(), ErrorType.b));;
            return;
        }
        // TODO:这不会错，跳过
//        funcTypeError(funcDef.getFuncType());
        if (funcDef.getFuncFParams() == null) {
            // 声明分析结束，填入符号表
            putIn(funcDef.getIdent().getToken(), new FuncSymbol(funcDef.getIdent().getToken(), funcDef.getFuncType().getType(), new ArrayList<>())); // 不设null。便于后续直接遍历
        }
        else {
            // 读参数列表
            List<FuncParam> params = new ArrayList<>();
            for (FuncFParam funcFParam : funcDef.getFuncFParams().getFuncFParamList()) {
                params.add(new FuncParam(funcFParam.getIdent().getToken(), funcFParam.getLb().size()));
            }
            putIn(funcDef.getIdent().getToken(), new FuncSymbol(funcDef.getIdent().getToken(), funcDef.getFuncType().getType(), params));
        }
        // 函数添加完毕，建立该函数的块表
        symbolTable.add(new SbTable<>(new HashMap<>(), true, funcDef.getFuncType().getType()));

        if (funcDef.getFuncFParams() != null) {
            funcFParamsError(funcDef.getFuncFParams());
        }
        blockError(funcDef.getBlock(), true, funcDef.getFuncType().getType());

        // 函数返回，删除块表
        removeCurBolck();
    }

    public void mainFuncDefError(MainFuncDef mainFuncDef) {
        // MainFuncDef → 'int' 'main' '(' ')' Block
        // 同样是块首，且不会重、漏
        putIn("main", new FuncSymbol("main", 1, new ArrayList<>()));
        symbolTable.add(new SbTable<>(new HashMap<>(), true, 1));
        blockError(mainFuncDef.getBlock(), true, 1);
        removeCurBolck();
    }

    public void funcFParamsError(FuncFParams funcFParams) {
        // FuncFParams → FuncFParam { ',' FuncFParam }
        for (FuncFParam funcFParam : funcFParams.getFuncFParamList()) {
            funcFParamError(funcFParam);
        }
    }

    public void funcFParamError(FuncFParam funcFParam) {
        //  FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
        // 加入函数形参表
        if (inCurBlock(funcFParam.getIdent().getToken())) {
            EM.getEM().addError(new Error(funcFParam.getIdent().getLineNumber(), ErrorType.b));
        }
        putIn(funcFParam.getIdent().getToken(), new IntSymbol(funcFParam.getIdent().getToken(), false, funcFParam.getLb().size()));
    }

    public void blockError(Block block, boolean createdByFunc, int funcType) {
        // Block → '{' { BlockItem } '}'
//        symbolTable.add(new SbTable<>(new HashMap<>(), createdByFunc, funcType));
        for (BlockItem blockItem : block.getBlockItemList()) {
            blockItemError(blockItem);
        }
        if (createdByFunc) {
            if (getInFunType() == 1) {
                // int型函数
                if (blockWithoutReturn(block)) {
                    EM.getEM().addError(new Error(block.getRb().getLineNumber(), ErrorType.g));
                }
            }
        }
//        removeCurBolck();
    }

    public void blockItemError(BlockItem blockItem) {
        // BlockItem → Decl | Stmt
        if (blockItem.getDecl() != null) {
            declError(blockItem.getDecl());
        }
        else {
            stmtError(blockItem.getStmt());
        }
    }

    public void stmtError(Stmt stmt) {
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
        switch (stmt.getType()) {
            case LValExp: {
                // Stmt → LVal '=' Exp ';'
                lValError(stmt.getLVal());
                expError(stmt.getExp());
                if (getLastInt(stmt.getLVal().getIdent().getToken()) != null) {
                    if (getLastInt(stmt.getLVal().getIdent().getToken()).isConstInt()) {
                        EM.getEM().addError(new Error(stmt.getLVal().getIdent().getLineNumber(), ErrorType.h));
                    }
                }
                break;
            }
            case Exp: {
                // [Exp] ';'
                if (stmt.getExp() != null) {
                    expError(stmt.getExp());
                }
                break;
            }
            case Block: {
                // Block
                symbolTable.add(new SbTable<>(new HashMap<>(), false, -1));
                blockError(stmt.getBlock(), false, -1);
                removeCurBolck();
                break;
            }
            case If: {
                // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
                condError(stmt.getCond());
                stmtError(stmt.getStmtList().get(0));
                for (int i = 1; i < stmt.getStmtList().size(); i++) {
                    stmtError(stmt.getStmtList().get(i));
                }
                break;
            }
            case For: {
                // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
                if (stmt.getForStmt1() != null) {
                    forStmtError(stmt.getForStmt1());
                }
                if (stmt.getCond() != null) {
                    condError(stmt.getCond());
                }
                if (stmt.getForStmt2() != null) {
                    forStmtError(stmt.getForStmt2());
                }
                loop ++;
                for (Stmt aStmt : stmt.getStmtList()) {
                    stmtError(aStmt);
                }
                loop --;
                break;
            }
            case Break: {
                // 'break' ';'
            }
            case Continue: {
                // 'continue' ';'
                // 非循环出现break和continue
                if (!inLoop()) {
                    EM.getEM().addError(new Error(stmt.getBOrCToken().getLineNumber(), ErrorType.m));
                }
                break;
            }
            case Return: {
                // 'return' [Exp] ';'
                if (inFunc()) {
                    if (stmt.getExp() != null) {
                        // 仅有int和void两种返回类型，因此可直接使用是否有返回值来区分
                        if (getInFunType() == 0) {
                            EM.getEM().addError(new Error(stmt.getReturnToken().getLineNumber(), ErrorType.f));
                        }
                        expError(stmt.getExp());
                    }
                }
                // TODO:不会有return出现在函数外的错误
                break;
            }
            case LValGetint: {
                // LVal '=' 'getint''('')'';'
                lValError(stmt.getLVal());
                if (getLastInt(stmt.getLVal().getIdent().getToken()) != null) {
                    if (getLastInt(stmt.getLVal().getIdent().getToken()).isConstInt()) {
                        EM.getEM().addError(new Error(stmt.getLVal().getIdent().getLineNumber(), ErrorType.h));
                    }
                }
                break;
            }
            case Printf: {
                // 'printf''('FormatString{','Exp}')'';'
                int expNum = stmt.getExpList().size();
                int dNum = 0;
                String str = stmt.getFormatString().getToken();
                for (int i = 0; i < str.length() - 1; i++) {
                    // no need to consider the case of % as an escape character
                    if (str.charAt(i) == '%' && str.charAt(i + 1) == 'd') {
                        dNum++;
                    }
                }
                if (dNum != expNum) {
                    EM.getEM().addError(new Error(stmt.getPrintfToken().getLineNumber(), ErrorType.l));
                }
                for (Exp exp : stmt.getExpList()) {
                    expError(exp);
                }
                break;
            }
            default: {}
        }
    }

    public void forStmtError(ForStmt forStmt) {
        //  ForStmt → LVal '=' Exp
        lValError(forStmt.getLVal());
        // TODO 说起来似乎不存在这种错误
        if (getLastInt(forStmt.getLVal().getIdent().getToken()).isConstInt()) {
            EM.getEM().addError(new Error(forStmt.getLVal().getIdent().getLineNumber(), ErrorType.h));
        }
        expError(forStmt.getExp());
    }

    public void expError(Exp exp) {
        //  Exp → AddExp
        addExpError(exp.getAddExp());
    }

    public void condError(Cond cond) {
        // Cond → LOrExp
        lOrExpError(cond.getLOrExp());
    }

    public void lValError(LVal lVal) {
        // LVal → Ident {'[' Exp ']'}
        if (!containIdent(lVal.getIdent().getToken())) {
            EM.getEM().addError(new Error(lVal.getIdent().getLineNumber(), ErrorType.c));
        }
        if (lVal.getExpList() != null) {
            for (Exp exp : lVal.getExpList()) {
                expError(exp);
            }
        }
    }

    public void primaryExpError(PrimaryExp primaryExp) {
        //  PrimaryExp → '(' Exp ')' | LVal | Number
        if (primaryExp.getExp() != null) {
            expError(primaryExp.getExp());
        }
        else if (primaryExp.getLVal() != null) {
            lValError(primaryExp.getLVal());
        }
    }

    public void unaryExpError(UnaryExp unaryExp) {
        //  UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        if (unaryExp.getPrimaryExp() != null) {
            primaryExpError(unaryExp.getPrimaryExp());
        }
        else if (unaryExp.getUnaryExp() != null) {
            unaryExpError(unaryExp.getUnaryExp());
        }
        else {
            // 处理函数调用
            if (!containIdent(unaryExp.getIdent().getToken())) {
                EM.getEM().addError(new Error(unaryExp.getIdent().getLineNumber(), ErrorType.c));
                return;
            }
            FuncSymbol funcSymbol = getFunc(unaryExp.getIdent().getToken());
            // TODO:不存在此处调用Ident是变量不是函数的问题
            // TODO:也不存在调用时还没有声明任何一个函数的问题
            // 这里说明一下，unaryExp是调用除的实参，funcSymbol是声明处的
            if (unaryExp.getFuncRParams() == null) {
                // null 需要特殊处理
                if (funcSymbol.getFuncParamList().size() > 0) {
                    // 之前专门填入了空的ArrayList就是为了这里不用专门判断为空
                    EM.getEM().addError(new Error(unaryExp.getIdent().getLineNumber(), ErrorType.d));
                }
            }
            else {
                // 有参数的情况
                // 检查数量是否匹配
                if (funcSymbol.getFuncParamList().size() != unaryExp.getFuncRParams().getExpList().size()) {
                    EM.getEM().addError(new Error(unaryExp.getIdent().getLineNumber(), ErrorType.d));
                    return;
                }
                // 检查参数类型，数量相等可一一匹配
                List<Integer> fParams = new ArrayList<>();
                for (int i = 0; i < funcSymbol.getFuncParamList().size(); i++) {
                    fParams.add(funcSymbol.getFuncParamList().get(i).getDimension());
                }
                List<Integer> rParams = new ArrayList<>();
                if (unaryExp.getFuncRParams() != null) {
                    funcRParamsError(unaryExp.getFuncRParams());
                    for (Exp exp : unaryExp.getFuncRParams().getExpList()) {
                        FuncParam funcParam = getFuncParamFromExp(exp);
                        if (funcParam != null) {
                            if (funcParam.getName() == null) {
                                // "立即数"不必区分是不是函数
                                rParams.add(funcParam.getDimension()); // 应该是可能是0，保险起见还是按程序来
                            }
                            else {
                                // Ident,需要区分是不是函数
                                // 查符号表
                                Symbol symbol = getSymbol(funcParam.getName());
                                if (symbol instanceof IntSymbol) {
                                    // 变量，直接加入列表
                                    rParams.add(((IntSymbol) symbol).getDimension() - funcParam.getDimension());
                                }
                                else {
                                    // 函数，检查是否是VOID
                                    rParams.add(((FuncSymbol) symbol).getType() - 1); // 不存在返回数组
                                }
                            }
                        }
                    }
                }
                if (!Objects.equals(fParams, rParams)) {
                    EM.getEM().addError(new Error(unaryExp.getIdent().getLineNumber(), ErrorType.e));
                }
            }
        }
    }

    public FuncParam getFuncParamFromExp(Exp exp) {
        //  Exp → AddExp
        return getFuncParamFromAddExp(exp.getAddExp());
    }

    public FuncParam getFuncParamFromAddExp(AddExp addExp) {
        // AddExp → MulExp | AddExp ('+' | '−') MulExp
        // AddExp → MulExp{ ('+' | '−') MulExp }
        return getFuncParamFromMulExp(addExp.getMulExpList().get(0)); // 只关心类型，只需要第一个就好
    }

    public FuncParam getFuncParamFromMulExp(MulExp mulExp) {
        // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        // MulExp → UnaryExp { ('*' | '/' | '%') UnaryExp }
        return getFuncParamFromUnaryExp(mulExp.getUnaryExpList().get(0)); // 只关心类型，只需要第一个就好
    }

    public FuncParam getFuncParamFromUnaryExp(UnaryExp unaryExp) {
        //  UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        if (unaryExp.getPrimaryExp() != null) {
            return getFuncParamFromPrimaryExp(unaryExp.getPrimaryExp());
        }
        else if (unaryExp.getUnaryExp() != null) {
            return getFuncParamFromUnaryExp(unaryExp.getUnaryExp());
        }
        else {
            return getSymbol(unaryExp.getIdent().getToken()) instanceof FuncSymbol ? new FuncParam(unaryExp.getIdent().getToken(), 0) : null;
        }
    }

    public FuncParam getFuncParamFromPrimaryExp(PrimaryExp primaryExp) {
        //  PrimaryExp → '(' Exp ')' | LVal | Number
        if (primaryExp.getExp() != null) {
            return getFuncParamFromExp(primaryExp.getExp());
        }
        else if (primaryExp.getLVal() != null) {
            return getFuncParamFromLVal(primaryExp.getLVal());
        }
        else {
            return new FuncParam(null, 0);
        }
    }

    public FuncParam getFuncParamFromLVal(LVal lVal) {
        // LVal → Ident {'[' Exp ']'}
        return new FuncParam(lVal.getIdent().getToken(), lVal.getExpList().size());
    }

    public void funcRParamsError(FuncRParams funcRParams) {
        // FuncRParams → Exp { ',' Exp }
        for (Exp exp : funcRParams.getExpList()) {
            expError(exp);
        }
    }

    public void mulExpError(MulExp mulExp) {
        // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        // MulExp → UnaryExp { ('*' | '/' | '%') UnaryExp }
        for (UnaryExp unaryExp : mulExp.getUnaryExpList()) {
            unaryExpError(unaryExp);
        }
    }

    public void addExpError(AddExp addExp) {
        // AddExp → MulExp | AddExp ('+' | '−') MulExp
        // AddExp → MulExp{ ('+' | '−') MulExp }
        for (MulExp mulExp : addExp.getMulExpList()) {
            mulExpError(mulExp);
        }
    }

    public void relExpError(RelExp relExp) {
        // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
        // RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp }
        for (AddExp addExp : relExp.getAddExpList()) {
            addExpError(addExp);
        }
    }

    public void eqExpError(EqExp eqExp) {
        //  EqExp → RelExp | EqExp ('==' | '!=') RelExp
        //  EqExp → RelExp { ('==' | '!=') RelExp }
        for (RelExp relExp : eqExp.getRelExpList()) {
            relExpError(relExp);
        }
    }

    public void lAndError(LAndExp lAndExp) {
        // LAndExp → EqExp | LAndExp '&&' EqExp
        // LAndExp → EqExp { '&&' EqExp }
        for (EqExp eqExp : lAndExp.getEqExpList()) {
            eqExpError(eqExp);
        }
    }

    public void lOrExpError(LOrExp lOrExp) {
        //  LOrExp → LAndExp | LOrExp '||' LAndExp
        //  LOrExp → LAndExp { '||' LAndExp }
        for (LAndExp lAndExp : lOrExp.getLAndExpList()) {
            lAndError(lAndExp);
        }
    }

    public void constExpError(ConstExp constExp) {
        //  ConstExp → AddExp
        addExpError(constExp.getAddExp());
    }
}
