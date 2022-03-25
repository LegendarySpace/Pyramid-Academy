package world.fantasy.Items.equipment;

import world.fantasy.Items.Item;
import world.fantasy.Items.ItemOption;
import world.fantasy.World;
import world.fantasy.creatures.GearSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Equipment extends Item {
    private final int value;
    private final int bonus;

    public Equipment(int value) {
        this.value = value;
        int x = ThreadLocalRandom.current().nextInt( 100);
        if (x < 1) this.bonus = 2;
        else if (x < 10) this.bonus = 1;
        else this.bonus = 0;
    }

    public Equipment(int value, int bonus) {
        this.value = value;
        this.bonus = bonus;
    }

    public int getTotal() {
        return value + bonus;
    }
    public int getValue() { return value; }
    public int getBonus() { return bonus; }

    public boolean equip() {
        return getOwner() != null && getOwner().wearEquipment(equipTo(), this);
    }

    public GearSlot equipTo() {
        if (getOwner() == null) return null;
        String message = String.format("Current Gear: \n%sWhat slot will you equip to? (%s)", getOwner().displayEquipment(), availableSlots());
        String error = "Error receiving player input";
        String res = World.ensureInput(message, error);
        for (var gs: availableSlots()) if (gs.name().equalsIgnoreCase(res) || gs.name().substring(0,1).equalsIgnoreCase(res)) return gs;
        System.out.printf("%s was not a valid slot", res);
        return equipTo();
    }

    public boolean unequip() { return false;}

    public abstract List<GearSlot> availableSlots();

    @Override
    public String toString() {
        return Character.toString('\u2692');
    }

    @Override
    public String toDetailedString() {
        return "Item that can be equipped";
    }

    @Override
    public void activate(ItemOption option) {
        super.activate(option);
        if (getOwner() == null) return;
        switch (option) {
            case UNEQUIP -> getOwner().removeEquipment(this);
        }
    }

    public GearSlot getEquipmentSlot(List<GearSlot> possible) {
        String message = String.format("Please choose an equipment slot (%s)",
                String.join(", ", possible.stream().map(Enum::name).toArray(String[]::new)));
        String error = "Error receiving input";
        String input = World.ensureInput(message, error);
        for (var slot : possible) if (slot.name().equalsIgnoreCase(input) || slot.name().substring(0,1).equalsIgnoreCase(input)) return slot;
        System.out.printf("%s is not a valid equipment slot\n", input);
        return getEquipmentSlot(possible);
    }

    @Override
    public List<ItemOption> getMenu() {
        var list = new ArrayList<>(super.getMenu());
        list.add(ItemOption.EQUIP);
        list.add(ItemOption.UNEQUIP);
        return list;
    }
}
