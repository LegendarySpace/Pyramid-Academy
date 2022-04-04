package world.fantasy.items.equipment;
import world.fantasy.items.ItemOption;
import world.fantasy.Menu;
import world.fantasy.creatures.GearSlot;
import world.fantasy.world.World;

import java.util.List;

public class Armor extends Equipment {

    public Armor(World world, int defence) {
        super(world, defence);
    }

    public int getDefence() { return getTotal(); }

    @Override
    public String toString() {
        return Character.toString('\u2645');
    }

    @Override
    public String getName() {
        return "Armor of protecting";
    }

    @Override
    public List<GearSlot> availableSlots() {
        return getOwner().getArmorSlots();
    }

    @Override
    public void activate(ItemOption option) {
        super.activate(option);
        if (getOwner() == null) return;
        switch (option) {
            case EQUIP -> getOwner().wearEquipment(Menu.getEquipmentSlot(getOwner().getArmorSlots()), this);
        }
    }

}
