package Type;

public class FuncVoidType extends LineFuncType implements Type {
    public static final FuncVoidType typeVoid = new FuncVoidType();


    private FuncVoidType() {
    }

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public String toStringForCall() {
        return null;
    }
}
