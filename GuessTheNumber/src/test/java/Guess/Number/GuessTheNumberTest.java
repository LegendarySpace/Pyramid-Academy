package Guess.Number;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class GuessTheNumberTest {

    @Test
    void pickRandomTest() {
        for (int i = 0; i < 20; i++) {
            int x = GuessTheNumber.pickRandom();
            assertTrue(x >= 1 && x <= 20, "RNG is out of range");
        }
    }

    @Test
    void enterNameTest() {
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String str : values) {
            GuessTheNumber.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            GuessTheNumber.enterName();
            assertEquals(str, GuessTheNumber.name, "Name is not being stored properly");
            GuessTheNumber.scan.close();
        }
    }

    @Test
    void makeGuessTest() {
        ArrayList<String> values = new ArrayList<>();
        for (int i = 1; i <= 20; i++) values.add(Integer.toString(i));
        for (String str : values) {
            GuessTheNumber.guessAttempts = 0;
            GuessTheNumber.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals(Integer.parseInt(str), GuessTheNumber.makeGuess(), "Guess is not being correctly returned");
            GuessTheNumber.scan.close();
        }
    }

    @Test
    void invalidGuessTest() {
        ArrayList<String> values = new ArrayList<>();
        for (int i = -100; i <= 100; i++) if (i < 1 || i > 20) values.add(Integer.toString(i));
        for (String str : values) {
            GuessTheNumber.guessAttempts = 0;
            GuessTheNumber.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals(0, GuessTheNumber.makeGuess(), "Guess is not being correctly verified");
            GuessTheNumber.scan.close();
        }
    }

    @Test
    void wrongGuessTypeTest() {
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String str : values) {
            GuessTheNumber.guessAttempts = 0;
            GuessTheNumber.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals(0, GuessTheNumber.makeGuess(), "Incorrect guess data types aren't being handled properly");
            GuessTheNumber.scan.close();
        }
    }

    @Test
    void compareGuessTest() {
        List<Integer> values = List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,-1,-97, 65, 52, 21, 34);
        for (int i : values) {
            GuessTheNumber.secretNumber = i;
            assertEquals("equal", GuessTheNumber.compareGuess(i), "Guess equal comparison is giving incorrect result");
        }
        GuessTheNumber.secretNumber = -100;
        for (int i : values) {
            assertEquals("high", GuessTheNumber.compareGuess(i), "Guess high comparison is giving incorrect result");
        }
        GuessTheNumber.secretNumber = 100;
        for (int i : values) {
            assertEquals("low", GuessTheNumber.compareGuess(i), "Guess low comparison is giving incorrect result");
        }
        assertEquals("invalid", GuessTheNumber.compareGuess(0), "Guess invalid comparison is giving incorrect result");
    }

    @Test
    void outputTest() {
        assertTrue(GuessTheNumber.output("equal"), "Output equals is giving incorrect result");
        assertFalse(GuessTheNumber.output("high"), "Output high is giving incorrect result");
        assertFalse(GuessTheNumber.output("low"), "Output low is giving incorrect result");
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String str : values) {
            assertFalse(GuessTheNumber.output(str), "Invalid output is not failing");
        }
    }
}