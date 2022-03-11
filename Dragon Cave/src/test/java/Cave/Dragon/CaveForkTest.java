package Cave.Dragon;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CaveForkTest {

    @Test
    void leftTest() {
        List<String> value = List.of("LEFT", "lEFT", "LeFT", "LEfT", "LEFt", "leFT", "lEfT", "lEFt", "LefT",
                "LeFt", "LEft", "Left", "lEft", "leFt", "lefT", "left");
        for (String str : value) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            CaveFork fork = new CaveFork(DragonCave.scan, 4);
            assertEquals("left", fork.leftOrRight().toLowerCase(), "Failed to select left");
            DragonCave.scan.close();
        }
    }

    @Test
    void rightTest() {
        List<String> value = List.of("RIGHT", "rIGHT", "RiGHT", "RIgHT", "RIGhT", "RIGHt", "riGHT", "rIgHT",
                "rIGhT", "rIGHt", "RigHT", "RiGhT", "RiGHt", "RIghT", "RIgHt", "RIGht", "rigHT", "riGhT", "riGHt",
                "rIghT", "rIgHt", "rIGht", "righT", "rigHt", "riGht", "rIght", "Right", "right");
        for (String str : value) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            CaveFork fork = new CaveFork(DragonCave.scan, 4);
            assertEquals("right", fork.leftOrRight().toLowerCase(), "Failed to select right");
            DragonCave.scan.close();
        }
    }

    @Test
    void invalidTest() {
        List<String> value = List.of("Yes", "YES", "yes","South", "SOUTH", "south", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String str : value) {
            DragonCave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            CaveFork fork = new CaveFork(DragonCave.scan, 4);
            assertNull(fork.leftOrRight(), "Invalid input should be null");
            DragonCave.scan.close();
        }
    }
}