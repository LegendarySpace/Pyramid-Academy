package HangingFromMyCuticle;

import java.util.Scanner;

public class Hangman {
    // common file names
    public static final String nameSaveFile = "SavedGame.txt";
    public static final String nameHighScores = "HighScores.txt";
    public Scanner scan;
    public String secretWord;
    public String lettersGuessed;
    public String lettersIncorrect;
    public Gallows gallows;

    public Hangman(Scanner scanner) {
        scan = scanner;
        gallows = new Gallows();
    }

    public void displayMenu() {
    }

    public String getMenuInput() {
        return null;
    }

    public void newGame() {
    }

    public void continueGame() {
    }

    public void displayHighScores() {
    }

    public void displayHelp() {
    }

    public void winMessage() {
    }

    public void loseMessage() {
    }

    public boolean playAgain() {
        return false;
    }
}
