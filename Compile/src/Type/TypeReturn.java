package Type;

// 仅用在visitStmt到StmtType.Return时返回的pair的type，以便在visitBlock中识别是否已有return语句
public class TypeReturn implements Type {
    public static final TypeReturn ret = new TypeReturn();

    private TypeReturn() {}

    @Override
    public String toStringForCall() {
        return "ReturnTk";
    }

    @Override
    public Type getTypeForMips() {
        return FuncVoidType.typeVoid;
    }
}
