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
        name = scan.next();

        System.out.printf("Welcome %s, have a wonderful time playing Hangman", name);

        Hangman game = new Hangman(scan);
        game.newGame();

        scan.close();
    }
}
