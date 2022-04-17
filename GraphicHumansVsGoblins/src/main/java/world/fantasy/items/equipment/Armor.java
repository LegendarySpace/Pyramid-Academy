package world.fantasy.items.equipment;
import world.fantasy.items.ItemOption;
import world.fantasy.creatures.GearSlot;
import world.fantasy.world.World;

import java.util.List;

public class Armor extends Equipment {

    public Armor(World world, int defence) {
        super(world, defence);
        setName("Armor of protecting");
        setImagePath(chooseImage());
    }

    public int getDefence() { return getTotal(); }

    protected String chooseImage() {
        if (getBonus() < 1) return "/armor.png";
        return "/armor+.png";
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
            case EQUIP -> getOwner().autoEquip(this);
        }
    }

}
