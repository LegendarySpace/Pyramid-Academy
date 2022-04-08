import java.util.concurrent.ThreadLocalRandom;

public class Buzz {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int x = ThreadLocalRandom.current().nextInt(200);
            System.out.println(x);
            System.out.println(fizz(x));
        }
    }

    public static String fizz(int num) {
        if (num % 3 == 0 && num % 5 == 0) return "fizz buzz bazz";
        if (num % 3 == 0) return "fizz";
        if (num % 5 == 0) return "buzz";
        return "fizzled";
    }
}
