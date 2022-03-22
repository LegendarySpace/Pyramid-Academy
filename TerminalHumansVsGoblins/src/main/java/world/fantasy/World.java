package world.fantasy;

import world.fantasy.Items.consumable.Consumable;
import world.fantasy.Items.equipment.Armor;
import world.fantasy.Items.equipment.Equipment;
import world.fantasy.creatures.Goblin;
import world.fantasy.creatures.Human;
import world.fantasy.creatures.Humanoid;
import world.fantasy.Items.Item;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class World {
    public static Scanner scan;
    private final HashMap<Land, Object> worldMap;
    public final int boardHeight, boardWidth;
    private final List<Humanoid> units;   // all characters in existence
    private final List<Item> items;        // all items on world map
    // Holds information about all objects in the world and maps their position on the map

    World(int height, int width) {
        scan = new Scanner(System.in);
        worldMap = new HashMap<>();
        boardHeight = height;
        boardWidth = width;
        units = new ArrayList<>();
        items = new ArrayList<>();
    }

    public void spawnLand(Land land) { if (!landExists(land)) worldMap.put(land, null); }

    public void spawnObject(Land land, Object obj) {
        worldMap.put(land, obj);
        if (obj instanceof Humanoid h) units.add(h);
        if (obj instanceof Item i) items.add(i);
    }

    public boolean landExists(Land land) { return worldMap.containsKey(land); }

    public Land getLandFromUnit(Humanoid unit) {
        if (unit == null || !worldMap.containsValue(unit)) return null;
        for (var entry : worldMap.entrySet()) {
            if (unit.equals(entry.getValue())) return entry.getKey();
        }
        return null;
    }

    public Land getLandFromItem(Item item) {
        if (item == null || !worldMap.containsValue(item)) return null;
        for (var entry : worldMap.entrySet()) {
            if (item.equals(entry.getValue())) return entry.getKey();
        }
        return null;
    }

    public boolean moveTo(Humanoid unit, Land land) {
        if (!landExists(land) || unit == null) return false;
        Land startPoint = getLandFromUnit(unit);
        Object overlapObject = worldMap.get(land);

        if (startPoint != null) worldMap.remove(startPoint);
        Item drop = unit.encounter(overlapObject);
        if (unit.getHealth() > 0) worldMap.put(land, unit);
        if (drop != null && startPoint != null) worldMap.put(startPoint, drop);

        // if worldMap does not contain overlap then remove it
        if (!worldMap.containsValue(overlapObject)) {
            if (overlapObject instanceof Humanoid h) units.remove(h);
            if (overlapObject instanceof Item i) items.remove(i);
        }
        return true;
    }

    // Play Turn: iterate through each unit attempting to move them
    public void playTurn() {
        // May need to switch to an iterator instead of for loop so units can be removed during loop    // or record units to remove at end of loop
        for (var unit : units) {        // TODO Use iterator
            if (!worldMap.containsValue(unit)) continue;
            if (!unit.isNPC()) showTurnMenu(unit);
            movement(unit);
            displayMap();
        }
        for (int i = units.size() - 1; i >=0; i--) {
            // if worldMap does not contain unit, remove it
            if (!worldMap.containsValue(units.get(i))) units.remove(units.get(i));
        }
    }

    private void showTurnMenu(Humanoid unit) {
        // Options each turn will be: move, check inventory, check equipment, cast spells
        String message = "Please select an action (Move, Inventory, Equipment)";
        String error = "Error receiving input";
        String menuInput = ensureInput(message, error);
        if (validTurnMenu(menuInput)) { if (processTurnMenuOption(menuInput, unit)) return; }
        else {
            System.out.println("Not a valid menu option");
        }
        showTurnMenu(unit);
    }

    private boolean validTurnMenu(String option) {
        if (option == null || option.isEmpty()) return false;
        return option.equalsIgnoreCase("m") || option.equalsIgnoreCase("move") ||
                option.equalsIgnoreCase("i") || option.equalsIgnoreCase("inventory") ||
                option.equalsIgnoreCase("e") || option.equalsIgnoreCase("equipment");
    }

    private boolean processTurnMenuOption(String option, Humanoid unit) {
        if (option == null || option.isEmpty()) return false;
        // return true only if option is move
        switch (option.toLowerCase()) {
            case "m":
            case "move": return true;
            case "i":
            case "inventory":
                // First select an item in inventory first
                if (unit.getInventory().size() == 0) {
                    System.out.println("Inventory is empty");
                    return false;
                }
                int idx = chooseInventoryItem(unit);
                showInventoryMenu(unit, idx);
                return false;
            case "e":
            case "equipment":
                String str = chooseEquipment(unit);
                showEquipmentMenu(unit, str);
                return false;
            default: return false;
        }
    }

    private int chooseInventoryItem(Humanoid unit) {
        // display inventory with position number each
        String message = unit.displayInventory();
        String error = "Your input was not a valid number";
        int itemNum = ensureIntInput(message, error);
        if (itemNum > -1 && itemNum < unit.getInventory().size()) return itemNum;
        System.out.printf("%s is not a valid item number", itemNum);
        return chooseInventoryItem(unit);
    }

    private void showInventoryMenu(Humanoid unit, int idx) {
        // Options are inspect, equip, use, drop
        var menus = unit.getItem(idx).getMenu();
        StringBuilder sb = new StringBuilder();
        for (String menu: menus) sb.append(" ").append(menu.toLowerCase());
        String message = "Please select an option: ( " + sb.toString() + ")";
        String error = "Trouble receiving input, please try again";
        String menuInput = ensureInput(message, error);
        if (validInventoryMenu(menuInput, menus)) processInventoryMenuOption(menuInput, unit, unit.getItem(idx));
        else {
            System.out.println("Not a valid menu option");
            showInventoryMenu(unit, idx);
        }
    }

    private boolean validInventoryMenu(String choice, List<String> options) {
        if (options == null || options.size() < 1 || choice.isEmpty()) return false;
        List<String> initial = options.stream().map(str -> str.substring(0, 1)).toList();
        return options.contains(choice) || initial.contains(choice);
    }

    private void processInventoryMenuOption(String option, Humanoid unit, Item item) {
        switch (option.toLowerCase()) {
            case "i", "inspect" -> System.out.println(item.toDetailedString());
            case "e", "equip" -> {
                if (item instanceof Armor a) unit.equipArmor(a);
                if (item instanceof Equipment e) {
                    if (unit.getMainHand() == null) unit.equipOffHand(e);
                    else unit.equipMainHand(e);
                }
            }
            case "u", "use" -> unit.useItem(item);
            case "d", "drop" -> unit.dropItem(item);
        }
    }

    private String chooseEquipment(Humanoid unit) {
        if (unit == null) return null;
        // display inventory with position number each
        List<String> slotNames = List.of("a", "armor", "m", "main", "mainhand", "main-hand",
                "o", "off", "offhand", "off-hand");
        String message = unit.displayEquipment() + "Choose a slot (armor, main, off)";
        String error = "Your input was not a valid number";
        String equipment = ensureInput(message, error);
        if (validEquipmentSlot(equipment, slotNames)) {
            if (slotNames.subList(0, 2).contains(equipment)) return "armor";
            if (slotNames.subList(2, 6).contains(equipment)) return "main";
            if (slotNames.subList(6, slotNames.size()).contains(equipment)) return "off";
            return equipment;
        }
        System.out.printf("%s is not a valid equipment slot", equipment);
        return chooseEquipment(unit);
    }

    private boolean validEquipmentSlot(String slot, List<String> list) {
        if (list == null || slot == null || slot.isEmpty()) return false;
        if (slot.equalsIgnoreCase("back") || slot.equalsIgnoreCase("quit")) return true;
        return list.contains(slot.toLowerCase());
    }

    private void showEquipmentMenu(Humanoid unit, String slot) {
        // Options are inspect, unequip, drop, switch hand
        if (slot == null || slot.isEmpty() || slot.equalsIgnoreCase("quit") ||
                slot.equalsIgnoreCase("back")) return;
        List<String> options = getEquipmentOptions(unit, slot);
        String message = String.format("Please select an option (%s)", String.join(", ", options));
        String error = "Error receiving user input";
        String menuInput = ensureInput(message, error);
        if (validEquipmentMenu(menuInput, options)) processEquipmentMenuOption(unit, slot, menuInput);
        else {
            System.out.println("Not a valid menu option");
            showEquipmentMenu(unit, slot);
        }
    }

    private List<String> getEquipmentOptions(Humanoid unit, String slot) {
        List<String> options = new ArrayList<>();
        if (unit == null || slot == null || slot.isEmpty()) return options;
        if ((slot.equalsIgnoreCase("armor") && unit.getArmor() != null) ||
                (slot.equalsIgnoreCase("main") && unit.getMainHand() != null) ||
                (slot.equalsIgnoreCase("off") && unit.getOffHand() != null)) {
            options.add("inspect");
            options.add("unequip");
            options.add("drop");
        }
        options.add("auto");
        return options;
    }

    private boolean validEquipmentMenu(String choice, List<String> options) {
        if (options == null || choice == null || choice.isEmpty()) return false;
        List<String> initials = options.stream().map(str -> str.toLowerCase().substring(0,1)).toList();
        return options.contains(choice.toLowerCase()) || initials.contains(choice.toLowerCase());
    }

    private void processEquipmentMenuOption(Humanoid unit, String slot, String option) {
        if (option.equalsIgnoreCase("inspect") || option.equalsIgnoreCase("i")) {
            switch (slot) {
                case "armor" -> unit.getArmor().toDetailedString();
                case "main" -> unit.getMainHand().toDetailedString();
                case "off" -> unit.getOffHand().toDetailedString();
            }
        } else if (option.equalsIgnoreCase("unequip") || option.equalsIgnoreCase("u")) {
            switch (slot) {
                case "armor" -> unit.unequipArmor();
                case "main" -> unit.unequipMainHand();
                case "off" -> unit.unequipOffHand();
            }
        } else if (option.equalsIgnoreCase("drop") || option.equalsIgnoreCase("d")) {
            switch (slot) {
                case "armor" -> { if (unit.getArmor() != null) unit.dropItem(unit.unequipArmor()); }
                case "main" -> { if (unit.getMainHand() != null) unit.dropItem(unit.unequipMainHand()); }
                case "off" -> { if (unit.getOffHand() != null) unit.dropItem(unit.unequipOffHand()); }
            }
        } else if (option.equalsIgnoreCase("auto") || option.equalsIgnoreCase("a")) {
            switch (slot) {
                case "armor" -> {
                    unit.unequipArmor();
                    var opt = unit.getInventory().stream().filter(itm -> itm instanceof Armor).map(itm -> (Armor) itm)
                            .max(Comparator.comparingInt(Armor::getDefence));
                    opt.ifPresent(unit::equipArmor);
                }
                case "main", "off" -> {
                    unit.unequipMainHand();
                    unit.unequipOffHand();
                    var opt = unit.getInventory().stream().filter(itm -> itm instanceof Equipment).map(itm -> (Equipment) itm)
                            .max(Comparator.comparingInt(Equipment::getDamage));
                    opt.ifPresent(unit::equipMainHand);
                    opt = unit.getInventory().stream().filter(itm -> itm instanceof Equipment).map(itm -> (Equipment) itm)
                            .max(Comparator.comparingInt(Equipment::getDamage));
                    opt.ifPresent(unit::equipOffHand);
                }
            }
        }
    }

    private void movement(Humanoid unit) {
        List<String> directions = new ArrayList<>();
        Land current = getLandFromUnit(unit);
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
        moveTo(unit, land);
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
        return units.contains(human);
    }

    public boolean contains(List<Human> humans) {
        if (humans == null || humans.size() < 1) return false;
        for (Human h : humans) if (units.contains(h)) return true;
        return false;
    }

    public boolean hasEnemies() {
        for (var unit : units) if (unit instanceof Goblin) return true;
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
