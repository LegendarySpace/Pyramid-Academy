package world.fantasy.items.equipment;
import world.fantasy.items.ItemOption;
import world.fantasy.creatures.GearSlot;
import world.fantasy.world.World;

import java.util.List;

public abstract class Weapon extends Equipment {

    public Weapon(World world, int damage) {
        super(world, damage);
        setName("Weapon of killing");
        setImagePath("/weapon.png");
    }

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
            case EQUIP -> getOwner().autoEquip(this);
        }
    }

}
