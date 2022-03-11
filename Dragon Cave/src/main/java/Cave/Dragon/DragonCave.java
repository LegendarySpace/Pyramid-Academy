package Cave.Dragon;

import java.util.Scanner;

public class DragonCave {
    public static String mtn = "Smaugenraug Mountains";

    public static Scanner scan;

    public static void main(String[] args) {
        introduction();
        scan = new Scanner(System.in);

        try {
            Cave entrance = determineCave(eastWestNorth());
            entrance.explore();
        } catch (Exception e) {
            System.out.println("Error entering the mountain");
            e.printStackTrace();
        }
        outro();
        scan.close();
    }

    public static void introduction() {
        System.out.println("           DRAGON CAVE");
        System.out.println("        Adventure awaits");
        System.out.println("================================");
        System.out.println();
        System.out.println("You're an adventurer from a town in the woods of honey searching for excitement and treasure.");
        System.out.printf("One day while out exploring you decide to venture to the %s in search of fortune and fame.%n", mtn);
        System.out.printf("All Adventurers know %s is a breeding ground for dragons, and where there are dragons there's treasure.%n", mtn);
        System.out.println("There's 2 types of dragons: kind dragons who share their treasure, and vicious dragons who devour all they encounter.");
    }

    public static void outro() {
        System.out.println();
        System.out.printf("A fitting end to your journey in %s, but things are just beginning for many other adventurers.%n", mtn);
        System.out.printf("Come back again to hear more tales from the %s.%n", mtn);
    }

    public static String eastWestNorth() {
        try {
            String enter;
            do {
                System.out.println("Which side of the mountain do you decide to approach? (East, West, North)");
                enter = scan.next();
            } while (!(enter.equalsIgnoreCase("east") || enter.equalsIgnoreCase("west") || enter.equalsIgnoreCase("north")));
            return enter;
        } catch (Exception e) {
            System.out.println("Error getting user input!!!");
            e.printStackTrace();
        }
        return null;
    }

    public static Cave determineCave(String direction) {
        if (direction.equalsIgnoreCase("east")) {
            // East is most likely to be a den
            return switch (Cave.d5()) {
                case 1 -> new CaveFork(scan,0);
                case 2 -> new CavePath(scan,0);
                default -> new CaveDen(scan,0);
            };
        } else if (direction.equalsIgnoreCase("west")) {
            // West is most likely to be a path
            return switch (Cave.d5()) {
                case 1 -> new CaveFork(scan,0);
                case 2 -> new CaveDen(scan,0);
                default -> new CavePath(scan,0);
            };
        } else if (direction.equalsIgnoreCase("north")) {
            // North is most likely to be a fork
            return switch (Cave.d5()) {
                case 1 -> new CaveDen(scan,0);
                case 2 -> new CavePath(scan,0);
                default -> new CaveFork(scan,0);
            };
        } else return null;
    }
}
