package world.fantasy.Items.equipment;
import world.fantasy.Items.ItemOption;

public abstract class Weapon extends Equipment {

    public Weapon(int damage) { super(damage); }

    @Override
    public String toString() {
        return Character.toString('\u2694');
    }

    @Override
    public String toDetailedString() {
        return "A Weapon of killing" + ((getBonus() > 0)? " +" + getBonus():"");
    }

    public int getDamage() { return getTotal(); }


    @Override
    public void activate(ItemOption option) {
        super.activate(option);
        if (getOwner() == null) return;
        switch (option) {
            case EQUIP -> getOwner().wearEquipment(getEquipmentSlot(getOwner().getWeaponSlots()), this);
        }
    }

}
