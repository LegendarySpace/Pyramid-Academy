import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Buzz {
    public static void main(String[] args) {
        callMissing();
    }

    public static void callMissing() {
        System.out.println("Hello World");
        var list = IntStream.rangeClosed(1,1000).boxed().collect(Collectors.toList());
        list.remove((Integer) 1000);
        System.out.println(missing(list));
        System.out.println(laughableSolution(list));
    }


    public static int missing(List<Integer> nums) {
        return (int)((Collections.max(nums)/2.0)*(1+Collections.max(nums))) - nums.stream().reduce(Integer::sum).orElse(0);
    }

    public static int laughableSolution(List<Integer> nums){
        List<Object> lawl = IntStream.rangeClosed(1,nums.size())
                .boxed()
                .reduce(
                        List.of(0,false),
                        (List<Object> acc, Integer next) -> (Boolean)acc.get(1) ? acc : (Integer)acc.get(0) + 1 == nums.get(next - 1) ? List.of(next,false) : List.of((Integer)acc.get(0) + 1, true),
                        (in1, in2) -> in1);

        return (Boolean)lawl.get(1) ? (int)lawl.get(0) : 0;
    }


    public static void callOp() {
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
