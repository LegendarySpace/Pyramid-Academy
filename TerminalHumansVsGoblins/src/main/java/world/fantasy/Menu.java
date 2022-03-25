package world.fantasy;

import world.fantasy.Items.Item;
import world.fantasy.Items.ItemOption;
import world.fantasy.Items.consumable.Consumable;
import world.fantasy.Items.equipment.Equipment;
import world.fantasy.creatures.Humanoid;

public class Menu {
    World world;
    public Menu(World world) {
        this.world = world;
    }

    public void displayMenu(Humanoid unit) {
        if (unit == null) {
            System.out.println("A unit is required to display this menu");
            return;
        }
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
                    case MOVE -> world.movement(unit);
                    case INVENTORY -> inventoryMenu(unit);
                    case GEAR -> gearMenu(unit);
                    case SPELLS -> gearMenu(null); // TODO: make operational
                    case QUIT -> System.exit(0);
                }

            } else displayMenu(unit);
        }
    }

    public void inventoryMenu(Humanoid unit) {
        if (unit == null) {
            System.out.println("A unit is required to display this menu");
            return;
        }
        String message = String.format("Please select an item: \n%s", unit.displayInventory());
        String error = "Trouble receiving input, please try again";
        int input = World.ensureIntInput(message, error);
        if (input < 0 || input >= unit.getInventory().size()) {
            System.out.printf("%d is not a valid item\n", input);
            inventoryMenu(unit);
        }
        else itemMenu(unit.getItem(input));
    }

    public void gearMenu(Humanoid unit) {
        if (unit == null) {
            System.out.println("A unit is required to display this menu");
            return;
        }
        String message = String.format("Please select an equipment slot: \n%s", unit.displayEquipment());
        String error = "Trouble receiving input, please try again";
        String input = World.ensureInput(message, error);
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

    public void itemMenu(Item item) {
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
}
