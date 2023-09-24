package Lexical;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private static Lexer lexer;
    private String source; // 源程序字符串,依靠外部赋值
    private int curPos; // 当前字符串位置指针
    private String token; // 解析单词值
    private LexType lexType; // 解析单词类型
    private int number; // 解析数值

    // 以下非单行内属性，称为词法分析器状态属性
    private int lineNum; // 当前行号
    private boolean inExegesis;
    private boolean inFormatString;
    private static final Map<String, LexType> reserveWords;
    private final String[] reserveWords = new String[] {
            "main",
            "const",
            "int",
            "break",
            "continue",
            "if",
            "else",
            "for",
            "getint",
            "printf",
            "return",
            "void"
    }; // 保留字表

    // 配置属性 注意此处无需手动修改
    private boolean printAble;

    static {
        lexer = new Lexer();
    }

    public Lexer() {
        initLexer();
        lineNum = 0; // 行号可能在运算中修改，因此仅初始化一次。
        inExegesis = false;
        inFormatString = false;

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
            str = next();
        }
    }

    // 总管单词获取和分析
    public String next() {
        if (inExegesis) {

            return token;
        }
        if (inFormatString) {
            return token;
        }
        token = "";
        lexType = null;
        number = 0;
        char c = source.charAt(curPos++);
        if (isNonDigit(c)) {
            // 是字母或下划线
            token += c;
            while (curPos < source.length() && Character.isLetterOrDigit(source.charAt(curPos))) {
                c = source.charAt(curPos++);
                token += c;
            }
            reserve();
        }
        else if (Character.isDigit(c)) {
            // 是数字
        }
        else {
            // 其他，switch处理各种符号
        }
        return getToken();
    }

    private void reserve() {
        for (String str : reserveWords) {
            if (token.equals(str)) {

            }
        }
    }

    // 封装后的打印函数
    public void lexerPrinter(String str) {
        if (printAble) {
            System.out.println(str);
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

    public boolean isInFormatString() {
        return inFormatString;
    }

    public void setInFormatString(boolean inFormatString) {
        this.inFormatString = inFormatString;
    }

    public boolean isPrintAble() {
        return printAble;
    }

    public void setPrintAble(boolean printAble) {
        this.printAble = printAble;
    }
}
