package HangTheMan;

import java.util.Scanner;
import javax.swing.*;

public class Window {
    public JFrame frame;
    public ImageIcon background;
    public JLabel backgroundDetails;

    public static String name;

    public Window() {
        background = new ImageIcon(this.getClass().getResource("/Hangman0.jpg"));
        backgroundDetails = new JLabel(background);
        backgroundDetails.setSize(960, 540);//(1920, 1080)

        frame = new JFrame("Hang The Man");
        frame.add(backgroundDetails);
        frame.setSize(978, 581);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Window win = new Window();
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
