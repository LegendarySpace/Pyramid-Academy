package world.fantasy;

import world.fantasy.items.consumable.HealthPotion;
import world.fantasy.creatures.Goblin;
import world.fantasy.creatures.Human;
import world.fantasy.items.equipment.*;
import world.fantasy.items.Item;
import world.fantasy.world.Land;
import world.fantasy.world.World;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Wormhole {
    public static ArrayList<Land> lands;
    public static ArrayList<Human> players;
    public static void main(String[] args) {
        // Your entrance into the program
        // Make a player object to handle player input
        var world = buildWorld(5, 0, 4, 3);
        if (world == null) {
            System.out.println("Failed to load world. System exiting");
            System.exit(0);
        }
        do {
            world.playTurn();
        } while (world.contains(players) && world.hasEnemies());
        world.displayMap();
        if (!world.hasEnemies()) {
            // You have lost
            System.out.println("Congratulations on defeating the goblins");
        } else {
            // You have won
            System.out.println("You failed to defeat all the goblins");
        }
    }

    public static ArrayList<Land> generateSquare(int size) {
        ArrayList<Land> square = new ArrayList<>();
        if (size < 1) return square;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) square.add(new Land(i,j));
        }
        return square;
    }

    public static ArrayList<Land> generateDiamond(int size) {
        ArrayList<Land> diamond = new ArrayList<>();
        final int s = size - 1;
        if (size < 1) return diamond;
        for (int i = 0; i < size; i++) {
            for (int j = s; j >= 0; j--) {
                diamond.add(new Land(i + s, j + s));
                if (j != 0) diamond.add(new Land(i + s, -j + s));
                if (i != 0) {
                    diamond.add(new Land(-i + s, j + s));
                    if (j != 0) diamond.add(new Land(-i + s, -j + s));
                }
            }
        }
        return diamond;
    }

    public static void spawnRemaining(World world) {
        for (int i = lands.size() - 1; i >= 0; i--) world.spawnLand(lands.remove(i));
    }

    public static void generateGoblins(World world, int amount) {
        if (world == null || amount < 1 || lands == null) return;
        for (int i = 1; i <= amount; i++) {
            if (lands.size() < 1) break;
            int idx = ThreadLocalRandom.current().nextInt(0, lands.size());
            Land land = lands.remove(idx);
            if (!world.landExists(land)) world.spawnObject(land, new Goblin());
        }
    }

    public static void generatePlayers(World world, int playerCount) {
        if (world == null || playerCount < 1 || lands == null) return;
        for (int i = 1; i <= playerCount; i++) {
            if (lands.size() < 1) break;
            Human h = new Human(false);
            players.add(h);
            int idx = ThreadLocalRandom.current().nextInt(0, lands.size());
            Land land = lands.remove(idx);
            if (!world.landExists(land)) world.spawnObject(land, h);
        }
    }

    public static void generateHumans(World world, int amount) {
        if (world == null || amount < 1 || lands == null) return;
        for (int i = 1; i <= amount; i++) {
            if (lands.size() < 1) break;
            int idx = ThreadLocalRandom.current().nextInt(0, lands.size());
            Land land = lands.remove(idx);
            if (!world.landExists(land)) world.spawnObject(land, new Human());
        }
    }

    public static void generateItems(World world, int amount) {
        if (world == null || amount < 1 || lands == null) return;
        for (int i = 1; i <= amount; i++) {
            if (lands.size() < 1) break;
            int idx = ThreadLocalRandom.current().nextInt(lands.size());
            Land land = lands.remove(idx);
            if (!world.landExists(land)) world.spawnObject(land, createItem());
        }
    }

    public static Item createItem() {
        int type = ThreadLocalRandom.current().nextInt(1, 7);
        int stat = ThreadLocalRandom.current().nextInt(10);
        return switch (type) {
            case 1 -> new Armor(stat);
            case 2 -> new Melee(stat);
            case 3 -> new Ranged(stat);
            case 4 -> new Magic(stat);
            case 5 -> new Shield(stat);
            case 6 -> new HealthPotion();
            default -> null;
        };
    }

    public static World buildWorld(int size, int humans, int goblins, int items) {
        World world = new World(size,size);
        players = new ArrayList<>();
        int players = World.ensureIntInput("How many Units will you have?", "Error, not a valid number");
        if (players > 10) {
            System.out.printf("You can only have %d units, extra will be removed%n", 10);
            players = 10;
        }
        if (size < 1 || players < 1 || goblins < 1) return null;

        // generate lands
        lands = generateSquare(size);
        // populate world
        generatePlayers(world, players);
        generateGoblins(world, goblins);
        generateHumans(world, humans);
        generateItems(world, items);
        // apply remaining lands to world
        spawnRemaining(world);
        return world;
    }
}
