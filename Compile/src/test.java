import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(0);

        Integer a = getInteger(0, list);
        a = 1;
        System.out.println((int) '\n');
    }

    static Integer getInteger(int i, List<Integer> list) {
        return list.get(i);
    }
}