import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ReverseInt {
    public static void main(String[] args) {
        int num = ThreadLocalRandom.current().nextInt(-1000, 1000);
        System.out.printf("The random number is: %s%n", num);
        System.out.printf("The reversed number is: %s%n", reverse(num));

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        String x = "Hello";
        System.out.printf("Value of x before function:%s%n", x);
        addValue(x);
        System.out.printf("Value of x after function:%s%n", x);
    }

    public static int reverse(int num) {
        List<String> l = Arrays.stream(String.valueOf(num).split("")).collect(Collectors.toList());
        if (num <0) {
            List<String> sub = l.subList(1, l.size());
            Collections.reverse(sub);
            return Integer.parseInt("-"+String.join("",sub));
        }
        Collections.reverse(l);
        return Integer.parseInt(String.join("",l));
    }

    public static void addValue(String i) {
        i = "ghi";
        System.out.printf("Value of i in function:%s%n", i);
    }
}
