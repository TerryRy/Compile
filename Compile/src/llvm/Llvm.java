package llvm;

import Lexical.LexType;
import Token.Token;
import Type.LineFuncType;
import Type.IntegerType;
import Type.LineArrayType;
import Type.Type;
import Type.FuncVoidType;
import llvm.Container.Pair;
import llvm.Container.Triple;
import llvm.TreeTable.Line.*;
import llvm.TreeTable.Line.LineFuncFParam;
import llvm.TreeTable.TableNode;
import node.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Llvm {
    private static Llvm llvm;

    static {
        llvm = new Llvm();
    }

    public static Llvm getLlvm() {
        return llvm;
    }

    public void llvmPrinter(BufferedWriter writerLlvm) throws IOException {
        for (String str : llvm_ir) {
            writerLlvm.write(str);
        }
    }

    /*
    * 工具变量
     */
    // 待打印llvm代码
    private static List<String> llvm_ir = new ArrayList<>();

    // 树状符号表，码进来而已树结构在节点中
    private static List<TableNode> treeTable = new ArrayList<>();
    // 不使用树容器，使用标号跳转更方便，同时下标可直接作为基本块的编号

    // block编号计数器从0开始，注意任何时间指向直接可用的空值，用来建表
    private static int blockCounter = 0;

    // 当前block编号，初始为0，之后指向当前所在block，用于建表
    private static int curBbNum = 0;

//    // 下一个基本块的编号，其实我感觉只有跳转指令能用到，函数定义都用不到
//    private int baseBlockCounter = 0;

    // 时代变了，每个基本块和每个临时寄存器一个编号，只好全局一个计数器跑到底了
    private int baseBlockCounter = 0;

    /*
    * 工具函数
     */
    // 添加汇编码
    public void addLlvm(String str) {
        llvm_ir.add(str);
    }

    // 获取根节点
    public TableNode getRootNode() {
        return treeTable.get(0);
    }

    // 获取当前节点
    public TableNode getCurNode() {
        return treeTable.get(curBbNum);
    }

    // 向外查找符号，可以不断访问Node的parentNode
    public Ident findLVal(String name) {
        TableNode curNode = getCurNode();
        while (curNode.getParentNode() != -1) {
            for (int i = curNode.getLines().size() - 1; i >= 0; i--) {
                if (curNode.getLines().get(i) instanceof LineIdent) {
                    if (curNode.getLines().get(i).getName().equals(name)) {
                        return (Ident) curNode.getLines().get(i);
                    }
                }
            }
            curNode = treeTable.get(curNode.getParentNode());
        }
        if (curNode.getParentNode() == -1) {
            for (int i = curNode.getLines().size() - 1; i >= 0; i--) {
                if (curNode.getLines().get(i) instanceof LineIdent) {
                    if (curNode.getLines().get(i).getName().equals(name)) {
                        return (Ident) curNode.getLines().get(i);
                    }
                }
            }
        }
        return null;
    }

    // 在符号表 中查函数，没有直接在0号标中查是想要应对局部函数（不过这玩意应该不会有吧
    public LineFunc findFunc(String name) {
        TableNode curNode;
        int nodeNum = curBbNum;
        do {
            curNode = treeTable.get(nodeNum);
            for (int i = curNode.getLines().size() - 1; i >= 0; i--) {
                TreeTableLine line = curNode.getLines().get(i);
                if (line instanceof LineFunc) {
                    if (line.getName().equals(name)) {
                        return (LineFunc) line;
                    }
                }
            }
            nodeNum = curNode.getParentNode();
        } while (nodeNum != -1);
        return null;
    }

    // 向外查找符号，找到则需改掉其 值
    public Ident findAndChangeLVal(String name, String value) {
        TableNode curNode = getCurNode();
        while (curNode.getParentNode() != -1) {
            for (int i = curNode.getLines().size() - 1; i >= 0; i--) {
                if (curNode.getLines().get(i) instanceof LineIdent) {
                    if (curNode.getLines().get(i).getName().equals(name)) {
                        ((Ident) curNode.getLines().get(i)).setValue(value);
                        return (Ident) curNode.getLines().get(i);
                    }
                }
            }
            curNode = treeTable.get(curNode.getParentNode());
        }
        if (curNode.getParentNode() == -1) {
            for (int i = curNode.getLines().size() - 1; i >= 0; i--) {
                if (curNode.getLines().get(i) instanceof LineIdent) {
                    if (curNode.getLines().get(i).getName().equals(name)) {
                        ((Ident) curNode.getLines().get(i)).setValue(value);
                        return (Ident) curNode.getLines().get(i);
                    }
                }
            }
        }
        return null;
    }

    // 添加Node
    public void addTreeTable(int parentNode) {
        int tNum = 0;
        if (blockCounter != 0) {
//            tNum = getCurNode().getCurTCounter();
            tNum = baseBlockCounter;
        }
        treeTable.add(new TableNode(parentNode, tNum));
        blockCounter++;
    }

    // 建立并进入非根节点
    public void addSonNode() {
        // 建立自己的节点,更新父节点的子节点信息
        addTreeTable(curBbNum);
        getCurNode().addSonNode(blockCounter - 1);
        // 进入自己的节点
        curBbNum = blockCounter - 1;
    }

    // 添加Line
    public void addLine(String token) {
        TableNode curNode = getCurNode();
        TreeTableLine treeTableLine = new TreeTableLine(token, String.valueOf(baseBlockCounter), curBbNum);
        curNode.addLine(treeTableLine);
        // 填表动作本身与临时寄存器无关，不需要操作curTCounter
    }
    public void addFuncLine(String token, LineFuncType funcType, ArrayList<LineFuncFParam> fParamList) {
        TableNode curNode = getCurNode();
        LineFunc lineFunc= new LineFunc(token, String.valueOf(baseBlockCounter), funcType, fParamList, curBbNum);
//        curNode.addCurTCounter();
        baseBlockCounter++;
        curNode.addLine(lineFunc);
        // 将
        // 添加汇编码
        StringBuilder sb = new StringBuilder("");
        if (fParamList.size() > 0) {
            LineIdent lineIdent = fParamList.get(0).getLineIdent();
            Type type = IntegerType.i32; // 默认i32
            if (lineIdent instanceof Ident) {
                type = ((Ident) lineIdent).getLineIdentType();
            }
            else if (lineIdent instanceof Array) {
                // TODO: 数组
            }
            sb.append(type)
                    .append(" ")
                    .append(lineIdent.getTNum());
        }
        for (int i = 1; i < fParamList.size(); i++) {
            LineIdent lineIdent = fParamList.get(i).getLineIdent();
            Type type = IntegerType.i32; // 默认i32
            if (lineIdent instanceof Ident) {
                type = ((Ident) lineIdent).getLineIdentType();
            }
            else if (lineIdent instanceof Array) {
                // TODO: 数组
            }
            sb.append(", ")
                    .append(type)
                    .append(" ")
                    .append(lineIdent.getTNum());;
        }
        addLlvm("define dso_local" + " " + funcType.toString() + " " + "@" + token + "(" + sb.toString() + ") {\n");
    }

    public void addIdentLine(String name, Type type, String value, boolean isConst, boolean isGlobal) {
        TableNode curNode = getCurNode();
        StringBuilder sb = new StringBuilder("");
        if (isGlobal) {
            Ident Ident = new Ident(name, "@" + name, isConst, curBbNum, type, value);
            curNode.addLine(Ident);
            // 生成汇编码
            // @a = dso_local constant i32 5
            // @b = dso_local global i32 5
            sb.append("@")
                    .append(name)
                    .append(" = dso_local ")
                    .append(isConst ? "constant" : "global")
                    .append(" ")
                    .append(type.toString());
                    if (value != null) {
                        sb.append(" ")
                                .append(value);
                    }
                    sb.append("\n");
        }
        else {
            /*
            %1 = alloca i32
            store i32 %2, i32* %1
             */
            // 分配一个寄存器 alloca然后将初始化的值store
//            String tNum = "%a" + curNode.getCurTCounter();
//            curNode.addCurTCounter();
            String tNum = "%a" + baseBlockCounter;
            baseBlockCounter++;
            Ident ident = new Ident(name, tNum, isConst, curBbNum, type, value);
            curNode.addLine(ident);
            // 生成汇编码
            // alloca
            sb.append("    ")
                    .append(tNum)
                    .append(" = alloca ")
                    .append(type.toString())
                    .append("\n");
            if (value != null) {
                    sb.append("    store ")
                          .append(type)
                          .append(" ")
                          .append(value)
                          .append(", ")
                          .append(type)
                          .append("* ")
                          .append(tNum)
                          .append("\n");
            }
        }
        addLlvm(sb.toString());
    }

    // 完成基本块编译，返回父节点
    public void returnNode() {
        curBbNum = getCurNode().getParentNode();
    }

    // 强制类型转换，暂时没用
    public String toType(Type type, Pair value) {
        // value也可能是寄存器，是寄存器的话似乎没什么意义
        if (type instanceof IntegerType) {
            if (((IntegerType) value.getType()).compareTo((IntegerType) type) == 0) {
                return value.getResult();
            }
            if (value.getResult().charAt(0) == '@' || value.getResult().charAt(0) == '%') {
                if (((IntegerType) value.getType()).compareTo((IntegerType) type) > 0) {
                    // 大转小不会
                    return value.getResult();
                }
                else if (((IntegerType) value.getType()).compareTo((IntegerType) type) < 0){
                    String holdResult = "%" + baseBlockCounter;
                    baseBlockCounter++;
                    StringBuilder sb_add  = new StringBuilder("    ");
                    sb_add.append(holdResult)
                            .append(" = add ")
                            .append(type.toString())
                            .append(" 0, ")
                            .append(value.getResult())
                            .append("\n");
                    addLlvm(sb_add.toString());
                    return holdResult;
                }
                else {
                    return value.getResult();
                }
            }
            else {
                int result_int = Integer.parseInt(value.getResult());
                switch (type.toString()) {
                    case "i1": {
                        return String.valueOf((char) result_int);
                    }
                    case "i32": {
                        return String.valueOf((int) result_int);
                    }
                    default: {
                        System.out.println("不存在的数据类型");
                        return null;
                    }
                }
            }
        }
        else if (type instanceof LineArrayType) {
            // 数组
            if (((LineArrayType) type).getBaseType().compareTo(((LineArrayType) value.getType()).getBaseType()) == 0) {
                return value.getResult();
            }
            else if (((LineArrayType) type).getBaseType().compareTo(((LineArrayType) value.getType()).getBaseType()) < 0) {
                //大转小不会
                return value.getResult();
            }
            else {
                // 小转大
                // 新开一个数组，每个元素都是value对应元素add0(检测到是全0则不用)
                // 但我暂时不想写
                return value.getResult();
            }
        }
        else {
            System.out.println("不是普通变量也不是数组？");
            return null;
        }
    }

    /*
    * 遍历语法树
     */

    public void analyze(CompUnit compUnit) {
        visitCompUnit(compUnit);
    }

    public void visitCompUnit(CompUnit compUnit) {
        // CompUnit -> {Decl} {FuncDef} MainFuncDef
        // 顶级块，新建Node
        addTreeTable(-1);
        curBbNum = 0;
        // 库函数
        /*
        declare i32 @getint()
        declare void @putint(i32)
        declare void @putch(i32)
        declare void @putstr(i8*)
        */
        addLlvm("declare i32 @getint()\n");
        addLlvm("declare void @putint(i32)\n");
        addLlvm("declare void @putch(i32)\n");
        addLlvm("declare void @putstr(i8*)\n\n");
        if (compUnit.getDecls() != null) {
            for (Decl decl : compUnit.getDecls()) {
                visitDecl(decl, true);
            }
        }
        addLlvm("\n");
        if (compUnit.getFuncDefs() != null) {
            for (FuncDef funcDef : compUnit.getFuncDefs()) {
                visitFuncDef(funcDef);
            }
        }
        visitMainFuncDef(compUnit.getMainFuncDef());
    }

    public void visitDecl(Decl decl, boolean isGlobal) {
        // Decl → ConstDecl | VarDecl
        if (decl.getConstDecl() != null) {
            visitConstDecl(decl.getConstDecl(), isGlobal);
        }
        else {
            visitVarDecl(decl.getVarDecl(), isGlobal);
        }
    }

    public void visitVarDecl(VarDecl varDecl, boolean isGlobal) {
        // VarDecl → BType VarDef { ',' VarDef } ';'
        Type type = visitBType(varDecl.getbType());
        for (VarDef varDef : varDecl.getVarDefList()) {
            Triple triple = visitVarDef(varDef, type);
            // 更新type 不更新，强制转化
//            type = bigger(type, triple.getPair().getType());
            addIdentLine(triple.getName(), type, toType(type, triple.getPair()), false, isGlobal);
            // 生成汇编码封装进addIdentLine
        }
    }

    public Triple visitVarDef(VarDef varDef, Type type) {
        // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
        // VarDef → Ident { '[' ConstExp ']' } [ '=' InitVal ]
        String name;
        Pair pair;
        if (varDef.getConstExpList() == null || varDef.getConstExpList().size() == 0) {
            name = varDef.getIdent().getToken();
            if (varDef.getAssignToken() == null) {
                // 未初始化
                // 未初始化value为0
                pair = new Pair(type, "0", null, false);
            }
            else {
                pair = visitInitVal(varDef.getInitVal());
//                pair = visitConstExp(varDef.getConstExpList().get(0));
            }
            return new Triple(name, pair);
        }
        else {
            // TODO: 数组
            return null;
        }
    }

    public Pair visitInitVal(InitVal initVal) {
        //  InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
        if (initVal.getLb() == null) {
            return visitExp(initVal.getExp(), false);
        }
        else {
            // TODO: 数组
            return null;
        }
    }

    public void visitConstDecl(ConstDecl constDecl, boolean isGlobal) {
        Type type = visitBType(constDecl.getbType());
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
        for (ConstDef constDef : constDecl.getConstDefs()) {
            Triple triple = visitConstDef(constDef);
            // 更新type,不更新，强制转化
//            type = bigger(type, triple.getPair().getType());
            addIdentLine(triple.getName(), type, toType(type, triple.getPair()), true, isGlobal);
            // 生成汇编码封装进addIdentLine
        }
    }

    public Type visitBType(BType bType) {
        // BType → 'int'
        if (bType.getIntToken().getToken().equals("int")) {
            return IntegerType.i32;
        }
        else {
            return IntegerType.i32;
            // 用不到，纯占位默认i32
        }
    }

    public Triple visitConstDef(ConstDef constDef) {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
        String name;
        Pair pair;
        if (constDef.getConstExps() == null || constDef.getConstExps().size() == 0) {
            name = constDef.getIdent().getToken();
            pair = visitConstInitVal(constDef.getConstInitVal());
//            pair = visitConstExp(constDef.getConstExps().get(0));
            return new Triple(name, pair);
        }
        else {
            // TODO: 数组
            name = constDef.getIdent().getToken();
            List<Pair> constExpList = new ArrayList<>();
            for (ConstExp constExp : constDef.getConstExps()) {
                constExpList.add(visitConstExp(constExp));
            }
            pair = visitConstInitVal(constDef.getConstInitVal());
            // 只需要返回Triple，在addLine中统一生成中间代码
            return new Triple(name, pair);
            // 分配内存空间，ConstInitVal计算值后在这里传值
            // ConstInitVal计算值
            // 传值（store）
        }
    }

    public Pair visitConstInitVal(ConstInitVal constInitVal) {
        // ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        if (constInitVal.getLb() == null) {
            return visitConstExp(constInitVal.getConstExp());
        }
        else {
            // TODO: 数组
            if (constInitVal.getConstInitValList() == null || constInitVal.getConstInitValList().size() == 0) {
                // "{""}",我也不知道为什么会有这种情况，大概是数组长度为0吧
                List<Integer> lenList = new ArrayList<Integer>();
                lenList.add(0);
                // 好奇怪，也没法调用
                List<Pair> pairs = new ArrayList<>();
                pairs.add(null);
                return new Pair(new LineArrayType(IntegerType.i32, IntegerType.i32, 0, 1, lenList), null, pairs, false);
            }
            else {
                // 正常数组
                // [3 x i32] [i32 1, i32 2, i32 0], [3 x i32] zeroinitializer]
                // type [consInitVal, con···]
                // zeroinitializer 不需要括号环绕
                List<Pair> pairList = new ArrayList<Pair>();
                for (ConstInitVal curConstInitVal : constInitVal.getConstInitValList()) {
                    pairList.add(visitConstInitVal(curConstInitVal));
                }
                Type lowType = pairList.get(0).getType(); // 类型肯定是一样的
                StringBuilder sb_value = new StringBuilder();
                sb_value.append("[");

            }
            return null;
        }
    }

    public Pair visitConstExp(ConstExp constExp) {
        //  ConstExp → AddExp
        // 包含在赋值语句中，可以直接返回单个值，因此false
        return visitAddExp(constExp.getAddExp(), false);
    }

    public void visitFuncDef(FuncDef funcDef) {
        //  FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        // 看看形参表
        ArrayList<LineFuncFParam> funcFParamList;
        if (funcDef.getFuncFParams() != null) {
            funcFParamList = visitFuncFParams(funcDef.getFuncFParams());
        }
        else {
            funcFParamList = new ArrayList<LineFuncFParam>();
            // 不放null
        }
        // 添加表项
        LineFuncType funcType = visitFuncType(funcDef.getFuncType());
        addFuncLine(funcDef.getIdent().getToken(), funcType, funcFParamList);
        // 看看后面的block
        visitBlock(funcDef.getBlock(), funcFParamList, true, null, null);
        addLlvm("}\n\n");
    }

    public LineFuncType visitFuncType(FuncType funcType) {
        // FuncType → 'void' | 'int'
        if (funcType.getType() == 0) {
            return FuncVoidType.typeVoid;
        }
        else if (funcType.getType() == 1) {
            return IntegerType.i32;
        }
        else {
            // 可能的其他类型，目前没有
            System.out.println("访问了不存在的函数返回类型");
            return null;
        }
    }

    public ArrayList<LineFuncFParam> visitFuncFParams(FuncFParams funcFParams) {
        // FuncFParams → FuncFParam { ',' FuncFParam }
        ArrayList<LineFuncFParam> funcFParamList = new ArrayList<>();
        for (FuncFParam funcFParam : funcFParams.getFuncFParamList()) {
            funcFParamList.add(visitFuncFParam(funcFParam));
        }
        return funcFParamList;
    }

    public LineFuncFParam visitFuncFParam(FuncFParam funcFParam) {
        //  FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
        if (funcFParam.getLb() == null || funcFParam.getLb().size() == 0) {
            Type type = null;
            String curBType = funcFParam.getbType().getIntToken().getToken();
            switch (curBType) {
                case "int": {
                    type = IntegerType.i32;
                    break;
                }
                default:{
                    // default不要使用
                    return null;
                }
            }
            String tNum = "%a" + baseBlockCounter;
            baseBlockCounter++;
            Ident ident = new Ident(funcFParam.getIdent().getToken(), tNum, false, baseBlockCounter, type, tNum);
            return new LineFuncFParam(ident);
        }
        else {
            // TODO: 数组
            return null;
        }
    }

    public void visitMainFuncDef(MainFuncDef mainFuncDef) {
        // MainFuncDef → 'int' 'main' '(' ')' Block
        // 同样是块首，且不会重、漏
        ArrayList<LineFuncFParam> lineFuncFParams = new ArrayList<>();
        addFuncLine("main", IntegerType.i32, lineFuncFParams);
        // 添加汇编封装进addFuncLine()
//        // 建立自己的节点,更新父节点的子节点信息
//        addTreeTable(curBbNum);
//        getCurNode().addSonNode(bbCounter - 1);
//        // 进入自己的节点
//        curBbNum = bbCounter - 1;

        // node只用来描述作用域，闭上眼睛见block就建，所以封装进visitBlock
        // 建立并进入自己的节点，树结构搭建已封装
//        addSonNode();
        visitBlock(mainFuncDef.getBlock(), lineFuncFParams, true, null, null);
//        returnNode();
        addLlvm("}\n");
    }

    public void visitBlock(Block block, ArrayList<LineFuncFParam> lineFuncFParams, boolean funcCall, String blockBreak, String blockContinue) {
        // 里面有东西再说话，空块谁理你啊
        if (block.getBlockItemList() != null) {
            addSonNode();
            if (lineFuncFParams != null) {
                // 是函数定义后的block，将形参加入符号表
                for (LineFuncFParam lineFuncFParam : lineFuncFParams) {
                    LineIdent lineIdent = lineFuncFParam.getLineIdent();
                    if (lineIdent instanceof Ident) {
                        addIdentLine(lineIdent.getName(), ((Ident) lineIdent).getLineIdentType(), ((Ident) lineIdent).getValue(), false, false);
                    }
                    else if (lineIdent instanceof Array) {
                        // TODO: 数组
                        return;
                    }
                }
            }
            else {
                // stmt中的block，没形参
            }
            // Block → '{' { BlockItem } '}'
//            addLlvm("{\n");
//            addLlvm("block" + baseBlockCounter++ + ":\n");
            Pair pair = null;
            for (BlockItem bi : block.getBlockItemList()) {
                pair = visitBlockItem(bi, blockBreak, blockContinue);
            }
            if (funcCall) {
                StringBuilder sb = new StringBuilder("    ret ");
                if (pair != null) {
                    if (pair.getType().equals(FuncVoidType.typeVoid)){
                        sb.append("void\n");
                    }
                    else if (pair.getType().equals(IntegerType.i32)) {
                        sb.append(pair.getType().toString())
                                .append(" ")
                                .append(pair.getResult())
                                .append("\n");
                    }
                }
                else {
                    sb.append("void\n");
                }
                addLlvm(sb.toString());
            }
//            addLlvm("}\n");
//            addLlvm("\n");
            returnNode();
        }
    }

    public Pair visitBlockItem(BlockItem bi, String blockBreak, String blockContinue) {
        // BlockItem → Decl | Stmt
        if (bi.getDecl() != null) {
            visitDecl(bi.getDecl(), false);
            return null;
        }
        else {
            return visitStmt(bi.getStmt(), blockBreak, blockContinue);
        }
    }

    public Pair visitStmt(Stmt stmt, String blockBreak, String blockContinue) {
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
        if (stmt.getType() == Stmt.StmtType.LValExp) {
            //LVal '=' Exp ';'
            //分析Exp，然后将值送给LVal
            Pair pair = visitExp(stmt.getExp(), false);
            Type type2 = pair.getType();
            String value = pair.getResult();
            // store i32 %2, i32* %1
            // 查表获得左值信息,数组最终也是一个个Ident
            // value为新值
            Ident left = visitLVal(stmt.getLVal(), value, true);
            // 返回的就是表中的本尊，不能是新建的，因为还要修改值
            // 生成中间代码
            StringBuilder sb = new StringBuilder("    ");
            sb.append("store ")
                    .append(type2)
                    .append(" ")
                    .append(value)
                    .append(", ")
                    .append(left.getLineIdentType())
                    .append("* ")
                    .append(left.getTNum())
                    .append("\n");
            addLlvm(sb.toString());
        }
        else if (stmt.getType() == Stmt.StmtType.Exp) {
            // [Exp] ';'
            if (stmt.getExp() != null) {
                visitExp(stmt.getExp(), true);
            }
        }
        else if (stmt.getType() == Stmt.StmtType.Block) {
            // Block
            visitBlock(stmt.getBlock(), null, false, blockBreak, blockContinue);
        }
        else if (stmt.getType() == Stmt.StmtType.If) {
            // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            // 分配2/3个基本块计数器，分为if else then，
            String blockCond = "a" + baseBlockCounter;
            baseBlockCounter++;
            addLlvm("    br label %" + blockCond + "\n");
            if (stmt.getElseToken() != null) {
                // 三个基本块
                String blockIf = "a" + baseBlockCounter;
                baseBlockCounter++;
                String blockElse = "a" + baseBlockCounter;
                baseBlockCounter++;
                String blockThen = "a" + baseBlockCounter;
                baseBlockCounter++;
                visitCond(stmt.getCond(), blockIf, blockElse, blockCond);
                addLlvm(blockIf + ":\n");
                visitStmt(stmt.getStmtList().get(0), blockBreak, blockContinue);
                addLlvm("    br label %" + blockThen + "\n");
                addLlvm(blockElse + ":\n");
                visitStmt(stmt.getStmtList().get(1), blockBreak, blockContinue);
                addLlvm("    br label %" + blockThen + "\n");
                addLlvm(blockThen + ":\n");
            }
            else {
                // 两个基本块
                String blockIf = "a" + baseBlockCounter;
                baseBlockCounter++;
                String blockThen = "a" + baseBlockCounter;
                baseBlockCounter++;
                visitCond(stmt.getCond(), blockIf, blockThen, blockCond);
                addLlvm(blockIf + ":\n");
                visitStmt(stmt.getStmtList().get(0), blockBreak, blockContinue);
                addLlvm("    br label %" + blockThen + "\n");
                addLlvm(blockThen + ":\n");

            }
        }
        else if (stmt.getType() == Stmt.StmtType.For) {
            // 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt
            //
            /*
            ForStmt1的变量需要进入stmt的作用域
            目前不允许decl因此不做处理，若增加，只需修改visitBlock
            将判断有无blockItem和建立Node，returnNode提到函数外，手动环绕
            if(){
                add
                visit
                return
            }
             */
            // ForStmt1
            if (stmt.getForStmt1() != null) {
                visitForStmt(stmt.getForStmt1());
            }
            /*
                br 1
            block 1
                cond 2 4
            block 2
                stmt
                br 3
            block 3
                ForStmt2
                br 1
            block 4
             */
            // cond
            // stmt
            // ForStmt2
            // 结束位置
            // 暂时只写了无缺省的版本
            String blockCond;
            String blockStmt;
            String blockForStmt2;
            String blockOut;
            blockCond = "a" + baseBlockCounter;
            baseBlockCounter++;
            blockStmt = "a" + baseBlockCounter;
            baseBlockCounter++;
            blockForStmt2 = "a" + baseBlockCounter;
            baseBlockCounter++;
            blockOut = "a" + baseBlockCounter;
            baseBlockCounter++;
            addLlvm("    br label %" + blockCond + "\n");
            // Cond缺省封装入visitCond
            visitCond(stmt.getCond(), blockStmt, blockOut, blockCond);
            addLlvm(blockStmt + ":\n");
            visitStmt(stmt.getStmtList().get(0), blockOut, blockForStmt2);
            addLlvm("    br label %" + blockForStmt2 + "\n");
            addLlvm(blockForStmt2 + ":\n");
            if (stmt.getForStmt2() != null) {
                visitForStmt(stmt.getForStmt2());
            }
            addLlvm("    br label %" + blockCond + "\n");
            addLlvm(blockOut + ":\n");
        }
        else if (stmt.getType() == Stmt.StmtType.Break) {
            // 'break' ';'
            addLlvm("    br label %" + blockBreak + "\n");
        }
        else if (stmt.getType() == Stmt.StmtType.Continue) {
            // 'continue' ';'
            addLlvm("    br label %" + blockContinue + "\n");
        }
        else if (stmt.getType() == Stmt.StmtType.Return) {
            // 'return' [Exp] ';'
//            StringBuilder sb = new StringBuilder("ret");
            if (stmt.getExp() != null) {
                Pair pair = visitExp(stmt.getExp(), false);
//                sb.append(" ").append(pair.getType().toString()).append(" ").append(pair.getResult());
//                addLlvm("    " + sb.toString() + "\n");
                return pair;
            }
            return new Pair(FuncVoidType.typeVoid, "void", null, false);
        }
        else if (stmt.getType() == Stmt.StmtType.LValGetint) {
            // LVal '=' 'getint''('')'';'
            // %3 = call i32 @getint()
            // store i32 %3, i32* %1
            // 新开一个寄存器，结果转存到LVal
//            String result = "%a" + getCurNode().getCurTCounter();
//            getCurNode().addCurTCounter();
            String result = "%a" + baseBlockCounter;
            baseBlockCounter++;
            Type type = IntegerType.i32;
            StringBuilder sb_call = new StringBuilder("    ");
            sb_call.append(result)
                    .append(" = call ")
                    .append(type.toString())
                    .append(" @getint()\n");
            addLlvm(sb_call.toString());
            Ident ident = visitLVal(stmt.getLVal(), result, true);
            StringBuilder sb_store = new StringBuilder("    ");
            sb_store.append("store ")
                    .append(type.toString())
                    .append(" ")
                    .append(result)
                    .append(", ")
                    .append(ident.getLineIdentType().toString())
                    .append("* ")
                    .append(ident.getTNum())
                    .append("\n");
            addLlvm(sb_store.toString());
        }
        else if (stmt.getType() == Stmt.StmtType.Printf) {
            // 'printf''('FormatString{','Exp}')'';'
            String str = stmt.getFormatString().getToken();
            int expCounter = 0; // number of %d or else
            // 掐头去尾，去除引号
            for (int i = 1; i < str.length() - 1; i++) {
                char c = str.charAt(i);
                if (c == '%') {
                    // %
                    if (str.charAt(++i) == 'd') {
                        // %d
                        // 打印ExpList.get(expCounter++)
                        Pair pair = visitExp(stmt.getExpList().get(expCounter++), false);
                        // call void @putint(i32 %4)
                        StringBuilder sb = new StringBuilder("    ");
                        sb.append("call void @putint(")
                                .append(pair.getType().toString())
                                .append(" ")
                                .append(pair.getResult())
                                .append(")")
                                .append("\n");
                        addLlvm(sb.toString());
                    }
                    else {
                        // 其他数据类型，暂时没有
                        System.out.println("怎么在打印不存在的数据类型");
                        return null;
                    }
                }
                else if (c == '\\') {
                    // '\n'
                    i++;
                    addLlvm("    call void @putch(i32 10)\n");
                }
                else {
                    // 正常打印
                    // call void @putch(i32 101)
                    addLlvm("    call void @putch(i32 " + ((int) c) + ")\n");
                }

            }
        }
        return null;
    }

    public void visitForStmt(ForStmt forStmt) {
        //  ForStmt → LVal '=' Exp
        Stmt curStmt = new Stmt(Stmt.StmtType.LValExp, forStmt.getLVal(), forStmt.getAssignToken(), forStmt.getExp(), new Token(LexType.SEMICN, ";", forStmt.getAssignToken().getLineNumber()));
        visitStmt(curStmt, null, null);
    }

    public void visitCond(Cond cond, String blockTrue, String blockFalse, String blockCond) {
        // Cond → LOrExp
        addLlvm(blockCond + ":\n");
        if (cond == null) {
            // 空默认真
            addLlvm("    br label %" + blockTrue + "\n");
        }
        else {
            visitLOrExp(cond.getLOrExp(), blockTrue, blockFalse);
        }
    }

    public void visitLOrExp(LOrExp lorExp, String blockTrue, String blockFalse) {
        //  LOrExp → LAndExp | LOrExp '||' LAndExp
        //  LOrExp → LAndExp { '||' LAndExp }
        // ||后的部分临时生成一个LOrExp，相当于改成右递归
        // 或运算有一个LAndExp成功了就可以跳转到blockTrue
        String jumpTrue = blockTrue;
        String jumpFalse;
        if (lorExp.getLAndExpList().size() == 1) {
            // 单LAndExp，失败直接jump到bockFalse
            jumpFalse = blockFalse;
            visitLAndExp(lorExp.getLAndExpList().get(0), jumpTrue, jumpFalse);
        }
        else {
            /*
            无脑计算第一个，如果满足短路条件直接跳过递归生成指令
            若不满足短路条件那么刚刚计算过的第一个LAndExp也就没用了，在这里生成跳转到递归调用的位置的指令
            换言之，成功通向blockTrue，失败通向新生成的递归位置，递归传入的block仍然是true和false两个
             */
            String lessLOrExpBlock = "a" + baseBlockCounter;
            baseBlockCounter++;
            // 第一个失败jump到后面的"LOrExp"处
            jumpFalse = lessLOrExpBlock;
            visitLAndExp(lorExp.getLAndExpList().get(0), jumpTrue, jumpFalse);
            addLlvm(lessLOrExpBlock + ":\n");
            List<LAndExp> newLAndExpList = new ArrayList<>(lorExp.getLAndExpList());
            newLAndExpList.remove(0);
            List<Token> newOpList = new ArrayList<>(lorExp.getOpList());
            newOpList.remove(0);
            LOrExp curLOrExp = new LOrExp(newLAndExpList, newOpList);
            visitLOrExp(curLOrExp, blockTrue, blockFalse);
        }
    }

    public void visitLAndExp(LAndExp lAndExp, String blockTrue, String blockFalse) {
        // LAndExp → EqExp | LAndExp '&&' EqExp
        // LAndExp → EqExp { '&&' EqExp }
        // && 后的部分临时生成一个EqExp,相当于临时改成右递归
        // 与运算有一个LOrExp失败了就需要跳转到blockFlase
        String jumpTrue;
        String jumpFalse = blockFalse;
        String lResult;
        if (lAndExp.getEqExpList().size() == 1) {
            // 单EqExp，为真直接jump到bockTrue
            jumpTrue = blockTrue;
            visitEqExp(lAndExp.getEqExpList().get(0), jumpTrue, jumpFalse);
        }
        else {
            /*
            无脑计算第一个，如果满足短路条件直接跳过递归生成指令
            若不满足短路条件那么刚刚计算过的第一个EqExp也就没用了，在这里生成跳转到递归调用的位置的指令
            换言之，成功通向blockTrue，失败通向新生成的递归位置，递归传入的block仍然是true和false两个
             */
            String lessLAndExpBlock = "a" + baseBlockCounter;
            baseBlockCounter++;
            // 第一个为真jump到后面的"LAndExp"处
            jumpTrue = lessLAndExpBlock;
            visitEqExp(lAndExp.getEqExpList().get(0), jumpTrue, jumpFalse);
            addLlvm(lessLAndExpBlock + ":\n");
            List<EqExp> newEqList = new ArrayList<>(lAndExp.getEqExpList());
            newEqList.remove(0);
            List<Token> newOpList= new ArrayList<Token>(lAndExp.getOpList());
            newOpList.remove(0);
            LAndExp curLAndExp = new LAndExp(newEqList, newOpList);
            visitLAndExp(curLAndExp, jumpTrue, jumpFalse);
        }
    }

    public void visitEqExp(EqExp eqExp, String blockTrue, String blockFalse) {
        //  EqExp → RelExp | EqExp ('==' | '!=') RelExp
        //  EqExp → RelExp { ('==' | '!=') RelExp }
        // OP后部分临时生成一个新EqExp
        // 有OP时，单独的RelExp已经不足以判断真假，因而不再在EqExp内部生成跳转指令，转为返回计算结果
        String jumpTrue = blockTrue;
        String jumpFalse = blockFalse;
        if (eqExp.getRelExpList().size() == 1) {
            // 单RelExp，没有多余的逻辑直接跳向最终结果
            jumpTrue = blockTrue;
            visitRelExp(eqExp.getRelExpList().get(0), jumpTrue, jumpFalse);
//            if (eqExp.getRelExpList().get(0).getAddExpList().size() == 1) {
//                // 单add，rel中没有该判断程序
//            }
        }
        else {
            // 取前两个和第一个OP判断
            String lessEqExpBlock = null;
            if (eqExp.getRelExpList().size() == 2) {
                // 不需要继续递归
                jumpTrue = blockTrue;
            }
            else {
                lessEqExpBlock = "a" + baseBlockCounter;
                baseBlockCounter++;
                jumpTrue = lessEqExpBlock;
            }
            // visit第一个RelExp，内部出现错误直接跳转，成功跳转到内部递归
            visitRelExp(eqExp.getRelExpList().get(0), jumpTrue, jumpFalse);
            // 取两个原子元素和一个OP判断，若失败直接跳转，成功则跳转到递归位置
            Pair onL = getLOfEq(eqExp);
            Pair onR = getROfEq(eqExp);
            String op;
            if (eqExp.getOpList().get(0).getToken().equals("==")) {
                // 相等判断
                op = "eq";
            }
            else if (eqExp.getOpList().get(0).getToken().equals("!=")) {
                // 不等判断
                op = "ne";
            }
            else {
                // 其他情况，暂时没有
                return;
            }
            String lResult = "%a" + baseBlockCounter;
            baseBlockCounter++;
            Type type = bigger(onL.getType(), onR.getType());
            StringBuilder sb_icmp = new StringBuilder("    ")
                    .append(lResult)
                    .append(" = icmp ")
                    .append(op)
                    .append(" ")
                    .append(type.toString())
                    .append(" ")
                    .append(onL.getResult())
                    .append(", ")
                    .append(onR.getResult())
                    .append("\n");
            addLlvm(sb_icmp.toString());
            StringBuilder sb_br = new StringBuilder("    ")
                    .append("br i1 ")
                    .append(lResult)
                    .append(", label %")
                    .append(jumpTrue)
                    .append(", label %")
                    .append(jumpFalse)
                    .append("\n");
            addLlvm(sb_br.toString());
            // 递归
            if (eqExp.getRelExpList().size() == 2) {

            }
            else {
                addLlvm(lessEqExpBlock + ":\n");
                List<RelExp> newRelExpList = new ArrayList<>(eqExp.getRelExpList());
                newRelExpList.remove(0);
                List<Token> newOpList = new ArrayList<>(eqExp.getOpList());
                newOpList.remove(0);
                EqExp newEqExp = new EqExp(newRelExpList, newOpList);
                visitEqExp(newEqExp, blockTrue, blockFalse);
            }
        }
    }

    public Pair getLOfEq(EqExp eqExp) {
        return getLastOfRel(eqExp.getRelExpList().get(0));
    }

    public Pair getROfEq(EqExp eqExp) {
        return  getFirstOfRel(eqExp.getRelExpList().get(1));
    }

    public void visitRelExp(RelExp relExp, String blockTrue, String blockFalse) {
        // RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
        // RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp }
        String jumpTrue;
        String jumpFalse = blockFalse;
        if (relExp.getAddExpList().size() == 1) {
            // 单add，直接跳转
            jumpTrue = blockTrue;
            Pair singlePair = visitAddExp(relExp.getAddExpList().get(0), false);
            String lResult = "%a" + baseBlockCounter;
            baseBlockCounter++;
            StringBuilder sb_icmp = new StringBuilder("    ")
                    .append(lResult)
                    // 不用eq1，因为所有非0整数都是真，但不都eq1
                    .append(" = icmp ne i32 0, ")
                    .append(singlePair.getResult())
                    .append("\n");
            addLlvm(sb_icmp.toString());
            StringBuilder sb_br = new StringBuilder("    ")
                    .append("br i1 ")
                    .append(lResult)
                    .append(", label %")
                    .append(jumpTrue)
                    .append(", label %")
                    .append(jumpFalse)
                    .append("\n");
            addLlvm(sb_br.toString());
        }
        else {
            // 多Add，取两个和一个op计算，遇到false直接返回0,true则跳转内部递归
            // 弹一个add和一个op递归，全部通过再返回1
            String lessRelBlock = null; // 拉出来只是我不想再写一遍中间一大段
            if (relExp.getAddExpList().size() == 2) {
                // 无需继续递归
                jumpTrue = blockTrue;
            }
            else {
                lessRelBlock = "a" + baseBlockCounter;
                baseBlockCounter++;
                jumpTrue = lessRelBlock;
            }
            Pair pair1 = getLOfRel(relExp);
            Pair pair2 = getROfRel(relExp);
            Type type = bigger(pair1.getType(), pair2.getType());
            String op;
            switch (relExp.getOpList().get(0).getToken()) {
                case "<": {
                    op = "ult";
                    break;
                }
                case "<=": {
                    op = "ule";
                    break;
                }
                case ">": {
                    op = "ugt";
                    break;
                }
                case ">=": {
                    op = "uge";
                    break;
                }
                default: {
                    // 其他情况，暂时没有
                    return;
                }
            }
            String result = "%a" + baseBlockCounter;
            baseBlockCounter++;
            // %24 = icmp ne i32 0, 0
            StringBuilder sb_icmp = new StringBuilder("    ")
                    .append(result)
                    .append(" = icmp ")
                    .append(op)
                    .append(" ")
                    .append(type.toString())
                    .append(" ")
                    .append(pair1.getResult())
                    .append(", ")
                    .append(pair2.getResult())
                    .append("\n");
            addLlvm(sb_icmp.toString());
            StringBuilder sb_br = new StringBuilder("    ")
                    .append("br i1 ")
                    .append(result)
                    .append(", label %")
                    .append(jumpTrue)
                    .append(", label %")
                    .append(jumpFalse)
                    .append("\n");
            addLlvm(sb_br.toString());
            if (relExp.getAddExpList().size() == 2) {

            }
            else {
                addLlvm(lessRelBlock + ":\n");
                List<AddExp> newAddExpList = new ArrayList<>(relExp.getAddExpList());
                newAddExpList.remove(0);
                List<Token> newOpList = new ArrayList<>(relExp.getOpList());
                newOpList.remove(0);
                RelExp newRelExp = new RelExp(newAddExpList, newOpList);
                visitRelExp(newRelExp, jumpTrue, jumpFalse);
            }
        }
    }

    public Pair getLOfRel(RelExp relExp) {
        return visitAddExp(relExp.getAddExpList().get(0),false);
    }

    public Pair getROfRel(RelExp relExp) {
        return visitAddExp(relExp.getAddExpList().get(1), false);
    }

    public Pair getLastOfRel(RelExp relExp) {
        return visitAddExp(relExp.getAddExpList().get(relExp.getAddExpList().size() - 1), false);
    }

    public Pair getFirstOfRel(RelExp relExp) {
        return visitAddExp(relExp.getAddExpList().get(0), false);
    }

    public Ident visitLVal(LVal lVal, String value, boolean update) {
        // LVal → Ident {'[' Exp ']'}
        if (lVal.getExpList() == null || lVal.getExpList().size() == 0) {
            if (update) {
                return findAndChangeLVal(lVal.getIdent().getToken(), value);
            }
            else {
                return findLVal(lVal.getIdent().getToken());
            }
        }
        else {
            // TODO: 数组
            return null;
        }
    }

    public Type bigger(Type t1, Type t2) {
        // 暂时只有整形，其他类型可以用类来区分
        if (t1 instanceof IntegerType && t2 instanceof IntegerType) {
            if (((IntegerType) t1).compareTo((IntegerType) t2) >= 0) {
                return (IntegerType) t1;
            }
            else {
                return (IntegerType) t2;
            }
        }
        else {
            // 用不到，但防止意外默认i32
            return IntegerType.i32;
        }
    }

    public Pair visitExp(Exp exp, boolean isLonely) {
        //  Exp → AddExp
//        if (isLonely) {
//            return null;
//        }
        return visitAddExp(exp.getAddExp(), isLonely);
    }

    // 返回<类型，临时寄存器编号>
    public Pair visitAddExp(AddExp addExp, boolean isLonely) {
        // AddExp → MulExp | AddExp ('+' | '−') MulExp
        // AddExp → MulExp{ ('+' | '−') MulExp }
        Type type; // 类型
        String result; // 得数寄存器编号
        Type type1; // 左操作数类型
        String on1; // 左操作数寄存器编号
        Type type2; // 右操作数类型
        String on2; // 右操作数寄存器编号
        String op; // 操作符

        if (addExp.getOpList() == null || addExp.getOpList().size() == 0) {
            // 单MulExp，生成0+MulExp
            // mul的指令一定在add前，故先执行之占位寄存器编号
            Pair mulPair = visitMulExp(addExp.getMulExpList().get(0), isLonely);
            type2 = mulPair.getType();
            on2 = mulPair.getResult();
            type = type2;
            if (!isLonely) {
                // 被人调用，可以仅返回单独的立即数或寄存器，也就是不用生成算式
                result = on2;
                return new Pair(type, result, null, false);
            }
            // 光杆Exp，因为add是最后一级必然需要生成一些东西来存结果，因而0+
//            result = "%a" + getCurNode().getCurTCounter();
//            getCurNode().addCurTCounter();
//            result = "%" + baseBlockCounter;
//            baseBlockCounter++;
            result = on2;
//            op = "add";
//            // 格式%2 = sub i32 0, %1
//            addLlvm("    " + result + " = " + op + " " + type.toString() + " " + "0" + ", " + on2 + "\n");
            return new Pair(type, result, null, false);
        }
        else {
            /*
            维护：
            type
            result
            type1
            on1
            type2
            on2
            op
             */
            Pair right = visitMulExp(addExp.getMulExpList().get(addExp.getMulExpList().size() - 1), isLonely);
            type2 = right.getType();
            on2 = right.getResult();
            // 将Mul op （）括号内的东西看做新的AddExp，相当于临时改成右递归文法
            // 更新：右递归不行了，相当于给后面一层层套了括号，还得左递归
            ArrayList<MulExp> newMulList = new ArrayList<>(addExp.getMulExpList());
            newMulList.remove(newMulList.size() - 1);
            ArrayList<Token> newOpList = new ArrayList<>(addExp.getOpList());
            newOpList.remove(newOpList.size() - 1);
            Pair left = visitAddExp(new AddExp(newMulList, newOpList), isLonely);
            type1 = left.getType();
            on1 = left.getResult();
            // 类型转换
            type = bigger(type1, type2);
            op = (addExp.getOpList().get(addExp.getOpList().size() - 1).getToken().equals("+")) ? "add" : "sub";
            if (on1.charAt(0) != '%' && on2.charAt(0) != '%') {
                // 常量运算，直接算出结果
                if (type == IntegerType.i32) {
                    int one = Integer.parseInt(on1);
                    int two = Integer.parseInt(on2);
                    result = String.valueOf(op.equals("add") ? (one + two) : (one - two));
                    if (!isLonely) {
                        // 有人调用，可以直接返回单个值
                        return new Pair(type, result, null, false);
                    }
                }
                else {
                    // 其他类型，暂时还用不到
                    return null; // 暂时的，后面得改
                }
            }
//            result = "%a" + getCurNode().getCurTCounter();
//            getCurNode().addCurTCounter();
            result = "%a" + baseBlockCounter;
            baseBlockCounter++;
            // 格式%2 = sub i32 0, %1
            addLlvm("    " + result + " = " + op + " " + type.toString() + " " + on1 + ", " + on2 + "\n");
            return new Pair(type, result, null, false);
        }
    }

    public Pair visitMulExp(MulExp mulExp, boolean isLonely) {
        // MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
        // MulExp → UnaryExp { ('*' | '/' | '%') UnaryExp }
        Type type; // 类型
        String result; // 得数寄存器编号
        Type type1; // 左操作数类型
        String on1; // 左操作数寄存器编号
        Type type2; // 右操作数类型
        String on2; // 右操作数寄存器编号
        String op; // 操作符

        if (mulExp.getOpList() == null || mulExp.getOpList().size() == 0) {
            // 单UnaryExp，生成1*UnaryExp
            // unary的指令一定在mul前，故先执行之占位寄存器编号
            Pair unaryPair = visitUnaryExp(mulExp.getUnaryExpList().get(0), isLonely);
            type2 = unaryPair.getType();
            on2 = unaryPair.getResult();
            type = type2;
            result = on2;
////            result = "%a" + getCurNode().getCurTCounter();
////            getCurNode().addCurTCounter();
//            result = "%a" + baseBlockCounter;
//            baseBlockCounter++;
//            op = "mul";
            // 格式%2 = sub i32 0, %1
//            addLlvm("    " + result + " = " + op + " " + type.toString() + " " + "1" + ", " + on2 + "\n");
            return new Pair(type, result, null, false);
        }
        else {
            /*
            维护：
            type
            result
            type1
            on1
            type2
            on2
            op
             */
            Pair right = visitUnaryExp(mulExp.getUnaryExpList().get(mulExp.getUnaryExpList().size() - 1), isLonely);
            type2 = right.getType();
            on2 = right.getResult();
            // 将Unary op （）括号内的东西看做新的MulExp，相当于临时改成右递归文法
            // 更新：右递归不行，需要左递归
            ArrayList<UnaryExp> newUnaryList = new ArrayList<>(mulExp.getUnaryExpList());
            newUnaryList.remove(newUnaryList.size() - 1);
            ArrayList<Token> newOpList = new ArrayList<>(mulExp.getOpList());
            newOpList.remove(newOpList.size() - 1);
            Pair left = visitMulExp(new MulExp(newUnaryList, newOpList), isLonely);
            type1 = left.getType();
            on1 = left.getResult();
            // 类型转换
            type = bigger(type1, type2);
            op = mulExp.getOpList().get(mulExp.getOpList().size() - 1).getToken();
            switch (op) {
                case "*":
                    op = "mul";
                    break;
                case "/":
                    op = "sdiv";
                    break;
                case "%":
                    op = "srem";
                    break;
                default:
                    return null;
            }
            if (on1.charAt(0) != '%' && on2.charAt(0) != '%') {
                // 常量运算，直接算出结果,Mul不是最高级，不论lonely不lonely都可以直接返回立即数
                if (type == IntegerType.i32) {
                    int one = Integer.parseInt(on1);
                    int two = Integer.parseInt(on2);
                    switch (op) {
                        case "mul":
                            result = String.valueOf(one * two);
                            break;
                        case "sdiv":
                            result = String.valueOf(one / two);
                            break;
                        case "srem":
                            result = String.valueOf(one % two);
                            break;
                        default:// "srem":
                            return null;
                    }
                    return new Pair(type, result, null, false);
                }
                else {
                    // 其他类型，暂时还用不到
                    return null; // 暂时的，后面得改
                }
            }
//            result = "%a" + getCurNode().getCurTCounter();
//            getCurNode().addCurTCounter();
            result = "%a" + baseBlockCounter;
            baseBlockCounter++;
            // 格式%2 = sub i32 0, %1
            addLlvm("    " + result + " = " + op + " " + type.toString() + " " + on1 + ", " + on2 + "\n");
            return new Pair(type, result, null, false);
        }
    }

    public Pair visitUnaryExp(UnaryExp unaryExp, boolean isLonely) {
        //  UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
        Type type;
        String on;
        String result;
        if (unaryExp.getPrimaryExp() != null) {
            return visitPrimaryExp(unaryExp.getPrimaryExp(), isLonely);
        }
        else if (unaryExp.getUnaryExp() != null) {
            // UnaryOp UnaryExp
            if (unaryExp.getUnaryOp().getOpToken().getToken().equals("+")) {
                // do nothing
                return visitUnaryExp(unaryExp.getUnaryExp(), isLonely);
            }
            else if (unaryExp.getUnaryOp().getOpToken().getToken().equals("-")) {
                // -1 * UnaryExp
                Pair pair = visitUnaryExp(unaryExp.getUnaryExp(), isLonely);
                type = pair.getType();
                on = pair.getResult();
                if (on.charAt(0) != '%') {
                    // 又是常量运算，直接算出结果
                    result = String.valueOf(Integer.parseInt(on) * -1);
                    return new Pair(type, result, null, false);
                }
//            result = "%a" + getCurNode().getCurTCounter();
//            getCurNode().addCurTCounter();
                result = "%a" + baseBlockCounter;
                baseBlockCounter++;
                addLlvm("    " + result + " = mul " + type.toString() + " " + "-1" + ", " + on + "\n");
                return new Pair(type, result, null, false);
            }
            else if (unaryExp.getUnaryOp().getOpToken().getToken().equals("!")){
                // ! 条件表达式 是0得1否则得0
                // icmp eq i32 0, UnaryExp
                Pair pair = visitUnaryExp(unaryExp.getUnaryExp(), false);
                if (pair.getResult().charAt(0) != '@' && pair.getResult().charAt(0) != '%') {
                    // 常量，直接判断
                    if (pair.getResult().equals("0")) {
                        return new Pair(IntegerType.i32, "1", null, false);
                    }
                    else {
                        return new Pair(IntegerType.i32, "0", null, false);
                    }
                }
                else {
                    result = "%a" + baseBlockCounter;
                    baseBlockCounter++;
                    StringBuilder sb_icmp = new StringBuilder("    ")
                            .append(result)
                            .append(" = icmp eq i32 0, ")
                            .append(pair.getResult())
                            .append("\n");
                    addLlvm(sb_icmp.toString());
                    return new Pair(IntegerType.i32, result, null, false);
                }
            }
            else {
                // 其他op，暂时没有
                System.out.println("不存在的单目运算符");
                return null;
            }
        }
        else {
            // Ident '(' [FuncRParams] ')'
            // %7 = call i32 @aaa(i32 %5, i32 %6)
            LineFunc lineFunc = findFunc(unaryExp.getIdent().getToken());
            ArrayList<Pair> funcRParams;
            if (unaryExp.getFuncRParams() != null) {
                funcRParams = visitFuncRParams(unaryExp.getFuncRParams());
            }
            else {
                funcRParams = new ArrayList<Pair>();
            }
            StringBuilder sb = new StringBuilder("    ");
            if (!lineFunc.getType().equals(FuncVoidType.typeVoid)) {
                result = "%a" + baseBlockCounter;
                baseBlockCounter++;
                sb.append(result)
                        .append(" = ");
            }
            else {
                result = "0";
            }
            type = lineFunc.getType();
            sb.append("call ")
                    .append(type.toString())
                    .append(" @")
                    .append(lineFunc.getName())
                    .append("(");
            if (funcRParams.size() != 0) {
                sb.append(funcRParams.get(0).getType())
                        .append(" ")
                        .append(funcRParams.get(0).getResult());
            }
            for (int i = 1; i < funcRParams.size(); i++) {
                sb.append(", ")
                        .append(funcRParams.get(i).getType())
                        .append(" ")
                        .append(funcRParams.get(i).getResult());
            }
            sb.append(")\n");
            addLlvm(sb.toString());
            return new Pair(type, result, null, false);
        }
    }

    public ArrayList<Pair> visitFuncRParams(FuncRParams funcRParams) {
        // FuncRParams → Exp { ',' Exp }
        ArrayList<Pair> fRParams = new ArrayList<>();
        for (Exp exp : funcRParams.getExpList()) {
            fRParams.add(visitExp(exp, false));
        }
        return fRParams;
    }

    public Pair visitPrimaryExp(PrimaryExp primaryExp, boolean isLonely) {
        //  PrimaryExp → '(' Exp ')' | LVal | Number
        if (primaryExp.getExp() != null) {
            return visitExp(primaryExp.getExp(), isLonely);
        }
        else if (primaryExp.getLVal() != null) {
            // 不改变值所以value用不到给null就行
            Ident ident = visitLVal(primaryExp.getLVal(), null, false);
            String result;
            Type type = ident.getLineIdentType();
//            if (ident.getValue().charAt(0) != '@' && ident.getValue().charAt(0) != '%') {
//                // 具有确定的值，直接使用 ，如果影响分配寄存器就删掉这个分支
//                result = ident.getValue();
//            }
//            else {
            // 封装load
            // 分配寄存器
//            result = "%a" + getCurNode().getCurTCounter();
//            getCurNode().addCurTCounter();
            result = "%a" + baseBlockCounter;
            baseBlockCounter++;
            // 生成中间代码
            //%2 = load i32, i32* @b
            StringBuilder sb = new StringBuilder("    ");
            sb.append(result)
                    .append(" = load ")
                    .append(type.toString())
                    .append(", ")
                    .append(type.toString())
                    .append("* ")
                    .append(ident.getTNum())
                    .append("\n");
            addLlvm(sb.toString());
//            }
            return new Pair(type, result, null, false);
        }
        else {
            Type type = IntegerType.i32;
            String result = primaryExp.getNumber().getIntConstToken().getToken();
            return new Pair(type, result, null, false);
        }
    }
}
