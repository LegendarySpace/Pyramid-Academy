package HangTheMan;

import java.util.Scanner;

public class Window {
    public static String name;
    public static void main(String[] args) {
        System.out.println(" LET'S PLAY HANGMAN ");
        System.out.println("====================");
        Scanner scan = new Scanner(System.in);
        name = ensureName(scan, "Please enter your name: ", "Error getting user name!!");
        Hangman game = new Hangman(scan, name);

        System.out.printf("%nWelcome %s, have a wonderful time playing Hangman%n", name);
        boolean play;
        do {
            play = game.showMenu();
        } while (play);

        scan.close();
    }

    public static String ensureName(Scanner scan, String message, String error) {
        String result = "";
        try {
            System.out.print("%n"+message);
            result = scan.nextLine();
            return result;
        } catch (Exception e) {
            System.out.println("%n"+error);
            e.printStackTrace();
            ensureName(scan, message, error);
        }
        return result;
    }
}
