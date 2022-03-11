package Cave.Dragon;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Cave {
    public abstract void explore();
    protected Scanner scan;

    public static int d20() {
        return ThreadLocalRandom.current().nextInt(1, 20);
    }
    public static int d10() {
        return ThreadLocalRandom.current().nextInt(1, 10);
    }
    public static int d5() {
        return ThreadLocalRandom.current().nextInt(1, 3);
    }
}
