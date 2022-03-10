package Cave.Dragon;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Cave {
    public abstract void explore();
    protected Scanner scan;
    // increase dist each time an area is explored
    // if dist is 3 all spawned areas must be dens
    //protected static int dist = 0;      // Dist isn't being used correctly, all caves made before it's ever incremented

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
