package Cave.Dragon;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CavePathTest {
    @Test
    void continueJourneyTest() {
        List<String> value = List.of("Yes", "YES", "yES", "yEs", "YeS", "yeS", "YEs", "yes", "Y", "y");
        for (String str : value) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            CavePath path = new CavePath(DragonCave.scan, 4);
            assertTrue(path.continueJourney(), "Failed to continue journey");
            DragonCave.scan.close();
        }
    }

    @Test
    void endJourneyTest() {
        List<String> value = List.of("NO", "No", "nO", "no", "n", "Maybe", "Not Really", "Yeah",
                "yeAH", "Yo", "yO", "yOu3", "Y0", "Never");
        for (String str : value) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            CavePath path = new CavePath(DragonCave.scan, 4);
            assertFalse(path.continueJourney(), "Failed to end journey");
            DragonCave.scan.close();
        }
    }
}