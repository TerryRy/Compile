package Type;

/*
* 所有整形变量的Type，而不仅是int
 */
public class IntegerType extends LineFuncType implements Type, Comparable<IntegerType>{
    private final int bit;

    public static final IntegerType i1 = new IntegerType(1);

    public static final IntegerType i8 = new IntegerType(8);

    public static final IntegerType i32 = new IntegerType(32);

    private IntegerType(int bit) {
        this.bit = bit;
    }

    public boolean isI1() {
        return this.bit == 1;
    }

    public boolean isI8() {
        return this.bit == 8;
    }

    public boolean isI32() {
        return this.bit == 32;
    }

    public int getBit() {
        return this.bit;
    }

    @Override
    public String toString() {
        return "i" + bit;
    }


    @Override
    public int compareTo(IntegerType other) {
        if (this.bit > other.bit) {
            return 1;
        }
        else if (this.bit == other.bit) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
