package world.fantasy.Items.equipment;
import world.fantasy.Items.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class Equipment extends Item {
    private final int damage;

    public Equipment(int damage) { this.damage = damage; }

    public int getDamage() { return damage; }
    // public void setDamage(int damage) { this.damage = damage; }

    @Override
    public String toString() {
        return Character.toString('\u2692');
    }

    @Override
    public List<String> getMenu() {
        var list = new ArrayList<>(super.getMenu());
        list.add("equip");
        return list;
    }
}
