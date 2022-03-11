package Cave.Dragon;

import java.util.Scanner;

public class CaveFork extends Cave{
    Cave left;
    Cave right;
    public CaveFork(Scanner scanner, int dist) {
        scan = scanner;
        if (dist++ < 3) {
            left = switch (d10()) {
                case 1 -> new CaveFork(scan, dist);
                case 2,3,4,5 -> new CavePath(scan, dist);
                default -> new CaveDen(scan, dist);
            };
            right = switch (d10()) {
                case 1 -> new CaveFork(scan, dist);
                case 2,3,4,5 -> new CavePath(scan, dist);
                default -> new CaveDen(scan, dist);
            };
        } else {
            left = new CaveDen(scan, dist);
            right = new CaveDen(scan, dist);
        }
    }

    @Override
    public void explore() {
        System.out.println();
        System.out.println("As you make your way through the cave it suddenly widens and you encounter a forking path");

        if (leftOrRight().equalsIgnoreCase("left")) left.explore();
        else right.explore();
    }

    public String leftOrRight() {
        try {
            String direction;
            do {
                System.out.println("Which direction do you go? (Left, Right)");
                direction = scan.next();
            } while (!direction.equalsIgnoreCase("left") && !direction.equalsIgnoreCase("right"));
            return direction;
        } catch (Exception e) {
            System.out.println("Error getting user choice!!");
            e.printStackTrace();
        }
        return null;
    }
}
