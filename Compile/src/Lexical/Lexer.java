package Lexical;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private static Lexer lexer; // 单例模式
    private String source; // 源程序字符串,依靠外部赋值
    private int curPos; // 当前字符串位置指针
    private String token; // 解析单词值
    private LexType lexType; // 解析单词类型
    private int number; // 解析数值

    // 以下非单行内属性，称为词法分析器状态属性
    private int lineNum; // 当前行号
    private boolean inExegesis; // 注释状态
//    private boolean inFormatString; // printf
    private static final Map<String, LexType> reserveWords;
//    private final String[] reserveWords = new String[] {
//            "main",
//            "const",
//            "int",
//            "break",
//            "continue",
//            "if",
//            "else",
//            "for",
//            "getint",
//            "printf",
//            "return",
//            "void"
//    }; // 保留字表

    // 配置属性 注意此处无需手动修改
    private boolean printAble;

    static {
        // 创建不可变的Map并赋值给final变量
        Map<String, LexType> tempMap = new HashMap<String, LexType>();
        tempMap.put("main", LexType.MAINTK);
        tempMap.put("const", LexType.CONSTTK);
        tempMap.put("int", LexType.INTTK);
        tempMap.put("break", LexType.BREAKTK);
        tempMap.put("continue", LexType.CONTINUETK);
        tempMap.put("if", LexType.IFTK);
        tempMap.put("else", LexType.ELSETK);
        tempMap.put("for", LexType.FORTK);
        tempMap.put("getint", LexType.GETINTTK);
        tempMap.put("printf", LexType.PRINTFTK);
        tempMap.put("return", LexType.RETURNTK);
        tempMap.put("void", LexType.VOIDTK);

        reserveWords = Collections.unmodifiableMap(tempMap);

        lexer = new Lexer();
    }

    public Lexer() {
        initLexer();
        lineNum = 0; // 行号可能在运算中修改，因此仅初始化一次。
        inExegesis = false;
//        inFormatString = false;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("./output.txt"));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Lexer getLexer() {
        return lexer;
    }

    public void initLexer() {
        source = "";
        curPos = 0;
        token = "";
        lexType = null;
        number = 0;
        printAble = false;
    }

    // 总管调度和输出
    public void analyse() {
        // 对于保留字，试图用非注释非FormatString的token和保留字数组对比来判断保留字
        // 因此状态机不用刻意处理保留字，而是将保留字划入Ident中二轮识别。
        String str;
        while (curPos < source.length()) {
            try {
                str = next();
                lexerPrinter(str);
            } catch (Exception e) {
                lexerPrinter("错误！");
            }
        }
    }

    // 总管单词获取和分析
    public String next() throws Exception {
        char c = source.charAt(curPos++);
        if (isInExegesis()) {
            while (curPos < source.length()) {
                if (c == '*' && source.charAt(curPos) == '/') {
                    setInExegesis(false);
                    curPos++;
                    return "";
                }
                curPos++;
            }
            return "";
        }
//        if (isInFormatString()) {
//
//            return token;
//        }
        token = "";
        lexType = null;
        number = 0;
        if (isNonDigit(c)) {
            // 是字母或下划线
            token += c;
            c = source.charAt(curPos++);
            while (curPos < source.length() && Character.isLetterOrDigit(c)) {
                token += c;
                c = source.charAt(curPos++);
            }
            curPos--;
            reserve();
            if (!isSeparator(c)) {
                throw new IllegalStateException();
            }

        }
        else if (Character.isDigit(c)) {
            // 是数字
            token += c;
            c = source.charAt(curPos++);
            while (curPos < source.length() && Character.isDigit(c)) {
                token += c;
                c = source.charAt(curPos++);
            }
            curPos--;
            lexType = LexType.INTCON;
            number = Integer.parseInt(token);
            if (!isSeparator(c)) {
                throw new IllegalStateException();
            }
        }
        else {
            // 其他，switch处理各种符号
            switch (c) {
                case '!' : {
                    token += c;
                    if (source.charAt(curPos++) == '=') {
                        token += '=';
                        lexType = LexType.NEQ;
                    }
                    else {
                        curPos--;
                        lexType = LexType.NOT;
                    }
                    break;
                }
                case '&': {
                    token += c;
                    if (source.charAt(curPos++) == '&') {
                        token += c;
                        lexType = LexType.AND;
                    }
                    else {
                        curPos--;
                        throw new IllegalStateException();
                    }
                    break;
                }
                case '|' : {
                    token += c;
                    if (source.charAt(curPos++) == '|') {
                        token += c;
                        lexType = LexType.OR;
                    }
                    else {
                        curPos--;
                        throw new IllegalStateException();
                    }
                    break;
                }
                case '+' : {
                    token += c;
                    lexType = LexType.PLUS;
                    break;
                }
                case '-' : {
                    token += c;
                    lexType = LexType.MINU;
                    break;
                }
                case '*' : {
                    token += c;
                    lexType = LexType.MULT;
                    break;
                }
                case '/' : {
                    c = source.charAt(curPos++);
                    if (c == '*') {
                        setInExegesis(true);
                        c = source.charAt(curPos++);
                        while (curPos < source.length()) {
                            if (c == '*' && source.charAt(curPos) == '/') {
                                setInExegesis(false);
                                curPos++;
                                return "";
                            }
                            curPos++;
                        }
                        return "";
                    }
                    else if (c == '/') {
                        curPos = source.length(); // 结束当前行
                        return ""; // 不在输出
                    }
                    else {
                        curPos--;
                        token += '/';
                        lexType = LexType.DIV;
                    }
                    break;
                }
                case '%' : {
                    token += c;
                    lexType = LexType.MOD;
                    break;
                }
                case '<' : {
                    token += c;
                    if (source.charAt(curPos++) == '=') {
                        token += '=';
                        lexType = LexType.LEQ;
                    }
                    else {
                        curPos--;
                        lexType = LexType.LSS;
                    }
                    break;
                }
                case '>' : {
                    token += c;
                    if (source.charAt(curPos++) == '=') {
                        token += '=';
                        lexType = LexType.GEQ;
                    }
                    else {
                        curPos--;
                        lexType = LexType.GRE;
                    }
                    break;
                }
                case '=' : {
                    token += c;
                    if (source.charAt(curPos++) == '=') {
                        token += '=';
                        lexType = LexType.EQL;
                    }
                    else {
                        curPos--;
                        lexType = LexType.ASSIGN;
                    }
                    break;
                }
                case ';' : {
                    token += c;
                    lexType = LexType.SEMICN;
                    break;
                }
                case ',' : {
                    token += c;
                    lexType = LexType.COMMA;
                    break;
                }
                case '(' : {
                    token += c;
                    lexType = LexType.LPARENT;
                    break;
                }
                case ')' : {
                    token += c;
                    lexType = LexType.RPARENT;
                    break;
                }
                case '[' : {
                    token += c;
                    lexType = LexType.LBRACK;
                    break;
                }
                case ']' : {
                    token += c;
                    lexType = LexType.RBRACK;
                    break;
                }
                case '{' : {
                    token += c;
                    lexType = LexType.LBRACE;
                    break;
                }
                case '}' : {
                    token += c;
                    lexType = LexType.RBRACE;
                    break;
                }
                case '"' : {
                    token += c;
                    lexType = LexType.STRCON;
                    c = source.charAt(curPos++);
                    while (curPos < source.length() && c != '"') {
                        token += c;
                        c = source.charAt(curPos++);
                    }
                    token += c;
                    break;
                }
                default: {
                    if (c == ' ' || c == '\t' || c == '\n') {
                        return "";
                    }
                    throw new IllegalStateException();
                }
            }
        }
        return lexType + " " + token + '\n';
    }

    private boolean isSeparator(char c) {
        // 暂不考虑
        return true;
    }

    private void reserve() {
        lexType = reserveWords.getOrDefault(token, LexType.IDENFR);
    }

    // 封装后的打印函数
    public void lexerPrinter(String str) {
        if (printAble) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt", true));
                writer.write(str);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(str);
        }
    }

    public boolean isNonDigit(char c) {
        return (Character.isLetter(c) || c == '_');
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getCurPos() {
        return curPos;
    }

    public void setCurPos(int curPos) {
        this.curPos = curPos;
    }

    public LexType getLexType() {
        return lexType;
    }

    public void setLexType(LexType lexType) {
        this.lexType = lexType;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isInExegesis() {
        return inExegesis;
    }

    public void setInExegesis(boolean inExegesis) {
        this.inExegesis = inExegesis;
    }

//    public boolean isInFormatString() {
//        return inFormatString;
//    }
//
//    public void setInFormatString(boolean inFormatString) {
//        this.inFormatString = inFormatString;
//    }

    public boolean isPrintAble() {
        return printAble;
    }

    public void setPrintAble(boolean printAble) {
        this.printAble = printAble;
    }
}
