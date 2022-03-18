package Guess.Number;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GuessTheNumber {
    public static Scanner scan;
    private int secretNumber;
    private int guessAttempts;
    private String name;

    public GuessTheNumber() {
        resetGame();
    }

    public static void main(String[] args) {
        GuessTheNumber gn = new GuessTheNumber();
        scan = new Scanner(System.in);

        gn.enterName();
        System.out.printf("Welcome %s. I'm thinking of a number between 1 and 20.%n", gn.getName());

        boolean correct = gn.play();

        if (correct) {
            System.out.printf("Congratulations %s, you guessed the correct number in %d attempts.%n", gn.getName(), gn.getAttempts());
        } else {
            System.out.printf("Sorry %s but you have run out of guesses. GAME OVER!!!%n", gn.getName());
        }

        scan.close();

    }

    protected void resetGame() {
        secretNumber = pickRandom();
        guessAttempts = 0;
    }

    public int getAttempts() { return guessAttempts; }

    public String getName() { return name; }

    public static int pickRandom() {
        return ThreadLocalRandom.current().nextInt(1, 20);
    }

    protected void enterName() {
        String message = "Please enter your name: ";
        String error = "Error getting player name";
        name = ensureInput(message, error);
    }

    public boolean play() {
        // Less than 6 previous guesses and haven't guessed correctly
        if (guessAttempts <= 6 && !didYouWin(compareGuess(secretNumber, makeGuess()))) return play();
        return guessAttempts <= 6;
    }

    protected int makeGuess() {
        String message = "Please enter a number between 1 and 20: ";
        String error = "Error getting player guess";
        int guess = ensureIntInput(message, error);
        if (!isInRange(guess)) return makeGuess();
        guessAttempts++;
        return guess;
    }

    protected boolean isInRange(int num) {
        if (1 <= num && num <= 20) return true;
        System.out.println("Guess was out of range!");
        return false;
    }

    public String compareGuess(int answer, int guess) {
        if (guess > answer) return "high";
        if (guess < answer) return "low";
        else return "equal";
    }

    public boolean didYouWin(String value) {
        if (value.equalsIgnoreCase("equal")) {
            System.out.println("You guessed correctly!!");
            return true;
        } else if (value.equalsIgnoreCase("high")) {
            System.out.println("Your guess is too high.");
            return false;
        } else if (value.equalsIgnoreCase("low")) {
            System.out.println("Your guess is too low.");
            return false;
        }
        System.out.println("Invalid guess");
        return false;
    }

    public static int ensureIntInput(String inputMessage, String errorMessage) {
        try {
            System.out.print("%n" + inputMessage);
            return Integer.parseInt(scan.next());
        } catch (NumberFormatException e) {
            System.out.print("%n" + errorMessage);
            return ensureIntInput(inputMessage, errorMessage);
        }
    }

    public static String ensureInput(String inputMessage, String errorMessage) {
        try {
            System.out.println(inputMessage);
            return scan.next();
        } catch (Exception e) {
            System.out.println(errorMessage);
            return ensureInput(inputMessage, errorMessage);
        }
    }
}
