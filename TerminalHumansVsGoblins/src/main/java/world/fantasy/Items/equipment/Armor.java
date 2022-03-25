package world.fantasy.Items.equipment;
import world.fantasy.Items.ItemOption;
import world.fantasy.creatures.GearSlot;

import java.util.List;

public class Armor extends Equipment {

    public Armor(int defence) {
        super(defence);
    }

    @Override
    public List<GearSlot> availableSlots() {
        return null;
    }

    public int getDefence() { return getTotal(); }

    @Override
    public String toString() {
        return Character.toString('\u2645');
    }

    @Override
    public String toDetailedString() {
        return "Armor of protecting" + ((getBonus() > 0)? " +" + getBonus():"");
    }

    @Override
    public void activate(ItemOption option) {
        super.activate(option);
        if (getOwner() == null) return;
        switch (option) {
            case EQUIP -> getOwner().wearEquipment(getEquipmentSlot(getOwner().getArmorSlots()), this);
        }
    }

}
