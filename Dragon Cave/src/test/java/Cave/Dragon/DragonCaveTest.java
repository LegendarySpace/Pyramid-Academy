package Cave.Dragon;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

class DragonCaveTest {
    @Test
    void determineCaveTest() {
        // assert determineCave(direction) does not return null unless direction is invalid
        assertAll("CaveTest results",
                () -> assertNotNull(DragonCave.determineCave("east"), "East mountain failed"),
                () -> assertNotNull(DragonCave.determineCave("west"), "West mountain failed"),
                () -> assertNotNull(DragonCave.determineCave("north"), "North mountain failed"),
                () -> assertNull(DragonCave.determineCave("south"), "South mountain should be invalid"));
    }

    @Test
    void eastTest() {
        // assert different ByteArrayInputStreams return usable string
        List<String> values = List.of("East", "EAST", "eAsT", "EasT", "eASt", "east");
        for (String str : values) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals("east", DragonCave.eastWestNorth().toLowerCase(), String.format("Direction input %s failed", str));
            DragonCave.scan.close();
        }
    }

    @Test
    void westTest() {
        List<String> values = List.of("West", "WEST","wEsT", "WesT", "wESt", "west");
        for (String str : values) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals("west", DragonCave.eastWestNorth().toLowerCase(), String.format("Direction input %s failed", str));
            DragonCave.scan.close();
        }
    }

    @Test
    void northTest() {
        List<String> values = List.of("North", "NORTH", "nOrTh", "NoRtH", "NortH", "nORTh", "north");
        for (String str : values) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals("north", DragonCave.eastWestNorth().toLowerCase(), String.format("Direction input %s failed", str));
            DragonCave.scan.close();
        }
    }

    @Test
    void invalidTest() {
        List<String> values = List.of("South", "SOUTH", "south", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String str : values) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            // assertThrows(NoSuchElementException.class, DragonCave::eastWestNorth, String.format("Invalid direction input %s should be null", str));
            assertNull(DragonCave.eastWestNorth(), String.format("Invalid direction input %s should be null", str));
            DragonCave.scan.close();
        }
    }

    @Test
    void rngTest() {
        for (int i = 1; i <= 20; i++) {
            int x = Cave.d20();
            assertTrue(x >= 1 && x <= 20);
        }
        for (int i = 1; i <= 10; i++) {
            int x = Cave.d10();
            assertTrue(x >= 1 && x <= 10);
        }
        for (int i = 1; i <= 5; i++) {
            int x = Cave.d5();
            assertTrue(x >= 1 && x <= 5);
        }
    }
}