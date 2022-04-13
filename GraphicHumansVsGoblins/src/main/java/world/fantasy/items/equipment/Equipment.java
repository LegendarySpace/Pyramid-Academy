package world.fantasy.items.equipment;

import world.fantasy.items.Item;
import world.fantasy.items.ItemOption;
import world.fantasy.world.World;
import world.fantasy.creatures.GearSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Equipment extends Item {
    private final int value;
    private final int bonus;

    public Equipment(World world, int value) {
        super(world);
        setName("Item that can be equipped");
        this.value = value;
        int x = ThreadLocalRandom.current().nextInt( 100);
        if (x < 1) this.bonus = 2;
        else if (x < 10) this.bonus = 1;
        else this.bonus = 0;
    }

    public Equipment(World world, int value, int bonus) {
        super(world);
        setName("Item that can be equipped");
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

    // TODO: This needs to use gui instead
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
        return getName() + ((getBonus() > 0)? " +" + getBonus():"");
    }

    @Override
    public void activate(ItemOption option) {
        super.activate(option);
        if (getOwner() == null) return;
        switch (option) {
            case UNEQUIP -> {
                System.out.printf("Unequipping %s %n", this);
                getOwner().removeEquipment(this);
            }
        }
    }

    @Override
    public List<ItemOption> getMenu() {
        var list = new ArrayList<>(super.getMenu());
        list.add(ItemOption.EQUIP);     // Should only show up if not equipped
        list.add(ItemOption.UNEQUIP);   // should only show up if equipped
        return list;
    }

    @Override
    public String getImagePath() {
        return "D:\\Pyramid-Academy\\GraphicHumansVsGoblins\\src\\main\\resources\\world\\fantasy\\images\\equipment.png";
    }

}
