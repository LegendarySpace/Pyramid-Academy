package Cave.Dragon;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CavePathTest {
    public CavePath cp;

    @BeforeEach
    void setup() {
        cp = new CavePath(4);
    }

    @Test
    void validYesOrNoTest() {
        List<String> value = List.of("Yes", "YES", "yES", "yEs", "YeS", "yeS", "YEs", "yes", "Y", "y", "NO", "No",
                "nO", "no", "n");
        for (String str : value) {
            assertTrue(cp.yesOrNo(str));
        }
    }

    @Test
    void invalidYesOrNoTest() {
        List<String> value = List.of("Maybe", "Not Really", "Yeah", "yeAH", "Yo", "yO", "yOu3", "Y0", "Never",
                "South", "SOUTH", "south", "sdfjf", "SDlsucVCIU", "JdfklsKSqwFlkFDlS", "NwefS",
                "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String str : value) {
            assertFalse(cp.yesOrNo(str));
        }
    }

    @Test
    void continueJourneyTest() {
        List<String> value = List.of("Yes", "YES", "yES", "yEs", "YeS", "yeS", "YEs", "yes", "Y", "y");
        for (String str : value) {
            Cave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertTrue(cp.continueJourney(), "Failed to continue journey");
            Cave.scan.close();
        }
    }

    @Test
    void endJourneyTest() {
        List<String> value = List.of("NO", "No", "nO", "no", "n");
        for (String str : value) {
            Cave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            System.out.println(str);
            assertFalse(cp.continueJourney(), "Failed to end journey");
            Cave.scan.close();
        }
    }
}