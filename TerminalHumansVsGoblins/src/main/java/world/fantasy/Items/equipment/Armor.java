package world.fantasy.Items.equipment;
import world.fantasy.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class Armor extends Item {
    private int defence;

    public Armor(int defence) {
        this.defence = defence;
    }

    public int getDefence() { return defence; }
    public void setDefence(int defence) { this.defence = defence; }

    @Override
    public String toString() {
        return Character.toString('\u2645');
    }

    @Override
    public List<String> getMenu() {
        var list = new ArrayList<>(super.getMenu());
        list.add("equip");
        return list;
    }
}
