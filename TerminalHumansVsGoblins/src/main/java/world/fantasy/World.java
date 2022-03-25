package world.fantasy;

import world.fantasy.creatures.Goblin;
import world.fantasy.creatures.Human;
import world.fantasy.creatures.Humanoid;
import world.fantasy.Items.Item;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    public static Scanner scan;
    private final HashMap<Land, Object> worldMap;
    public final int boardHeight, boardWidth;
    public final Menu menu;
    // Holds information about all objects in the world and maps their position on the map


    World(int height, int width) {
        scan = new Scanner(System.in);
        worldMap = new HashMap<>();
        menu = new Menu(this);
        boardHeight = height;
        boardWidth = width;
    }

    public void spawnLand(Land land) { if (!landExists(land)) worldMap.put(land, null); }
    public void spawnLand(int row, int col) { spawnLand(new Land(row, col)); }
    public void spawnObject(Land land, Object obj) { moveTo(land, obj); }

    public boolean landExists(Land land) { return worldMap.containsKey(land); }
    public Land getLandByOccupant(Object obj) {
        if (obj == null || !worldMap.containsValue(obj)) return null;
        for (var entry : worldMap.entrySet()) {
            if (obj.equals(entry.getValue())) return entry.getKey();
        }
        return null;
    }
    public boolean moveTo(Land land, Object obj) {
        if (obj == null) return false;
        Land prev = getLandByOccupant(obj);
        Object other = worldMap.get(land);
        if (prev != null) worldMap.put(prev, null);    // remove obj from previous location
        if (other == null) worldMap.put(land, obj);
        else {
            if (obj instanceof Humanoid h) {
                worldMap.put(prev, h.encounter(other));
                if (!h.hasDied()) {
                    worldMap.put(land, obj);
                    return true;
                }
            }
            else if (other instanceof Humanoid h) h.encounter(obj);
            return false;
        }
        return true;
    }
    public List<Humanoid> getUnits() { return worldMap.values().stream()
            .filter(o -> o instanceof Humanoid).map(o -> (Humanoid) o).toList(); }
    public List<Item> getItems() { return worldMap.values().stream()
            .filter(o -> o instanceof Item).map(o -> (Item) o).toList(); }

    // Play Turn: iterate through each unit attempting to move them
    public void playTurn() {
        // May need to switch to an iterator instead of for loop so units can be removed during loop    // or record units to remove at end of loop
        for (var unit : getUnits()) {        // TODO Use iterator
            if (!worldMap.containsValue(unit)) continue;
            menu.displayMenu(unit);
            displayMap();
        }
    }

    public void movement(Humanoid unit) {       // TODO: move elsewhere
        List<String> directions = new ArrayList<>();
        Land current = getLandByOccupant(unit);
        String direction;
        if (landExists(current.getNorth())) directions.add("n");
        if (landExists(current.getSouth())) directions.add("s");
        if (landExists(current.getEast())) directions.add("e");
        if (landExists(current.getWest())) directions.add("w");
        if (unit.isNPC()) {
            direction  = directions.get(ThreadLocalRandom.current().nextInt(0, directions.size()));
        } else {
            do {
                direction = getDirection(String.format("Please choose a direction to move (%s%s%s%s)",
                        (directions.contains("n"))?"North, ":"", (directions.contains("e"))?"East, ":"",
                        (directions.contains("s"))?"South, ":"", (directions.contains("w"))?"West":"")).toLowerCase();
            } while (!directions.contains(Character.toString(direction.charAt(0)).toLowerCase()));
        }

        Land land = switch (direction) {
            case "n", "north" -> current.getNorth();
            case "e", "east" -> current.getEast();
            case "w", "west" -> current.getWest();
            case "s", "south" -> current.getSouth();
            default -> null;
        };
        moveTo(land, unit);
    }

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

    public boolean contains(Human human) {
        return getUnits().contains(human);
    }
    public boolean contains(List<Human> humans) {
        if (humans == null || humans.size() < 1) return false;
        for (Human h : humans) if (getUnits().contains(h)) return true;
        return false;
    }

    public boolean hasEnemies() {
        for (var unit : getUnits()) if (unit instanceof Goblin) return true;
        return false;
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

    public static boolean isDirection(String response) {
        if (response == null || response.isEmpty()) return false;
        return response.equalsIgnoreCase("n") || response.equalsIgnoreCase("north") ||
                response.equalsIgnoreCase("e") || response.equalsIgnoreCase("east") ||
                response.equalsIgnoreCase("s") || response.equalsIgnoreCase("south") ||
                response.equalsIgnoreCase("w") || response.equalsIgnoreCase("west");
    }

    public static String getDirection(String message) {
        String direction = ensureInput(message, "Problem receiving input");
        if (!isDirection(direction)) {
            System.out.printf("%s is not a valid direction, please try again%n", direction);
            return getDirection(message);
        }
        return direction;
    }
}
