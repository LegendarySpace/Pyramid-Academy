package HangingFromMyCuticle;

import java.awt.print.Printable;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Hangman {
    // common file names
    public static final String nameSaveFile = "src/main/resources/SavedGame.txt";
    public static final String nameHighScores = "src/main/resources/HighScores.txt";
    public static final String nameDictionary = "words_alpha.txt";
    public final Gallows gallows;
    public final String player;
    public Scanner scan;

    public ArrayList<String> wordBuffer;
    public String secretWord;
    public String lettersGuessed;
    public String lettersIncorrect;

    public Hangman(Scanner scanner, String name) {
        scan = scanner;
        player = name;
        gallows = new Gallows();
    }

    public boolean showMenu() {
        displayMenu();
        return processMenuSelection(inputMenu());
    }

    public void displayMenu() {
        System.out.println("Please choose an option: ");
        System.out.println("Start A New Game:    new");
        System.out.println("Continue Saved Game: continue");
        System.out.println("View High Scores:    score");
        System.out.println("Display Help Info:   help");
        System.out.println("Quit Game:           quit");
    }

    public String inputMenu() {
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

    public boolean processMenuSelection(String input) {
        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("new")) {
            boolean play = newGame();
            while (play) play = newGame();
            return false;
        } else if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("continue")) {
            boolean play = continueGame(loadGame());
            while (play) play = newGame();
            return false;
        } else if (input.equalsIgnoreCase("s") || input.equalsIgnoreCase("score")) {
            displayHighScores();
            return showMenu();
        } else if (input.equalsIgnoreCase("h") || input.equalsIgnoreCase("help")) {
            displayHelp();
            return showMenu();
        }
        return false;
    }

    public boolean newGame() {
        if (wordBuffer.size() < 2) loadWordBuffer();
        Collections.shuffle(wordBuffer);
        secretWord = wordBuffer.get(0);
        wordBuffer.remove(0);
        lettersGuessed = "";
        lettersIncorrect = "";
        return gameplayLoop();
    }

    public boolean continueGame(List<String> list) {
        if (list == null) return newGame();
        secretWord = decodeString(list.get(0));
        lettersGuessed = list.get(1);
        lettersIncorrect = list.get(2);
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

    public String inputGuessLetter() {
        final String quit = "quit";
        String result;
        try {
            do {
                System.out.println("Guess a letter");
                result = scan.next();
                if (result.equalsIgnoreCase("quit")) return quit;
                if (result.equalsIgnoreCase("save")) {
                    saveGame();
                    return quit;
                }
                if (result.equalsIgnoreCase("help")) {
                    displayHelp();
                    result = "";
                }
                if (result.length() > 1) result = Character.toString(result.charAt(0));
                if (lettersGuessed.contains(result.toUpperCase())) {
                    System.out.printf("You have already guessed %s.%n", result);
                    result = "";
                }
            } while (result.isEmpty() || !Character.isAlphabetic(result.charAt(0)));
            return result.toUpperCase();
        } catch (Exception e) {
            System.out.println("Error receiving Guessed Letter");
            e.printStackTrace();
        }
        return quit;
    }

    public boolean winMessage() {
        System.out.printf("Congratulations %s!!! You uncovered the word %s with %d chances remaining.%n", player, secretWord, gallows.chancesRemaining);
        return inputPlayAgain();
    }

    public boolean loseMessage() {
        System.out.printf("GAME OVER!!! Sorry %s, you guessed %d letters incorrectly.%n", player, Gallows.initialChances);
        return inputPlayAgain();
    }

    public boolean inputPlayAgain() {
        String replay;
        try {
            do {
                System.out.println("Would you like to play again?");
                replay = scan.next();
            } while (!replay.equalsIgnoreCase("y") && !replay.equalsIgnoreCase("yes") &&
                    !replay.equalsIgnoreCase("n") && !replay.equalsIgnoreCase("no") &&
                    !replay.equalsIgnoreCase("q") && !replay.equalsIgnoreCase("quit"));
            return replay.equalsIgnoreCase("y") || replay.equalsIgnoreCase("yes");
        } catch (Exception e) {
            System.out.println("Error receiving replay result");
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveGame() {
        File file = Paths.get(nameSaveFile).toFile();
        try {
            PrintWriter pw;
            List<String> saves;
            if (!file.exists()) {
                file.createNewFile();
                saves = List.of();
            } else {
                saves = Files.readAllLines(file.toPath());
                int start = saves.indexOf("NAME: " + player);
                if (start > -1) {
                    for (int i = 3; i >= 0; i--) saves.remove(start + i);
                }
            }
            pw = new PrintWriter(new FileWriter(file.getAbsoluteFile(), true));
            saves.addAll(List.of("NAME: " + player, encodeString(secretWord), lettersGuessed, lettersIncorrect));
            for (String str : saves) pw.println(str);
            pw.flush();
            pw.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error saving game");
            e.printStackTrace();
        }
        return false;
    }

    public List<String> loadGame() {
        File file = Paths.get(nameSaveFile).toFile();
        try {
            if (!file.exists()) return null;
            List<String> saves =  Files.readAllLines(file.toPath());
            int start = saves.indexOf("NAME: " + player);
            if (start < 0) return null;
            List<String> result = saves.subList(start + 1, start + 4);
            for (int i = 3; i >= 0; i--) saves.remove(start + i);
            PrintWriter pw = new PrintWriter(new FileWriter(file.getAbsoluteFile(), true));
            for (String str : saves) pw.println(str);
            pw.flush();
            pw.close();
            return result;
        } catch (Exception e) {
            System.out.println("Error loading save file");
            e.printStackTrace();
        }
        return null;
    }

    public void loadWordBuffer() {
        // TODO: Base min and max on difficulty
        int min = 2;
        int max = 20;
        try {
            URL url = getClass().getClassLoader().getResource(nameDictionary);
            Path p = Paths.get(url.toURI());
            List<String> words = Files.readAllLines(p).stream()
                    .filter(str -> str.length() > min && str.length() < max).distinct().toList();
            Collections.shuffle(words);
            wordBuffer.addAll(words.subList(0, 100));
        } catch (Exception e) {
            System.out.println("Error loading Dictionary");
            if (!(e instanceof URISyntaxException) && !(e instanceof NullPointerException)
                    && !(e instanceof IOException)) e.printStackTrace();
            wordBuffer.addAll(List.of("Hello", "Goodbye", "Fork", "Planet", "Exoplanet",
                    "Cosmology", "Physics", "Africa", "Neutron", "Proton", "Quark"));
        }
    }

    public String encodeString(String str) {    // Used to hide the secret words when saved to prevent exploit
        if (str.isEmpty()) return str;
        byte[] encode = str.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < encode.length; i++) {
            encode[i] -= 45;
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : encode) builder.append((char) b);
        return builder.toString();
    }

    public String decodeString(String str) {
        if (str.isEmpty()) return str;
        byte[] decode = str.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < decode.length; i++) {
            decode[i] -= 45;
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : decode) builder.append((char) b);
        return builder.toString();
    }
}
