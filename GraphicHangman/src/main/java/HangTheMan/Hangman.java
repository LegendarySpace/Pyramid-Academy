package HangTheMan;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Hangman {
    // common file names
    public static final String nameDictionary = "words_alpha.txt";
    public static final String nameHighScores = "src/main/resources/HighScores.txt";
    public static final String nameSaveFile = "src/main/resources/SavedGame.txt";
    public static final int scoreSheetSize = 10;

    public final int pointsLostPerIncorrect = 5;
    public final int pointsPerGuess = 10;
    public final Gallows gallows;
    public final String player;

    public ArrayList<String> wordBuffer;
    public Scanner scan;

    public Hangman(Scanner scanner, String name) {
        scan = scanner;
        player = name;
        gallows = new Gallows();
        wordBuffer = new ArrayList<>();
    }

    public boolean showMenu() { return processMenuSelection(inputMenu()); }

    protected String inputMenu() {
        String error = "Error receiving menu input";
        displayMenu();
        String option = ensureInput("", error);
        if (validateMenuOption(option)) return option;
        return inputMenu();
    }

    private void displayMenu() {
        System.out.println("Please choose an option: ");
        System.out.println("Start A New Game:    new");
        System.out.println("Continue Saved Game: continue");
        System.out.println("View High Scores:    score");
        System.out.println("Display Help Info:   help");
        System.out.println("Quit Game:           quit");
    }

    protected boolean validateMenuOption(String option) {
        if (option == null || option.isEmpty()) return false;
        if (option.equalsIgnoreCase("n") || option.equalsIgnoreCase("new") ||
                option.equalsIgnoreCase("c") || option.equalsIgnoreCase("continue") ||
                option.equalsIgnoreCase("s") || option.equalsIgnoreCase("score") ||
                option.equalsIgnoreCase("h") || option.equalsIgnoreCase("help") ||
                option.equalsIgnoreCase("q") || option.equalsIgnoreCase("quit")) return true;
        System.out.println("Not a valid menu choice");
        return false;
    }

    protected boolean processMenuSelection(String input) {
        if (input == null || input.isEmpty()) return false;
        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("new")) {
            if (newGame()) return processMenuSelection("n");
            return false;
        } else if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("continue")) {
            boolean play;
            List<String> game = loadGame(nameSaveFile);
            if (continueGame(game)) return processMenuSelection("n");
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

    private boolean newGame() {
        if (wordBuffer.size() < 2) inputLoadWordByDifficulty();
        Collections.shuffle(wordBuffer);
        String secret = wordBuffer.get(0).toUpperCase();
        wordBuffer.remove(0);
        gallows.reset();
        return gameplayLoop(secret, "", "");
    }

    private boolean continueGame(List<String> list) {
        if (list == null || list.size() < 3) return newGame();
        gallows.reset(list.get(2));
        return gameplayLoop(decodeString(list.get(0)), list.get(1), list.get(2));
    }

    private void displayHighScores() {
        List<String> highScores = loadScores(nameHighScores);
        printHighScore(highScores, 0);
        ensureInputLine("Press Enter to return to Menu", "Please enter any button");
    }

    private void printHighScore(List<String> highScores, int iter) {
        int idx = iter * 3;
        if (highScores != null && idx < highScores.size()) System.out.printf("%d: %03d - %s at %s%n", iter,
                Integer.parseInt(highScores.get(idx+1)), highScores.get(idx), highScores.get(idx+2));
        else System.out.printf("%d:%n", iter);
        if (iter < scoreSheetSize) printHighScore(highScores, iter+1);
    }

    private void displayHelp() {
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
        ensureInputLine("Press Enter to return to Menu", "Please enter any button");
    }

    protected boolean gameplayLoop(String secret, String attempted, String incorrect) {
        gallows.buildGallows(getHiddenWord(secret, attempted), incorrect);
        attempted = letterFetch(secret, attempted, incorrect);
        if (!gallows.hasChances()) return loseMessage(player);
        return  winMessage(player, secret, gallows.getChancesRemaining());
    }

    protected String letterFetch(String secret, String attempted, String incorrect) {
        String guess = ensureInput("Guess a letter", "Error receiving Guessed Letter").toUpperCase();
        if (isKeyword(guess, secret, attempted, incorrect)) return letterFetch(secret, attempted, incorrect);
        if (!validateLetter(guess, attempted)) return letterFetch(secret, attempted, incorrect);
        attempted += guess.charAt(0);
        incorrect = checkLetterContained(guess.charAt(0), secret, attempted);
        gallows.buildGallows(getHiddenWord(secret, attempted), incorrect);
        if (getHiddenCharCount(secret, attempted) > 0 && gallows.hasChances()) return letterFetch(secret, attempted, incorrect);
        return attempted;
    }

    protected boolean isKeyword(String word, String secret, String guessed, String incorrect) {
        if (word == null || word.isEmpty()) return false;
        if (word.equalsIgnoreCase("quit")) exit();
        if (word.equalsIgnoreCase("save")) {
            saveGame(nameSaveFile, secret, guessed, incorrect);
            exit();
        }
        if (word.equalsIgnoreCase("help")) {
            displayHelp();
            return true;
        }
        return false;
    }

    protected boolean validateLetter(String word, String guessed) {
        if (word == null || word.isEmpty() || !Character.isAlphabetic(word.charAt(0))) {
            System.out.println("That was not a valid guess, please try again");
            return false;
        }
        word = word.toUpperCase();        // Ensure all same case
        guessed = guessed.toUpperCase();
        String letter = Character.toString(word.charAt(0));
        if (guessed.contains(letter)) {
            System.out.printf("The letter %s was previously guessed, guess another letter%n", letter);
            return false;
        }
        return true;
    }

    protected String checkLetterContained(char letter, String secret, String incorrect) {
        if (secret == null) return incorrect;
        secret = secret.replaceAll("[ ]", "").toUpperCase();    // Remove any whitespace before testing
        String l = Character.toString(letter).replaceAll("[ ]", "").toUpperCase();
        if (secret.isEmpty() || l.isEmpty()) return incorrect;
        if (!secret.contains(l)) {
            gallows.decrementChances();
            return incorrect += letter;
        }
        return incorrect;
    }

    protected String getHiddenWord(String secret, String guessed) {
        if (secret == null || guessed == null || secret.isEmpty()) return "";
        String s = secret.replaceAll("[^a-zA-Z0-9]", "");
        return s.replaceAll("[^ "+guessed.toUpperCase()+guessed.toLowerCase()+"]", "_");
    }

    protected int getHiddenCharCount(String secret, String guessed) {
        if (secret == null || guessed == null || secret.isEmpty()) return 0;
        String s = secret.replaceAll("[^a-zA-Z0-9]", "");
        return s.toUpperCase().replaceAll("[ "+guessed.toUpperCase()+guessed.toLowerCase()+"]", "").length();
    }

    protected int getHiddenCharCount(String hidden) {
        if (hidden == null || hidden.isEmpty()) return 0;
        return hidden.replaceAll("[^_]", "").length();
    }

    protected int getRevealedCharCount(String secret, String guessed) {
        if (secret == null || guessed == null || secret.isEmpty()) return 0;
        String s = secret.replaceAll("[^a-zA-Z0-9]", "");
        return s.toUpperCase().replaceAll("[^ "+guessed.toUpperCase()+guessed.toLowerCase()+"]", "").length();
    }

    protected int getRevealedCharCount(String hidden) {
        if (hidden == null || hidden.isEmpty()) return 0;
        return hidden.replaceAll("[_]", "").length();
    }

    private boolean winMessage(String name, String word, int lives) {
        System.out.printf("Congratulations %s!!! You uncovered the word %s with %d chances remaining.%n", name, word, lives);
        int myScore = calculateScore(word, lives);
        updateScores(nameHighScores, myScore);
        return inputPlayAgain();
    }

    private boolean loseMessage(String name) {  // TODO: Should update score if score is positive
        System.out.printf("GAME OVER!!! Sorry %s, you guessed %d letters incorrectly.%n", player, Gallows.initialChances);
        return inputPlayAgain();
    }

    protected boolean inputPlayAgain() {
        String response = ensureInput("Would you like to play again", "Error receiving replay result");
        if (!validPlayAgain(response)) inputPlayAgain();
        return response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes");
    }

    protected boolean validPlayAgain(String word) {
        if (word == null || word.isEmpty() || !Character.isAlphabetic(word.charAt(0))) {
            System.out.println("That was not a valid response, please try again");
            return false;
        }
        if (word.equalsIgnoreCase("q") || word.equalsIgnoreCase("quit")) exit();
        if (!(word.equalsIgnoreCase("y") || word.equalsIgnoreCase("yes") ||
                word.equalsIgnoreCase("n") || word.equalsIgnoreCase("no"))) {
            System.out.printf("%s is not a valid response", word);
            return false;
        }
        return true;
    }

    protected int calculateScore(String hidden, int lives) {
        return (getRevealedCharCount(hidden) * pointsPerGuess) - ((Gallows.initialChances - lives) * pointsLostPerIncorrect);
    }

    protected boolean saveGame(String saveFile, String secret, String guessed, String incorrect) {
        List<String> saveDetails = List.of("NAME: " + player, encodeString(secret), guessed, incorrect);
        try {
            File file = Paths.get(saveFile).toFile();
            if (!file.exists()) {   // new Save file
                Files.write(file.toPath(), saveDetails, StandardOpenOption.CREATE);
            } else {
                ArrayList<String> saves = new ArrayList<>(Files.readAllLines(file.toPath()));
                int start = saves.indexOf("NAME: " + player);
                if (start > -1) {   // Overwrite previous save details
                    saves.remove(start + 3);
                    saves.remove(start + 2);
                    saves.remove(start + 1);
                    saves.remove(start);
                    saves.addAll(saveDetails);
                    Files.write(file.toPath(), saves, StandardOpenOption.CREATE);
                } else {    // Add save details to file
                    Files.write(file.toPath(), saveDetails, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error saving game");
            e.printStackTrace();
            return false;
        }
    }

    protected List<String> loadGame(String saveFile) {
        try {
            File file = Paths.get(saveFile).toFile();
            if (!file.exists()) return null;
            ArrayList<String> saves =  new ArrayList<>(Files.readAllLines(file.toPath()));
            int start = saves.indexOf("NAME: " + player);
            if (start < 0) return null;
            // sublist reflects changes to list, need an immutable list instead
            saves.set(start + 1, decodeString(saves.get(start + 1)));
            List<String> result = Arrays.asList(saves.subList(start + 1, start + 4).toArray(new String[0]));
            saves.remove(start + 3);
            saves.remove(start + 2);
            saves.remove(start + 1);
            saves.remove(start);
            Files.write(file.toPath(), saves, StandardOpenOption.CREATE);
            return result;
        } catch (Exception e) {
            System.out.println("Error loading save file");
            e.printStackTrace();
        }
        return null;
    }

    protected void inputLoadWordByDifficulty() {
        wordBuffer.clear();
        String message = "Please choose a difficulty: ANY, EASY, NORMAL, HARD, CHALLENGING";
        String error = "Error receiving difficulty";
        String difficulty = ensureInput(message, error);
        if (!validDifficulty(difficulty)) inputLoadWordByDifficulty();
        wordBuffer.addAll(loadWordBuffer(difficulty, nameDictionary));
    }

    protected boolean validDifficulty(String difficulty) {
        if (difficulty == null || difficulty.isEmpty() || !Character.isAlphabetic(difficulty.charAt(0))) {
            System.out.println("That was not a valid response, please try again");
            return false;
        }
        if (!(difficulty.equalsIgnoreCase("a") || difficulty.equalsIgnoreCase("any") ||
                difficulty.equalsIgnoreCase("e") || difficulty.equalsIgnoreCase("easy") ||
                difficulty.equalsIgnoreCase("n") || difficulty.equalsIgnoreCase("normal") ||
                difficulty.equalsIgnoreCase("h") || difficulty.equalsIgnoreCase("hard") ||
                difficulty.equalsIgnoreCase("c") || difficulty.equalsIgnoreCase("challenging"))) {
            System.out.printf("%s is not a valid difficulty", difficulty);
            return false;
        }
        return true;
    }

    protected List<String> loadWordBuffer(String difficulty, String dictionary) {
        int min = getWordMin(difficulty);
        int max = getWordMax(difficulty);
        int uniqMin = getWordUniqueMin(difficulty);
        int uniqMax = getWordUniqueMax(difficulty);
        try {
            URL url = getClass().getClassLoader().getResource(dictionary);
            Path p = Paths.get(url.toURI());
            ArrayList<String> words = Files.readAllLines(p).stream()
                    .filter(str -> str.length() > min && str.length() < max)
                    .filter(str -> {
                        int i = (int) str.toLowerCase().chars().distinct().count();
                        return i > uniqMin && i < uniqMax;
                    }).distinct().collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(words);
            return words.subList(0, 100);
        } catch (Exception e) {
            System.out.println("Error loading Dictionary");
            if (!(e instanceof URISyntaxException) && !(e instanceof NullPointerException)
                    && !(e instanceof IOException)) e.printStackTrace();
            return List.of("Helps", "customizable", "Fork", "Planet", "subdermatoglyphic",
                    "dumbwaiter", "Physical", "importance", "computerizably", "misconjugatedly", "Quark");
        }
    }

    protected List<String> loadScores(String scoreFile) {
        if (scoreFile == null || scoreFile.isEmpty()) return List.of();
        try {
            File file = Paths.get(scoreFile).toFile();
            if (!file.exists()) return null;
            return Files.readAllLines(file.toPath());
        } catch (Exception e) {
            System.out.println("Error loading High Scores");
            e.printStackTrace();
        }
        return null;
    }

    protected boolean updateScores(String scoreFile, int score) {
        if (scoreFile == null || scoreFile.isEmpty()) return false;
        try {
            File file = Paths.get(scoreFile).toFile();
            ArrayList<String> scoreSheet;
            if (file.createNewFile()) scoreSheet = new ArrayList<>();
            else scoreSheet = new ArrayList<>(Files.readAllLines(file.toPath()));
            addScoreToList(scoreSheet, score);
            reduceScoresSheet(scoreSheet);
            Files.write(file.toPath(), scoreSheet, StandardOpenOption.CREATE);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to High Scores!!");
            if (!(e instanceof UnsupportedOperationException || e instanceof NullPointerException)) e.printStackTrace();
        }
        return false;
    }

    protected static void reduceScoresSheet(ArrayList<String> scores) {
        if (scores == null || scores.size() < 3) return;
        int sheetLimit = 3 * scoreSheetSize;
        if (scores.size() > sheetLimit) {
            try {
                scores.remove(sheetLimit);
                scores.remove(sheetLimit);
                scores.remove(sheetLimit);
            } catch (Exception e) {
                System.out.println("Error removing record from High Score list. Record may be stored wrong");
                if (!(e instanceof IndexOutOfBoundsException)) e.printStackTrace();
            }
            reduceScoresSheet(scores);
        }
    }

    protected void addScoreToList(ArrayList<String> scoresList, int score) {
        // Each record is 3 lines: name, score, date
        if (scoresList == null) return;
        List<String> record = List.of(player, Integer.toString(score), LocalDate.now().toString());
        int idx = findRecordInsertionPoint(scoresList, score, 0);
        scoresList.addAll(idx, record);
    }

    private int findRecordInsertionPoint(ArrayList<String> scoresList, int score, int iter) {
        if (iter+2 >= scoresList.size()) return iter;     // End of list, return position
        if (Integer.parseInt(scoresList.get(iter+1)) < score) return iter;  // Found target area, return position
        return findRecordInsertionPoint(scoresList, score, iter+3);     // Target not found and list continues
    }

    public static int getWordMin(String diff) {
        if (diff == null || diff.isEmpty()) return 3;
        if (diff.equalsIgnoreCase("n") || diff.equalsIgnoreCase("normal")) {
            return 6;
        } else if (diff.equalsIgnoreCase("h") || diff.equalsIgnoreCase("hard")) {
            return 10;
        } else if (diff.equalsIgnoreCase("c") || diff.equalsIgnoreCase("challenging")) {
            return 15;
        } else return 3;
    }

    public static int getWordMax(String diff) {
        if (diff == null || diff.isEmpty()) return 20;
        if (diff.equalsIgnoreCase("e") || diff.equalsIgnoreCase("easy")) {
            return 6;
        } else if (diff.equalsIgnoreCase("n") || diff.equalsIgnoreCase("normal")) {
            return 10;
        } else if (diff.equalsIgnoreCase("h") || diff.equalsIgnoreCase("hard")) {
            return 15;
        } else return 20;
    }

    public static int getWordUniqueMin(String diff) {
        if (diff == null || diff.isEmpty()) return 2;
        if (diff.equalsIgnoreCase("n") || diff.equalsIgnoreCase("normal")) {
            return 5;
        } else if (diff.equalsIgnoreCase("h") || diff.equalsIgnoreCase("hard")) {
            return 7;
        } else if (diff.equalsIgnoreCase("c") || diff.equalsIgnoreCase("challenging")) {
            return 10;
        } else return 2;
    }

    public static int getWordUniqueMax(String diff) {
        if (diff == null || diff.isEmpty()) return 20;
        if (diff.equalsIgnoreCase("e") || diff.equalsIgnoreCase("easy")) {
            return 6;
        } else if (diff.equalsIgnoreCase("n") || diff.equalsIgnoreCase("normal")) {
            return 10;
        } else if (diff.equalsIgnoreCase("h") || diff.equalsIgnoreCase("hard")) {
            return 15;
        } else if (diff.equalsIgnoreCase("c") || diff.equalsIgnoreCase("challenging")) {
            return 17;
        } else return 20;
    }

    public String ensureInput(String message, String error) {
        try {
            System.out.println(message);
            return scan.next();
        } catch (Exception e) {
            System.out.println(error);
            e.printStackTrace();
            return ensureInput(message, error);
        }
    }

    public String ensureInputLine(String message, String error) {
        try {
            System.out.println(message);
            return scan.nextLine();
        } catch (Exception e) {
            System.out.println(error);
            e.printStackTrace();
            return ensureInputLine(message, error);
        }
    }

    public static void exit() {
        System.out.println("Thank you for playing");
        System.exit(0);
    }

    public static String encodeString(String str) {    // Used to hide the secret words when saved to prevent exploit
        if (str == null || str.isEmpty()) return str;
        byte[] encode = str.getBytes(StandardCharsets.US_ASCII);
        StringBuilder builder = new StringBuilder();
        return encodeByte(encode, builder, 0);
    }

    private static String encodeByte(byte[] word, StringBuilder builder, int iter) {
        if (iter >= word.length) return builder.toString();
        builder.append((char) (word[iter]-offset));
        return encodeByte(word, builder, iter+1);
    }

    public static String decodeString(String str) {
        if (str == null || str.isEmpty()) return str;
        byte[] decode = str.getBytes(StandardCharsets.US_ASCII);
        StringBuilder builder = new StringBuilder();
        return decodeByte(decode, builder, 0);
    }

    private static String decodeByte(byte[] word, StringBuilder builder, int iter) {
        if (iter >= word.length) return builder.toString();
        builder.append((char) (word[iter]+offset));
        return decodeByte(word, builder, iter+1);
    }

    private static final int offset = 45;
}
