package Cave.Dragon;

import java.util.Scanner;

public class DragonCave {
    public static String mtn = "Smaugenraug Mountains";

    public static void main(String[] args) {
        Cave.scan = new Scanner(System.in);
        DragonCave dc = new DragonCave();
        dc.introduction();

        Cave entrance;
        do {
            entrance = dc.determineCave(dc.approachMountain());
        } while (entrance == null);
        entrance.explore();

        dc.outro();
        Cave.scan.close();
    }

    private void introduction() {
        System.out.println("           DRAGON CAVE");
        System.out.println("        Adventure awaits");
        System.out.println("================================");
        System.out.println();
        System.out.println("You're an adventurer from a town in the woods of honey searching for excitement and treasure.");
        System.out.printf("One day while out exploring you decide to venture to the %s in search of fortune and fame.%n", mtn);
        System.out.printf("All Adventurers know %s is a breeding ground for dragons, and where there are dragons there's treasure.%n", mtn);
        System.out.println("There's 2 types of dragons: kind dragons who share their treasure, and vicious dragons who devour all they encounter.");
    }

    private void outro() {
        System.out.println();
        System.out.printf("A fitting end to your journey in %s, but things are just beginning for many other adventurers.%n", mtn);
        System.out.printf("Come back again to hear more tales from the %s.%n", mtn);
    }

    protected String approachMountain() {
        String message = "Which side of the mountain do you decide to approach? (East, West, North)";
        String error = "Error getting user input!!!";
        String enter = Cave.ensureInput(message, error);
        if (eastWestNorth(enter)) return enter;
        return approachMountain();
    }

    protected boolean eastWestNorth(String direction) {
        if (direction == null || direction.isEmpty() || !(direction.equalsIgnoreCase("east") ||
                direction.equalsIgnoreCase("west") || direction.equalsIgnoreCase("north"))) {
            System.out.println("You did not choose East, West, or North");
            return false;
        }
        return true;
    }

    protected Cave determineCave(String direction) {
        if (direction == null || direction.isEmpty()) return null;
        if (direction.equalsIgnoreCase("east")) {
            // East is most likely to be a den
            return switch (Cave.d5()) {
                case 1 -> new CaveFork(0);
                case 2 -> new CavePath(0);
                default -> new CaveDen(0);
            };
        } else if (direction.equalsIgnoreCase("west")) {
            // West is most likely to be a path
            return switch (Cave.d5()) {
                case 1 -> new CaveFork(0);
                case 2 -> new CaveDen(0);
                default -> new CavePath(0);
            };
        } else if (direction.equalsIgnoreCase("north")) {
            // North is most likely to be a fork
            return switch (Cave.d5()) {
                case 1 -> new CaveDen(0);
                case 2 -> new CavePath(0);
                default -> new CaveFork(0);
            };
        } else return null;
    }
}
