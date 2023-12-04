package Type;

public class LineFuncType implements Type {
    private Type returnType; // 返回类型

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return returnType.toString();
    }

    @Override
    public String toStringForCall() {
        return null;
    }
}
