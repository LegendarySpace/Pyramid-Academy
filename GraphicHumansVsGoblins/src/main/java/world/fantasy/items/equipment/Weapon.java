package world.fantasy.items.equipment;
import world.fantasy.items.ItemOption;
import world.fantasy.Menu;
import world.fantasy.creatures.GearSlot;

import java.util.List;

public abstract class Weapon extends Equipment {

    public Weapon(int damage) { super(damage); }

    @Override
    public String toString() {
        return Character.toString('\u2694');
    }

    @Override
    public String name() { return "Weapon of killing"; }

    public int getDamage() { return getTotal(); }

    @Override
    public List<GearSlot> availableSlots() {
        return getOwner().getWeaponSlots();
    }

    @Override
    public void activate(ItemOption option) {
        super.activate(option);
        if (getOwner() == null) return;
        switch (option) {
            case EQUIP -> getOwner().wearEquipment(
                    Menu.getEquipmentSlot(
                            getOwner().getWeaponSlots()),
                    this);
        }
    }

}
