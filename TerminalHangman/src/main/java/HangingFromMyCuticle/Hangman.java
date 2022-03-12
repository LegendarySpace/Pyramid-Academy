package HangingFromMyCuticle;

import java.util.Scanner;

public class Hangman {
    // common file names
    public static final String nameSaveFile = "SavedGame.txt";
    public static final String nameHighScores = "HighScores.txt";
    public final String player;
    public Scanner scan;
    public String secretWord;
    public String lettersGuessed;
    public String lettersIncorrect;
    public Gallows gallows;

    public Hangman(Scanner scanner, String name) {
        scan = scanner;
        player = name;
        gallows = new Gallows();
    }

    public boolean showMenu() {
        displayMenu();
        return processMenuInput(getMenuInput());
    }

    public void displayMenu() {
        System.out.println("Please choose an option: ");
        System.out.println("Start A New Game:    new");
        System.out.println("Continue Saved Game: continue");
        System.out.println("View High Scores:    score");
        System.out.println("Display Help Info:   help");
        System.out.println("Quit Game:           quit");
    }

    public String getMenuInput() {
        String result;
        try {
            do {
                result = scan.next();
            } while (!result.equalsIgnoreCase("n") && !result.equalsIgnoreCase("new") &&
                    !result.equalsIgnoreCase("c") && !result.equalsIgnoreCase("continue") &&
                    !result.equalsIgnoreCase("s") && !result.equalsIgnoreCase("score") &&
                    !result.equalsIgnoreCase("h") && !result.equalsIgnoreCase("help") &&
                    !result.equalsIgnoreCase("q") && !result.equalsIgnoreCase("quit"));
            // TODO: if (result == continue & !saveFile.exists()) result = new;
            return result;
        } catch (Exception e) {
            System.out.println("Error receiving menu input");
            e.printStackTrace();
        }
        return "quit";
    }

    public boolean processMenuInput(String input) {
        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("new")) {
            boolean play = newGame();
            while (play) play = newGame();
            return false;
        } else if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("continue")) {
            boolean play = continueGame();
            while (play) play = newGame();
            return false;
        } else if (input.equalsIgnoreCase("s") || input.equalsIgnoreCase("score")) {
            displayHighScores();
            return showMenu();
        } else if (input.equalsIgnoreCase("h") || input.equalsIgnoreCase("help")) {
            displayHelp();
            return showMenu();
        } else {
            return false;
        }
    }

    public boolean newGame() {
        // TODO: Initialize game
        return gameplayLoop();
    }

    public boolean continueGame() {
        // TODO: Load game
        return gameplayLoop();
    }

    public void displayHighScores() {
        // TODO: load and display high scores
        System.out.println("Press Enter to return to Menu");
        scan.next();
    }

    public void displayHelp() {
        // TODO: Display help information
        System.out.println("Press Enter to return to Menu");
        scan.next();
    }

    public boolean gameplayLoop() {
        // TODO: exit conditions are win, loss or enter quit(return false)
        return  winMessage();
    }

    public boolean winMessage() {
        System.out.printf("Congratulations %s!!! You uncovered the word %s with %d chances remaining.%n", player, secretWord, gallows.chancesRemaining);
        return playAgain();
    }

    public boolean loseMessage() {
        System.out.printf("GAME OVER!!! Sorry %s, you guessed %d letters incorrectly.%n", player, Gallows.initialChances);
        return playAgain();
    }

    public boolean playAgain() {
        String replay;
        try {
            do {
                System.out.println("Would you like to play again?");
                replay = scan.next();
            } while (!replay.equalsIgnoreCase("y") && !replay.equalsIgnoreCase("yes") &&
                    !replay.equalsIgnoreCase("n") && !replay.equalsIgnoreCase("no"));
            return replay.equalsIgnoreCase("y") || replay.equalsIgnoreCase("yes");
        } catch (Exception e) {
            System.out.println("Error receiving replay result");
            e.printStackTrace();
        }
        return false;
    }
}
