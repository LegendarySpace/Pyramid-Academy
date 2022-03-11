package Guess.Number;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GuessTheNumber {
    public static Scanner scan;
    public static int secretNumber;
    public static int guessAttempts;
    public static String name;

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        guessAttempts = 0;
        secretNumber = pickRandom();

        enterName();
        System.out.printf("Welcome %s. I'm thinking of a number between 1 and 20.%n", name);

        boolean correct;
        do {
            correct = output(compareGuess(makeGuess()));
        } while (!correct && guessAttempts <= 6);
        scan.close();

        if (correct) {
            System.out.printf("Congratulations %s, you guessed the correct number in %d attempts.%n", name, guessAttempts);
        } else {
            System.out.printf("Sorry %s but you have run out of guesses. GAME OVER!!!%n", name);
        }

    }

    public static int pickRandom() {
        return ThreadLocalRandom.current().nextInt(1, 20);
    }

    public static void enterName() {
        try {
            System.out.print("Please enter your name: ");
            name = scan.next();
        } catch (Exception e) {
            System.out.println("Error getting player name");
            e.printStackTrace();
        }
    }

    public static int makeGuess() {
        try {
            guessAttempts++;
            int guess;
            do {
                System.out.print("Please enter a number between 1 and 20: ");
                guess = scan.nextInt();
            } while (guess < 1 || guess > 20);
            return guess;
        } catch (Exception e) {
            System.out.println("Error getting player guess");
            if (!(e instanceof InputMismatchException)) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static String compareGuess(int guess) {
        if (guess == 0) return "invalid";
        if (guess > secretNumber) return "high";
        if (guess < secretNumber) return "low";
        else return "equal";
    }

    public static boolean output(String value) {
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
        return false;
    }
}
