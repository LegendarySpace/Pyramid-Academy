package Cave.Dragon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CaveForkTest {
    public CaveFork cf;

    @BeforeEach
    void setup() {
        cf = new CaveFork(4);
    }

    @Test
    void validLeftOrRightTest() {
        List<String> value = List.of("LEFT", "lEFT", "LeFT", "LEfT", "LEFt", "leFT", "lEfT", "lEFt", "LefT", "LeFt",
                "LEft", "Left", "lEft", "leFt", "lefT", "left", "RIGHT", "rIGHT", "RiGHT", "RIgHT", "RIGhT", "RIGHt",
                "riGHT", "rIgHT", "rIGhT", "rIGHt", "RigHT", "RiGhT", "RiGHt", "RIghT", "RIgHt", "RIGht", "rigHT",
                "riGhT", "riGHt", "rIghT", "rIgHt", "rIGht", "righT", "rigHt", "riGht", "rIght", "Right", "right");
        for (String str : value) {
            assertTrue(cf.leftOrRight(str));
        }
    }

    @Test
    void invalidLeftOrRightTest() {
        List<String> value = List.of("Yes", "YES", "yes","South", "SOUTH", "south", "sdfjf", "SDlsucVCIU",
                "JdfklsKSqwFlkFDlS", "NwefS", "SDfjoefm", "xcVOPem", ".,mwefoi");
        for (String str : value) {
            assertFalse(cf.leftOrRight(str));
        }
    }

    @Test
    void leftTest() {
        List<String> value = List.of("LEFT", "lEFT", "LeFT", "LEfT", "LEFt", "leFT", "lEfT", "lEFt", "LefT",
                "LeFt", "LEft", "Left", "lEft", "leFt", "lefT", "left");
        for (String str : value) {
            Cave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertTrue(cf.chooseLeft(), "Failed to select left");
            Cave.scan.close();
        }
    }

    @Test
    void rightTest() {
        List<String> value = List.of("RIGHT", "rIGHT", "RiGHT", "RIgHT", "RIGhT", "RIGHt", "riGHT", "rIgHT",
                "rIGhT", "rIGHt", "RigHT", "RiGhT", "RiGHt", "RIghT", "RIgHt", "RIGht", "rigHT", "riGhT", "riGHt",
                "rIghT", "rIgHt", "rIGht", "righT", "rigHt", "riGht", "rIght", "Right", "right");
        for (String str : value) {
            Cave.scan = new Scanner(new ByteArrayInputStream(str.getBytes()));
            assertFalse(cf.chooseLeft(), "Failed to select right");
            Cave.scan.close();
        }
    }
}