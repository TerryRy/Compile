import java.util.HashSet;
import java.util.Set;

public class test {
    public static void main(String[] args) {
        // 创建两个 Set
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        // 向第一个 Set 添加元素
        set1.add("苹果");
        set1.add("香蕉");
        set1.add("橙子");

        // 向第二个 Set 添加元素
        set2.add("香蕉");
        set2.add("葡萄");
        set2.add("橙子");

        set1.addAll(set2);
        // 输出合并后的元素
        for (String fruit : set1) {
            System.out.println(fruit);
        }
    }
}