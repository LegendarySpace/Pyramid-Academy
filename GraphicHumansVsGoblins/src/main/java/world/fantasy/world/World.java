package world.fantasy.world;

import world.fantasy.Actor;
import world.fantasy.Menu;
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
    public static final int MAX_DISPLAY_SIZE = 10;        // if bound is passed map turns to scroll
    public HashSet<Land> lands;

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

    public HashSet<Actor> actors;

    public static Scanner scan;
    private HashMap<Land, Actor> worldMap;
    public int boardHeight, boardWidth;
    public Menu menu;
    // Holds information about all objects in the world and maps their position on the map

    public World() {
        this(5);
    }

    public World(int size) {
        this(generateSquare(size));
    }

    public World(List<Land> lands) {
        this.lands = new HashSet<>(lands);
        this.actors = new HashSet<>();
    }

    public boolean landExists(Land land) { return lands.contains(land); }
    public List<Creature> getPlayers() {
        return actors.stream().filter(a -> a instanceof  Creature)
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
    public List<Creature> getUnits() { return actors.stream().filter(o -> o instanceof Creature)
            .map(o -> (Creature) o).toList(); }
    public List<Item> getItems() { return actors.stream().filter(o -> o instanceof Item)
            .map(o -> (Item) o).toList(); }

    // Play Turn: iterate through each unit attempting to move them
    public void playTurn() {
        // May need to switch to an iterator instead of for loop so units can be removed during loop    // or record units to remove at end of loop
        // iterate over turn order deque
        for (var unit : getUnits()) {
            if (!unit.doesExist()) continue;
            menu.displayMenu(unit);
        }
        actors = actors.stream().filter(Actor::doesExist).collect(Collectors.toCollection(HashSet::new));
    }


    public static ArrayList<Land> generateSquare(int size) {
        ArrayList<Land> square = new ArrayList<>();
        if (size < 1) return square;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) square.add(new Land(row,col));
        }
        return square;
    }
    public static ArrayList<Land> generateDiamond(int size) {
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

    public void generatePlayers(int playerCount) {
        if (playerCount < 1 || lands == null) return;
        for (int i = 0; i < playerCount; i++) {
            var land = lands.stream()                      // start with all lands,
                    .filter(x -> !actors.stream()                       // filter away any lands
                            .map(Actor::getPosition)                    // occupied by an actor
                            .toList().contains(x))
                    .findAny().orElse(null);
            if (land == null) return;
            var player = new Human(this, false);
            spawnActor(player, land);
        }
        String pause = "Stop again";
    }
    public void generateEnemies(int amount) {
        if (amount < 1 || lands == null) return;
        for (int i = 0; i < amount; i++) {
            var opt = lands.stream()
                    .filter(x -> !actors.stream()
                            .map(Actor::getPosition)
                            .toList().contains(x))
                    .findAny();
            if (opt.isEmpty()) break;
            spawnActor(new Goblin(this), opt.get());
        }
    }
    public void generateAllies(int playerCount) {
        if (playerCount < 1 || lands == null) return;
        for (int i = 0; i < playerCount; i++) {
            var opt = lands.stream()
                    .filter(x -> !actors.stream()
                            .map(Actor::getPosition)
                            .toList().contains(x))
                    .findAny();
            if (opt.isEmpty()) return;
            spawnActor(new Human(this), opt.get());
        }
    }
    public void generateItems(int amount) {
        if (amount < 1 || lands == null) return;
        for (int i = 0; i < amount; i++) {
            var opt = lands.stream()
                    .filter(x -> !actors.stream()
                            .map(Actor::getPosition)
                            .toList().contains(x))
                    .findAny();
            if (opt.isEmpty()) break;
            spawnActor(createItem(), opt.get());
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


    // DEPRECATED
    public void displayMap() {
        // for each key, if map has value display value, else display key
        var keys = worldMap.keySet();
        System.out.println('\u2554' + "\u2550\u2566".repeat(boardWidth - 1) + "\u2550\u2557");
        for (int i = boardHeight-1; i >= 0; i--) {
            System.out.print('\u2551');
            for (int j = 0; j < boardWidth; j++) {
                Land land = new Land(i,j);
                if (keys.contains(land)) {
                    // display tile or object on it
                    var obj = worldMap.get(land);
                    if (obj != null) System.out.print(obj.toString());
                    else System.out.print(land.toString());
                } else System.out.print(" ");

                System.out.print('\u2551');
            }
            System.out.print("\n");
            if (i != 0) System.out.println('\u2560' + "\u2550\u256c".repeat(boardWidth - 1) + "\u2550\u2563");
        }
        System.out.println('\u255a' + "\u2550\u2569".repeat(boardWidth - 1) + "\u2550\u255d");
    }





    public static int d6() { return ThreadLocalRandom.current().nextInt(1, 7); }

    public static int ensureIntInput(String message, String error) {
        try {
            System.out.println(message);
            return Integer.parseInt(scan.next());
        } catch (Exception e) {
            System.out.println(error);
            if (!(e instanceof NumberFormatException)) e.printStackTrace();
            return ensureIntInput(message, error);
        }
    }

    public static String ensureInput(String message, String error) {
        try {
            System.out.println(message);;
            return scan.next();
        } catch (Exception e) {
            System.out.println(error);
            e.printStackTrace();
            return ensureInput(message, error);
        }
    }

    public static String ensureInputLine(String message, String error) {
        try {
            System.out.println(message);
            return scan.nextLine();
        } catch (Exception e) {
            System.out.println(error);
            e.printStackTrace();
            return ensureInputLine(message, error);
        }
    }

    public static boolean isYesOrNo(String response) {
        if (response == null || response.isEmpty()) return false;
        return response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes") ||
                response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no");
    }

    public static boolean yesOrNo(String message) {
        String yn = ensureInput(message, "Problem receiving input");
        if (!isYesOrNo(yn)) {
            System.out.println("A yes or no response is required");
            return yesOrNo(message);
        }
        return yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("yes");
    }

    public static Direction validDirection(String response) {
        if (response == null || response.isEmpty()) return null;
        for (var d : Direction.values()) {
            if (d.name().equalsIgnoreCase(response) || d.name().substring(0,1).equalsIgnoreCase(response)) return d;
        }
        return null;
    }

    public static Direction getDirection(String message) {
        String input = ensureInput(message, "Problem receiving input");
        var direction = validDirection(input);
        if (direction == null) {
            System.out.printf("%s is not a valid direction, please try again%n", direction);
            return getDirection(message);
        }
        return direction;
    }
}
