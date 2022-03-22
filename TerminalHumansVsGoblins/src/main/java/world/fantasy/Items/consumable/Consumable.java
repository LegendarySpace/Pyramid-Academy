package world.fantasy.Items.consumable;

import world.fantasy.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class Consumable extends Item {
    public void use() {
    }

    @Override
    public List<String> getMenu() {
        var list = new ArrayList<>(super.getMenu());
        list.add("use");
        return list;
    }
}
