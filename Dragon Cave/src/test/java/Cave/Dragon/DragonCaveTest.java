package Cave.Dragon;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

class DragonCaveTest {
    DragonCave dc;

    @BeforeEach
    void setup() {
        dc = new DragonCave();
    }

    @Test
    void validEastWestNorthTest() {
        List<String> values = List.of("East", "EAST", "eAsT", "EasT", "eASt", "east", "West", "WEST","wEsT", "WesT",
                "wESt", "west", "North", "NORTH", "nOrTh", "NoRtH", "NortH", "nORTh", "north");
        for (String str : values) assertTrue(dc.eastWestNorth(str));
    }

    @Test
    void invalidEastWestNorthTest() {
        List<String> values = List.of("South", "SOUTH", "south", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi", "aaaaaaaaa", "ouiposdviohfja;lfhi");
        for (String str : values) assertFalse(dc.eastWestNorth(str));
    }

    @Test
    void determineCaveTest() {
        // assert determineCave(direction) does not return null unless direction is invalid
       assertNotNull(dc.determineCave("east"), "East mountain failed");
       assertNotNull(dc.determineCave("west"), "West mountain failed");
       assertNotNull(dc.determineCave("north"), "North mountain failed");
       List<String> values = List.of("South", "SOUTH", "south", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
       for (String str : values) assertNull(dc.determineCave(str), String.format("%s should be invalid", str));
    }

    @Test
    void approachMountainEastTest() {
        // assert different ByteArrayInputStreams return usable string
        List<String> values = List.of("East", "EAST", "eAsT", "EasT", "eASt", "east");
        for (String str : values) {
            Cave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals("east", dc.approachMountain().toLowerCase(), String.format("Direction input %s failed", str));
            Cave.scan.close();
        }
    }

    @Test
    void approachMountainWestTest() {
        List<String> values = List.of("West", "WEST","wEsT", "WesT", "wESt", "west");
        for (String str : values) {
            Cave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals("west", dc.approachMountain().toLowerCase(), String.format("Direction input %s failed", str));
            Cave.scan.close();
        }
    }

    @Test
    void approachMountainNorthTest() {
        List<String> values = List.of("North", "NORTH", "nOrTh", "NoRtH", "NortH", "nORTh", "north");
        for (String str : values) {
            Cave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertEquals("north", dc.approachMountain().toLowerCase(), String.format("Direction input %s failed", str));
            Cave.scan.close();
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