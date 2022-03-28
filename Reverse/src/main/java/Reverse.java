import java.util.*;
import java.util.stream.Collectors;

public class Reverse {
    public static void main(String[] args) {
        List<Integer> nthRow = Arrays.asList(1,48,3,864,8,1,4,8,6,23,3124,564);
        ArrayList<Integer> a1 = new ArrayList<Integer>(nthRow);
        ArrayList<Integer> a2 = nthRow.stream()
                .filter(x -> x>4) // filtering stream
                .map(x -> x*20)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toCollection(ArrayList::new));
        var a3 = a1.stream()
                .mapToLong(x-> (long)x)
                .map(x -> x*10)
                .mapToObj(x -> String.valueOf(x))
                .reduce("", (a, x) -> a + x);
        System.out.println(nthRow);
        System.out.println(a2);
        System.out.println(a3);
        System.out.println(nthRow);
        System.out.println(nthRow.stream().max(Integer::compareTo).get());
        String he = "HElloooollleeEEE";
        System.out.println(same(he, "OehL"));
        System.out.println(he);
    }

    public static String reverse() {
        return null;
    }

    public static boolean same(String word1, String word2) {
        HashSet<String> h1 = new HashSet<String>(List.of(word1.toLowerCase().split("")));
        HashSet<String> h2 = new HashSet<String>(List.of(word2.toLowerCase().split("")));
        return h1.equals(h2);
    }
}
