package world.fantasy;

import world.fantasy.items.Item;
import world.fantasy.items.ItemOption;
import world.fantasy.creatures.GearSlot;
import world.fantasy.creatures.Creature;
import world.fantasy.world.World;

import java.util.List;

public class Menu {
    World world;
    public Menu(World world) {
        this.world = world;
    }

    public void displayMenu(Creature unit) {
        if (unit == null) {
            System.out.println("A unit is required to display this menu");
            return;
        }
        world.displayMap();
        String input;
        if (unit.isNPC()) input = unit.determineAction().name();
        else {
            String options = String.join(", ", unit.menuOptions().stream().map(Enum::name).toList());
            String message = String.format("Please select an option: (%s)", options);
            String error = "Trouble receiving input, please try again";
            input = World.ensureInput(message, error);
        }
        for (var opt : unit.menuOptions()) {
            // Validate input
            if (opt.name().equalsIgnoreCase(input) || opt.name().substring(0,1).equalsIgnoreCase(input)) {
                // Process input
                switch (opt) {
                    case MOVE -> {
                        world.movement(unit);  // TODO: remove from world, not sure where to put
                        return;
                    }
                    case INVENTORY -> inventoryMenu(unit);
                    case GEAR -> gearMenu(unit);
                    case SPELLS -> gearMenu(null); // TODO: make operational
                    case QUIT -> System.exit(0);
                }
                displayMenu(unit);
                return;
            }
        }
        System.out.printf("%s is not a valid menu option\n", input);
        displayMenu(unit);
    }

    public static void inventoryMenu(Creature unit) {
        if (unit == null || unit.getInventory().size() < 1) {
            System.out.println("There is no inventory to display");
            return;
        }
        String message = String.format("Please select an item: %s", unit.displayInventory());
        String error = "Trouble receiving input, please try again";
        int input = World.ensureIntInput(message, error);
        if (input < 0 || input >= unit.getInventory().size()) {
            System.out.printf("%d is not a valid item\n", input);
            inventoryMenu(unit);
        }
        else itemMenu(unit.getItem(input));
    }

    public static void gearMenu(Creature unit) {
        if (unit == null) {
            System.out.println("A unit is required to display this menu");
            return;
        }
        String message = String.format("Please select an equipment slot: %s", unit.displayEquipment());
        String error = "Trouble receiving input, please try again";
        String input = World.ensureInput(message, error);
        if (input.equalsIgnoreCase("back") || input.equalsIgnoreCase("quit")) return;
        for (var opt : unit.getAllSlots()) {
            if (opt.name().equalsIgnoreCase(input) || opt.name().substring(0,1).equalsIgnoreCase(input)) {
                itemMenu(switch (opt) {
                    case ARMOR -> unit.getArmor();
                    case MAINHAND -> unit.getMainHand();
                    case OFFHAND -> unit.getOffHand();
                });
                return;
            }
        }
        System.out.printf("%s is not a valid slot", input);
        gearMenu(unit);
    }

    public static void itemMenu(Item item) {
        if (item == null) {
            System.out.println("An item is required to display menu");
            return;
        }
        String message = String.format("Please select an option: \n%s", String.join(", ", item.getMenu().stream().map(Enum::name).toList()));
        String error = "Trouble receiving input, please try again";
        String input = World.ensureInput(message, error);
        for (var opt : item.getMenu()) {
            // Validate input
            if (opt.name().equalsIgnoreCase(input) || opt.name().substring(0,1).equalsIgnoreCase(input)) {
                if (opt == ItemOption.QUIT) System.exit(0);
                if (opt != ItemOption.BACK) item.activate(opt);
                return;
            }
        }
        System.out.printf("%s is not a valid option, please try again", input);
        itemMenu(item);
    }

    public static GearSlot getEquipmentSlot(List<GearSlot> possible) {
        if (possible == null || possible.isEmpty()) {
            System.out.println("Cannot find gear slots");
            return null;
        }
        String message = String.format("Please choose an equipment slot (%s)",
                String.join(", ", possible.stream().map(Enum::name).toArray(String[]::new)));
        String error = "Error receiving input";
        String input = World.ensureInput(message, error);
        for (var slot : possible) if (slot.name().equalsIgnoreCase(input) || slot.name().substring(0,1).equalsIgnoreCase(input)) return slot;
        System.out.printf("%s is not a valid equipment slot\n", input);
        return getEquipmentSlot(possible);
    }

}
