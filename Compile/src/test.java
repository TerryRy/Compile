import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        System.out.println(list);
        list.remove(0);
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    static Integer getInteger(int i, List<Integer> list) {
        return list.get(i);
    }
}