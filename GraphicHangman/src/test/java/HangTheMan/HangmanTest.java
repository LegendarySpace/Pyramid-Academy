package HangTheMan;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class HangmanTest {
    public static final String nameSaveFile = "src/test/resources/SavedGame.txt";
    public static final String nameHighScores = "src/test/resources/HighScores.txt";
    public static Hangman hangman;
    public static String name = "Me";

    @BeforeEach     // Create new hangman object
    void setup() {
        Scanner scan = new Scanner(System.in);
        hangman = new Hangman(scan, name);
    }

    @AfterEach
    void teardown() {
        hangman.scan.close();
    }

    @Test   // Input buffer
    void validInputMenuTest() {
        hangman.scan.close();
        List<String> values = List.of("n", "new", "c", "continue", "s", "score", "h", "help", "q", "quit");
        for (String val : values) {
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertEquals(val, hangman.inputMenu(), "Input menu is NOT correctly accepting input");
            hangman.scan.close();
        }
    }

    @Test   // Input buffer
    void invalidInputMenuTest() {
        hangman.scan.close();
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String val : values) {
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertEquals("quit", hangman.inputMenu(), "Improperly handles incorrect input");
            hangman.scan.close();
        }
    }

    @Test   // input buffer
    void validInputGuessLetterTest() {
        hangman.scan.close();
        List<String> values = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        for (String val : values) {
            hangman.lettersGuessed = "";
            hangman.lettersIncorrect = "";
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertEquals(val, hangman.inputGuessLetter(), "Letter Guess is not correctly accepting input");
            hangman.scan.close();
            hangman.scan = new Scanner(new ByteArrayInputStream(val.toLowerCase().getBytes()));
            assertEquals(val, hangman.inputGuessLetter(), "Letter Guess is not correctly accepting input");
            hangman.scan.close();
        }
        hangman.scan.close();
        List<String> others = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        for (String val : others) {
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertEquals(Character.toString(val.charAt(0)).toUpperCase(), hangman.inputGuessLetter(),
                    "Letter Guess is not correctly handling multicharacter Strings");
            hangman.scan.close();
        }


    }

    @Test   // input buffer
    void invalidInputGuessLetterTest() {
        hangman.scan.close();
        List<String> values = Arrays.asList("quit", "save", "help", "%n", ".,mwefoi", "!", "@", "(", "+", "~");
        for (String val : values) {
            hangman.lettersGuessed = "";
            hangman.lettersIncorrect = "";
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertEquals("quit", hangman.inputGuessLetter(), "Invalid input not prompting quit");
            hangman.scan.close();
        }
        hangman.lettersGuessed = "";
        hangman.lettersIncorrect = "";
        assertEquals("quit", hangman.inputGuessLetter(), "Invalid input not prompting quit");
        hangman.scan.close();
    }

    @Test   // Input buffer, secret, letters guessed, chances
    void gameplayLoopFailTest() {
        hangman.scan.close();
        List<String> fails = Arrays.asList("h i j k l m n", "a b c d e f g n", "quit");
        for (String val : fails) {
            hangman.lettersGuessed = "";
            hangman.lettersIncorrect = "";
            hangman.secretWord = "abcdefg";
            hangman.gallows.chancesRemaining = 6;
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertFalse(hangman.gameplayLoop(), "Gameplay loop incorrectly returning TRUE");
            hangman.scan.close();
        }
    }

    @Test
    void gameplayLoopSuccessTest() {
        hangman.scan.close();
        List<String> success = Arrays.asList("h i j k l m y", "a b c d e f g y");
        for (String val : success) {
            hangman.lettersGuessed = "";
            hangman.lettersIncorrect = "";
            hangman.secretWord = "abcdefg";
            hangman.gallows.chancesRemaining = 6;
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertTrue(hangman.gameplayLoop(), "Gameplay loop incorrectly returning FALSE");
            hangman.scan.close();
        }
    }

    @Test   // letters guessed and incorrect, secret word, chances remaining
    void checkLetterNotContainedTest() {
        for (int i = -10; i < 20; i++) {
            hangman.lettersGuessed = "";
            hangman.lettersIncorrect = "";
            hangman.gallows.chancesRemaining = i;
            hangman.secretWord = "a";
            int rn = ThreadLocalRandom.current().nextInt(1, 10);
            for (int j = 0; j < rn; j++) {
                hangman.checkLetterContained("b");
            }
            assertEquals(i-rn, hangman.gallows.chancesRemaining, "Chances not properly decrementing");
        }
    }

    @Test   // letters guessed and incorrect, secret word, chances remaining
    void checkLetterContainedTest() {
        for (int i = -10; i < 20; i++) {
            hangman.lettersIncorrect = "";
            hangman.lettersGuessed = "";
            hangman.gallows.chancesRemaining = i;
            hangman.secretWord = "a";
            int rn = ThreadLocalRandom.current().nextInt(1, 10);
            for (int j = 0; j < rn; j++) {
                hangman.checkLetterContained("a");
            }
            assertEquals(i, hangman.gallows.chancesRemaining, "Chances unintentionally changing");
        }
    }

    @Test   // Set Player name, file location, secret word, letters guessed and incorrect
    void saveGameTest() {
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        for (int i = 0; i < values.size(); i++) {
            Hangman h = new Hangman(hangman.scan, "Brian" + i);
            h.secretWord = values.get(i);
            h.lettersGuessed = "abcdefg";
            h.lettersIncorrect = h.lettersGuessed.replaceAll("["+h.secretWord+"]","");
            assertTrue(h.saveGame(nameSaveFile), "Game not saving correctly");
        }
    }

    @Test   // Set player name and file location
    void loadGameTest() {
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        for (int i = 0; i < values.size(); i++) {
            Hangman h = new Hangman(hangman.scan, "Brian" + i);
            h.secretWord = values.get(i);
            h.lettersGuessed = "abcdefg";
            h.lettersIncorrect = h.lettersGuessed.replaceAll("["+h.secretWord+"]","");
            h.saveGame(nameSaveFile);
            List<String> load = h.loadGame(nameSaveFile);
            assertEquals(h.secretWord, load.get(0), "Secret word failed to load");
            assertEquals(h.lettersGuessed, load.get(1), "failed to load letters guessed");
            assertEquals(h.lettersIncorrect, load.get(2), "failed to load incorrect letters");
        }
    }

    @Test   // Pass an empty list and a filled list, player and calculate score
    void addScoreToListTest() {
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        for (String val : values) {
            hangman.secretWord = val;
            hangman.lettersGuessed = "abcdefg";
            hangman.lettersIncorrect = hangman.lettersGuessed.replaceAll("["+hangman.secretWord+"]","");
            ArrayList<String> list = new ArrayList<>();
            hangman.addScoreToList(list);
            assertEquals(name, list.get(0), "Name not being added to score list");
            assertEquals(Integer.toString(hangman.calculateScore()), list.get(1), "Score not being added to score list");
        }
        ArrayList<String> list = null;
        hangman.addScoreToList(list);
        assertNull(list, "Null list should remain null but isn't");
    }

    @Test   // Fill sheet with excess values
    void reduceScoresSheetTest() {
        for (int size = 0; size < 100; size++) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) list.add(Integer.toString(i));
            Hangman.reduceScoresSheet(list);
            assertTrue((3 * Hangman.scoreSheetSize) >= list.size(), "Reduce failing to shrink array size");
            System.out.printf("Starting size of score: %s%nFinal size of score: %s%n", size, list.size());
        }
    }

    @Test   // Set file location, player, and calculate score
    void updateScoresTest() {
        hangman.secretWord = "planet";
        hangman.lettersGuessed = "abcefgln";
        hangman.lettersIncorrect = hangman.lettersGuessed.replaceAll("["+hangman.secretWord+"]","");
        assertTrue(hangman.updateScores(nameHighScores));
        assertFalse(hangman.updateScores(null));
    }

    @Test   // Set input buffer
    void successfulPlayAgainTest() {
        hangman.scan.close();
        List<String> values = List.of("y", "yes");
        for (String val : values) {
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertTrue(hangman.inputPlayAgain());
            hangman.scan.close();
        }
    }

    @Test   // Set input buffer
    void failedPlayAgainTest() {
        hangman.scan.close();
        List<String> values = List.of("n", "no", "q", "quit", "Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke",
                "sdfjf", "SDlsucVCIU", "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        for (String val : values) {
            hangman.scan = new Scanner(new ByteArrayInputStream(val.getBytes()));
            assertFalse(hangman.inputPlayAgain());
            hangman.scan.close();
        }
    }

    @Test
    void dictionaryTest() {
        hangman.scan.close();
        List<String> values = List.of("e", "easy", "n", "normal", "h", "hard", "c", "challenging", "a", "any");
        for (String diff : values) {
            for (int i = 0; i < 20; i++) {
                hangman.scan = new Scanner(new ByteArrayInputStream(diff.getBytes()));
                // load the dictionary based on difficulty then pick a random word
                hangman.inputLoadWordByDifficulty();
                String word = hangman.wordBuffer.get(ThreadLocalRandom.current().nextInt(0, 99));
                // the length of the word should be between getwordmin and getwordmax
                assertTrue(word.length() > Hangman.getWordMin(diff) && word.length() < Hangman.getWordMax(diff));
            }
         }
    }

    @Test
    void testEncodingProcess() {
        List<String> values = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
                "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "n", "new", "c", "continue", "s", "score", "h",
                "save", "help", "y", "yes", "n", "no", "q", "quit", "Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke",
                "sdfjf", "SDlsucVCIU", "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem");
        for (String val : values) {
            // ensure encoding then decoding returns the original word
            assertEquals(val, Hangman.decodeString(Hangman.encodeString(val)), "Error with encoding");
        }
    }
}