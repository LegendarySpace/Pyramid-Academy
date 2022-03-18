package Cave.Dragon;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Cave {
    public abstract void explore();
    protected static Scanner scan;

    public static String ensureInput(String inputMessage, String errorMessage) {
        try {
            System.out.println(inputMessage);
            return scan.next();
        } catch (Exception e) {
            System.out.println(errorMessage);
            return ensureInput(inputMessage, errorMessage);
        }
    }

    public static int d20() { return ThreadLocalRandom.current().nextInt(1, 20); }
    public static int d10() { return ThreadLocalRandom.current().nextInt(1, 10); }
    public static int d5() { return ThreadLocalRandom.current().nextInt(1, 3); }
}
