package HangingFromMyCuticle;

import java.util.Scanner;

public class Entry {
    public static String name;
    public static void main(String[] args) {
        System.out.println(" LET'S PLAY HANGMAN ");
        System.out.println("====================");
        System.out.println();
        System.out.print("Please enter your name:  ");
        Scanner scan = new Scanner(System.in);
        name = scan.nextLine();

        System.out.printf("Welcome %s, have a wonderful time playing Hangman%n", name);

        Hangman game = new Hangman(scan, name);
        boolean play;
        do {
            play = game.showMenu();
        } while (play);

        scan.close();
    }
}
