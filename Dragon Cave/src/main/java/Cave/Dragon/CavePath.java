package Cave.Dragon;

import java.util.Scanner;

public class CavePath extends Cave{
    Cave connection;
    public CavePath(Scanner scanner, int dist) {
        scan = scanner;
        if (dist++ < 3) {
            switch (d10()) {
                case 1, 2, 3, 4 -> connection = new CaveFork(scan, dist);     // 40% chance to spawn fork
                default -> connection = new CaveDen(scan, dist);
            }
        } else {
            connection = new CaveDen(scan, dist);
        }
    }

    @Override
    public void explore() {
        System.out.println();
        System.out.println("Ahead is a long dark corridor. A sense of unease washes over you. If you leave now you can still make it home.");
        String decision;
        do {
            System.out.println("Do you want to continue your journey? (y or n)");
            decision = scan.next();
            if (decision.length() > 1) decision = Character.toString(decision.charAt(0));
        } while (!decision.equalsIgnoreCase("y") && !decision.equalsIgnoreCase("n"));
        if (decision.equalsIgnoreCase("n")) return;

        System.out.println("You continue down a long dark corridor. You can barely see and must move slowly.");
        System.out.println("As you make your way down the twisting path there's an occasional rumble");
        if (d20() == 1) encounter();        // 5% chance auto game over
        else connection.explore();
    }

    public void encounter() {
        System.out.println("You encounter a wandering dragon and it devours you before you can react. GAME OVER!!!!");
    }
}
