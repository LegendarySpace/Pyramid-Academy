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
        setImagePath("/equipment.png");
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
        return getOwner() != null && getOwner().autoEquip(this);
    }

    public boolean unequip() { return false;}

    public abstract List<GearSlot> availableSlots();

    @Override
    public String toString() {
        return super.toString() + ((getBonus() > 0)? " +" + getBonus():"");
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

}
