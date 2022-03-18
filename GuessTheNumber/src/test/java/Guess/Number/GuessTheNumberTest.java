package Guess.Number;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class GuessTheNumberTest {
    GuessTheNumber gn;

    @BeforeEach
    void setup() {
        gn = new GuessTheNumber();
    }

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
            gn.enterName();
            assertEquals(str, gn.getName(), "Name is not being stored properly");
            GuessTheNumber.scan.close();
        }
    }

    @Test
    void isInRangeTest() {
        for (int i = -50; i <= 50; i++) {
            if (i >= 1 && i <= 20) assertTrue(gn.isInRange(i));
            else assertFalse(gn.isInRange(i));
        }
    }

    @Test
    void makeGuessTest() {
        ArrayList<String> values = new ArrayList<>();
        for (int i = 1; i <= 20; i++) values.add(Integer.toString(i));
        for (String str : values) {
            GuessTheNumber.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            gn.makeGuess();
            assertEquals(Integer.parseInt(str), gn.getAttempts(), "Guess is not correctly incrementing attempts");
            GuessTheNumber.scan.close();
        }
    }

    @Test
    void compareGuessTest() {
        List<Integer> values = List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,-1,-97, 65, 52, 21, 34);
        for (int i : values) {
            assertEquals("equal", gn.compareGuess(i, i), "Guess equal comparison is giving incorrect result");
        }
        for (int i : values) {
            assertEquals("high", gn.compareGuess(-100, i), "Guess high comparison is giving incorrect result");
        }
        for (int i : values) {
            assertEquals("low", gn.compareGuess(100, i), "Guess low comparison is giving incorrect result");
        }
    }

    @Test
    void outputTest() {
        assertTrue(gn.didYouWin("equal"), "Output equals is giving incorrect result");
        assertFalse(gn.didYouWin("high"), "Output high is giving incorrect result");
        assertFalse(gn.didYouWin("low"), "Output low is giving incorrect result");
        List<String> values = List.of("Brian", "Jordan", "Jacob", "Zach", "Mike", "Luke", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String str : values) {
            assertFalse(gn.didYouWin(str), "Invalid output is not failing");
        }
    }
}