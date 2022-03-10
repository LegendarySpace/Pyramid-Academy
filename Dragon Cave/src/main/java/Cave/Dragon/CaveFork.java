package Cave.Dragon;

import java.util.Scanner;

public class CaveFork extends Cave{
    Cave left;
    Cave right;
    public CaveFork() {
        if (dist < 3) {
            left = switch (d10()) {
                case 1 -> new CaveFork();
                case 2,3,4,5 -> new CavePath();
                default -> new CaveDen();
            };
            right = switch (d10()) {
                case 1 -> new CaveFork();
                case 2,3,4,5 -> new CavePath();
                default -> new CaveDen();
            };
        } else {
            left = new CaveDen();
            right = new CaveDen();
        }
    }

    @Override
    public void explore() {
        dist++;
        System.out.println("As you make your way through the cave it suddenly widens and you encounter a forking path");
        Scanner scan = new Scanner(System.in);
        String direction;
        do {
            System.out.println("Which direction do you go? (Left, Right)");
            direction = scan.next();
        } while (!direction.equalsIgnoreCase("left") && !direction.equalsIgnoreCase("right"));
        scan.close();

        if (direction.equalsIgnoreCase("left")) left.explore();
        else right.explore();
    }
}
