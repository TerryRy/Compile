package Token;

import Lexical.LexType;

public class Token {
    private LexType type;
    private String token;
    private int lineNumber;

    public Token(LexType type, String token, int lineNumber) {
        this.type = type;
        this.token = token;
        this.lineNumber = lineNumber;
    }

    public LexType getType() {
        return type;
    }

    public void setType(LexType type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return type.toString() + " " + token + "\n";
    }
}
