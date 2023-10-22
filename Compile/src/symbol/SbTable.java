package symbol;

import java.util.Objects;

public class SbTable<Map, Boolean, Integer> {
    private Map sb;
    private java.lang.Boolean isFunc;
    private java.lang.Integer funcType;

    public SbTable(Map sb, java.lang.Boolean isFunc, java.lang.Integer funcType) {
        this.sb = sb;
        this.isFunc = isFunc;
        this.funcType = funcType;
    }

    public Map getSb() {
        return sb;
    }

    public void setSb(Map sb) {
        this.sb = sb;
    }

    public java.lang.Boolean getIsFunc() {
        return isFunc;
    }

    public void setIsFunc(java.lang.Boolean isFunc) {
        this.isFunc = isFunc;
    }

    public java.lang.Integer getFuncType() {
        return funcType;
    }

    public void setFuncType(java.lang.Integer funcType) {
        this.funcType = funcType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SbTable<?, ?, ?> sbTable = (SbTable<?, ?, ?>) o;
        return Objects.equals(sb, sbTable.sb) &&
                Objects.equals(isFunc, sbTable.isFunc) &&
                Objects.equals(funcType, sbTable.funcType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sb, isFunc, funcType);
    }

}
