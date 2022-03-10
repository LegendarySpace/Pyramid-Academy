package Cave.Dragon;

import java.util.concurrent.ThreadLocalRandom;

public class DragonCave {
    public static void main(String[] args) {
        introduction();
    }

    public static void introduction() {
        String mtn = "Smaugenraug Mountains";

        System.out.println("           DRAGON CAVE");
        System.out.println("        Adventure awaits");
        System.out.println("================================");
        System.out.println("");
        System.out.println("You're an adventurer from a town in the woods of honey searching for excitement and treasure.");
        System.out.println(String.format("One day while out exploring you decide to venture to the %s in search of fortune and fame.", mtn));
        System.out.println(String.format("All Adventurers know %s is a breeding ground for dragons, and where there are dragons there's treasure.", mtn));
        System.out.println("There's 2 types of dragons: kind dragons who share their treasure, and vicious dragons who devour all they encounter.");
        System.out.println("Which side of the mountain do you decide to approach? (East, West, North)");

        // East is most likely to be a den
        // West is most likely to be a path
        // North is most likely to be a fork
    }
}
