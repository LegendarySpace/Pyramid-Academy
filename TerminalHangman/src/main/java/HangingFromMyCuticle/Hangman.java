package HangingFromMyCuticle;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Hangman {
    // common file names
    public static final String nameDictionary = "words_alpha.txt";
    public static final String nameSaveFile = "src/main/resources/SavedGame.txt";
    public static final String nameHighScores = "src/main/resources/HighScores.txt";
    public static final int scoreSheetSize = 10;
    public final Gallows gallows;
    public final String player;
    public final int pointsPerGuess = 10;
    public final int pointsLostPerIncorrect = 5;
    public Scanner scan;

    public ArrayList<String> wordBuffer;
    public String secretWord;
    public String lettersGuessed;
    public String lettersIncorrect;

    public Hangman(Scanner scanner, String name) {
        scan = scanner;
        player = name;
        gallows = new Gallows();
        wordBuffer = new ArrayList<>();
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
            return result;
        } catch (Exception e) {
            System.out.println("Error receiving menu input");
            e.printStackTrace();
        }
        return "quit";
    }

    public boolean processMenuSelection(String input) {
        if (input == null || input.isEmpty()) return false;
        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("new")) {
            boolean play;
            do play = newGame();
            while (play);
            return false;
        } else if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("continue")) {
            boolean play;
            List<String> game = loadGame(nameSaveFile);
            if (game != null) play = continueGame(game);
            else play = newGame();
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
        if (wordBuffer.size() < 2) loadWordByDifficulty();
        Collections.shuffle(wordBuffer);
        secretWord = wordBuffer.get(0).toUpperCase();
        wordBuffer.remove(0);
        lettersGuessed = "";
        lettersIncorrect = "";
        gallows.chancesRemaining = Gallows.initialChances;
        return gameplayLoop();
    }

    public boolean continueGame(List<String> list) {
        if (list == null) return newGame();
        secretWord = decodeString(list.get(0));
        lettersGuessed = list.get(1);
        lettersIncorrect = list.get(2);
        gallows.chancesRemaining = Gallows.initialChances - lettersIncorrect.length();
        return gameplayLoop();
    }

    public void displayHighScores() {
        List<String> highScores = loadScores(nameHighScores);
        int idx;
        for (int i = 0; i < scoreSheetSize; i++) {
            idx = i * 3;
            if (highScores != null && idx < highScores.size()) System.out.printf("%d: %03d - %s at %s%n", i,
                    Integer.parseInt(highScores.get(idx+1)), highScores.get(idx), highScores.get(idx+2));
            else System.out.printf("%d:%n", i);
        }
        System.out.println("Press Enter to return to Menu");
        scan.next();
    }

    public void displayHelp() {
        System.out.println("A secret word will be generated at random and an underscore will be placed in each letter's position."); // Game Rules
        System.out.println("Each turn you will be prompted to guess a letter. If the secret word contains the letter all occurrences will be uncovered");
        System.out.println("If  the letter you guess is not contained it will be added to the list of incorrect letter and a body part will be added to the hangman");
        System.out.println();
        System.out.printf("If you make %d incorrect guesses the hangman's body will be completed and the game will be over%n", Gallows.initialChances);
        System.out.println("If you can discover the word before using up all your chances you win the game");
        System.out.println();
        System.out.printf("You gain %d points for each letter you uncover and lose %d points for each letter you guess wrong%n", pointsPerGuess, pointsLostPerIncorrect);
        System.out.println();
        System.out.println("At any time you can save your game to resume later by typing save, or exit the game by typing quit");
        System.out.println("Have a great time playing");
        System.out.println("Press Enter to return to Menu");
        scan.next();
    }

    public boolean gameplayLoop() {
        int charactersRemaining = secretWord.replaceAll("[ "+lettersGuessed+"]", "").length();
        String guess;
        displayGallows();
        while (charactersRemaining > 0 && gallows.chancesRemaining > 0) {   // secret word not revealed and chances remaining
            guess = inputGuessLetter();
            if (guess.equalsIgnoreCase("quit")) return false;
            checkLetterContained(guess);
            charactersRemaining = secretWord.replaceAll("[ "+lettersGuessed+"]", "").length();
        }
        if (gallows.chancesRemaining <= 0) return loseMessage();
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
                    saveGame(nameSaveFile);
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

    public void checkLetterContained(String letter) {
        lettersGuessed += letter;
        if (!secretWord.contains(letter)) {
            gallows.chancesRemaining--;
            lettersIncorrect += letter;
        }
        displayGallows();
    }

    public void displayGallows() {
        gallows.buildGallows();
        String hiddenWord = secretWord.replaceAll("[^ "+lettersGuessed+"]", "_");
        System.out.printf("%s%s%n"," ".repeat((20-secretWord.length())/2), hiddenWord);
        System.out.println();
        System.out.printf("%s%s%n"," ".repeat((20-lettersIncorrect.length())/2-2),lettersIncorrect);
        System.out.printf("You have %d chances remaining%n", gallows.chancesRemaining);
        System.out.println();
    }

    public boolean winMessage() {
        System.out.printf("Congratulations %s!!! You uncovered the word %s with %d chances remaining.%n", player, secretWord, gallows.chancesRemaining);
        updateScores(nameHighScores);
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

    public boolean saveGame(String saveFile) {
        File file = Paths.get(saveFile).toFile();
        try {
            PrintWriter pw;
            ArrayList<String> saves;
            if (!file.exists()) {
                file.createNewFile();
                saves = new ArrayList<>();
            } else {
                saves = new ArrayList<>(Files.readAllLines(file.toPath()));
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

    public List<String> loadGame(String saveFile) {
        File file = Paths.get(saveFile).toFile();
        try {
            if (!file.exists()) return null;
            ArrayList<String> saves =  new ArrayList<>(Files.readAllLines(file.toPath()));
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

    public void loadWordByDifficulty() {
        wordBuffer.clear();
        System.out.println();
        String diff;
        do {
            System.out.println("Please choose a difficulty: ANY, EASY, NORMAL, HARD, CHALLENGING");
            diff = scan.next();
        } while (!(diff.equalsIgnoreCase("a") || diff.equalsIgnoreCase("any") ||
                diff.equalsIgnoreCase("e") || diff.equalsIgnoreCase("easy") ||
                diff.equalsIgnoreCase("n") || diff.equalsIgnoreCase("normal") ||
                diff.equalsIgnoreCase("h") || diff.equalsIgnoreCase("hard") ||
                diff.equalsIgnoreCase("c") || diff.equalsIgnoreCase("challenging")));

        if (diff.equalsIgnoreCase("e") || diff.equalsIgnoreCase("easy")) {
            loadWordBuffer(3, 6, 2, 6);
        } else if (diff.equalsIgnoreCase("n") || diff.equalsIgnoreCase("normal")) {
            loadWordBuffer(6, 10, 5, 10);
        } else if (diff.equalsIgnoreCase("h") || diff.equalsIgnoreCase("hard")) {
            loadWordBuffer(10, 15, 7, 15);
        } else if (diff.equalsIgnoreCase("c") || diff.equalsIgnoreCase("challenging")) {
            loadWordBuffer(15, 20, 10, 17);
        } else loadWordBuffer(3,20,3,20);
    }

    public void loadWordBuffer(int min, int max, int uniqMin, int uniqMax) {
        try {
            URL url = getClass().getClassLoader().getResource(nameDictionary);
            Path p = Paths.get(url.toURI());
            ArrayList<String> words = Files.readAllLines(p).stream()
                    .filter(str -> str.length() > min && str.length() < max)
                    .filter(str -> {
                        int i = (int) str.toLowerCase().chars().distinct().count();
                        return i > uniqMin && i < uniqMax;
                    }).distinct().collect(Collectors.toCollection(ArrayList::new));
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

    public List<String> loadScores(String scoreFile) {
        File file = Paths.get(scoreFile).toFile();
        try {
            if (!file.exists()) return null;
            return Files.readAllLines(file.toPath());
        } catch (Exception e) {
            System.out.println("Error loading High Scores");
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateScores(String scoreFile) {
        File file = Paths.get(scoreFile).toFile();
        try {
            ArrayList<String> scoreSheet;
            if (!file.exists()) {
                file.createNewFile();
                scoreSheet = new ArrayList<>();
            } else scoreSheet = new ArrayList<>(Files.readAllLines(file.toPath()));
            addScoreToList(scoreSheet);
            ReduceScoresSheet(scoreSheet);
            PrintWriter pw = new PrintWriter(new FileWriter(file.getAbsoluteFile(), false));
            for (String str : scoreSheet) pw.println(str);
            pw.flush();
            pw.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error updating High Scores");
            if (!(e instanceof UnsupportedOperationException)) e.printStackTrace();
        }
        return false;
    }

    public void ReduceScoresSheet(ArrayList<String> scores) {
        if (scores == null) return;
        int sheetLimit = 3 * scoreSheetSize;
        while (scores.size() > sheetLimit) for (int i = 2; i >= 0; i--) scores.remove(sheetLimit);
    }

    public void addScoreToList(ArrayList<String> scores) {
        // Each record is 3 lines: name, score, date
        if (scores == null) return;
        int myScore = calculateScore();
        List<String> record = List.of(player, Integer.toString(myScore), LocalDate.now().toString());
        boolean added = false;
        for (int i = 0; i < scores.size(); i += 3) {
            if (Integer.parseInt(scores.get(i+1)) < myScore) {
                scores.addAll(i, record);
                added = true;
                break;
            }
        }
        if (!added) scores.addAll(record);
    }

    public int calculateScore() {
        String str = secretWord.replaceAll("[^ "+lettersGuessed+"]", ""); // Remove any letters not guessed
        return (str.length() * pointsPerGuess) - (lettersIncorrect.length() * pointsLostPerIncorrect);
    }

    public String encodeString(String str) {    // Used to hide the secret words when saved to prevent exploit
        if (str == null || str.isEmpty()) return str;
        byte[] encode = str.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < encode.length; i++) {
            encode[i] -= 45;
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : encode) builder.append((char) b);
        return builder.toString();
    }

    public String decodeString(String str) {
        if (str == null || str.isEmpty()) return str;
        byte[] decode = str.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < decode.length; i++) {
            decode[i] -= 45;
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : decode) builder.append((char) b);
        return builder.toString();
    }
}
