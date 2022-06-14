package world.fantasy.world;

import world.fantasy.Actor;
import world.fantasy.creatures.Goblin;
import world.fantasy.creatures.Human;
import world.fantasy.creatures.Creature;
import world.fantasy.items.Item;
import world.fantasy.items.consumable.HealthPotion;
import world.fantasy.items.equipment.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class World {
    public static final int MIN_SIZE = 5;

    public HashSet<Land> lands;
    public HashSet<Actor> actors;

    public HashSet<Land> getLands() {
        return lands;
    }
    public void setLands(HashSet<Land> lands) {
        this.lands = lands;
    }
    public HashSet<Actor> getActors() {
        return actors;
    }
    public void setActors(HashSet<Actor> actors) {
        this.actors = actors;
    }

    public static int boardHeight, boardWidth;
    public int boardSize;
    // Holds information about all objects in the world and maps their position on the map

    public World() {
        this(5);
    }

    public World(int size) {
        this(generateSquare(size));

        // TODO: This should be reworked to accommodate any land collection
        boardSize = size;
    }

    public World(List<Land> lands) {
        this.lands = new HashSet<>(lands);
        this.actors = new HashSet<>();
    }

    public boolean landExists(Land land) { return lands.contains(land); }
    public List<Creature> getPlayers() {
        return actors.stream().filter(a -> a instanceof Creature)
                .map(a -> (Creature) a).filter(a -> !a.isNPC()).toList();
    }
    public boolean hasPlayers() {
        return !getPlayers().isEmpty();
    }
    public boolean hasEnemies() { return actors.stream().anyMatch(a -> a instanceof Goblin); }
    public boolean spawnLand(Land land) { return lands.add(land); }
    public boolean spawnLand(int row, int col) { return spawnLand(new Land(row, col)); }
    public boolean spawnActor(Actor actor, Land location) {
        if (actors.stream().anyMatch(x -> x.getPosition() == location)) return false;
        actor.setPosition(location);
        actors.add(actor);
        return true;
    }
    public static int getHeight() { return boardHeight; }
    public static int getWidth() { return boardWidth; }
    public List<Creature> getUnits() { return actors.stream().filter(o -> o instanceof Creature)
            .map(o -> (Creature) o).toList(); }
    public List<Item> getItems() { return actors.stream().filter(o -> o instanceof Item)
            .map(o -> (Item) o).toList(); }
    public void gc() {
        actors = actors.stream().filter(Actor::doesExist).collect(Collectors.toCollection(HashSet::new));
    }

    public static ArrayList<Land> generateSquare(int size) {
        size = Math.max(MIN_SIZE, size);
        boardHeight = size;
        boardWidth = size;
        ArrayList<Land> square = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) square.add(new Land(row,col));
        }
        return square;
    }
    public static ArrayList<Land> generateDiamond(int size) {
        size = Math.min(MIN_SIZE, size);
        boardHeight = size;
        boardWidth = size;
        ArrayList<Land> diamond = new ArrayList<>();
        final int s = size - 1;
        if (size < 1) return diamond;
        for (int row = 0; row < size; row++) {
            for (int col = s; col >= 0; col--) {
                diamond.add(new Land(row + s, col + s));
                if (col != 0) diamond.add(new Land(row + s, -col + s));
                if (row != 0) {
                    diamond.add(new Land(-row + s, col + s));
                    if (col != 0) diamond.add(new Land(-row + s, -col + s));
                }
            }
        }
        return diamond;
    }

    public Land getRandomLand() {
        var list = lands.stream()
                .filter(x -> !actors.stream()
                        .map(Actor::getPosition)
                        .toList().contains(x))
                .collect(Collectors.toCollection(ArrayList::new));
        if (list.isEmpty()) return null;
        Collections.shuffle(list);
        return list.get(0);
    }

    public void generatePlayers(int playerCount) {
        if (playerCount < 1 || lands == null) return;
        for (int i = 0; i < playerCount; i++) {
            var land = getRandomLand();
            if (land == null) return;
            var player = new Human(this, false);
            spawnActor(player, land);
        }
        String pause = "Stop again";
    }
    public void generateEnemies(int amount) {
        if (amount < 1 || lands == null) return;
        for (int i = 0; i < amount; i++) {
            var land = getRandomLand();
            if (land == null) break;
            spawnActor(new Goblin(this), land);
        }
    }
    public void generateAllies(int playerCount) {
        if (playerCount < 1 || lands == null) return;
        for (int i = 0; i < playerCount; i++) {
            var land = getRandomLand();
            if (land == null) return;
            spawnActor(new Human(this), land);
        }
    }
    public void generateItems(int amount) {
        if (amount < 1 || lands == null) return;
        for (int i = 0; i < amount; i++) {
            var land = getRandomLand();
            if (land == null) break;
            spawnActor(createItem(), land);
        }
    }
    public Item createItem() {
        int type = ThreadLocalRandom.current().nextInt(1, 7);
        int stat = ThreadLocalRandom.current().nextInt(10);
        return switch (type) {
            case 1 -> new Armor(this, stat);
            case 2 -> new Melee(this, stat);
            case 3 -> new Ranged(this, stat);
            case 4 -> new Magic(this, stat);
            case 5 -> new Shield(this, stat);
            case 6 -> new HealthPotion(this);
            default -> null;
        };
    }
    public void spawnWave(int enemies, int allies, int items) {
        generateEnemies(enemies);
        generateAllies(allies);
        generateItems(items);
    }





    public static int d6() { return ThreadLocalRandom.current().nextInt(1, 7); }

}
