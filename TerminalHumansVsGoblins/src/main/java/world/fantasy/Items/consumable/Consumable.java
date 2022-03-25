package world.fantasy.Items.consumable;

import world.fantasy.Items.Item;
import world.fantasy.Items.ItemOption;

import java.util.ArrayList;
import java.util.List;

public class Consumable extends Item {
    public void use() {
    }

    @Override
    public List<ItemOption> getMenu() {
        var list = new ArrayList<>(super.getMenu());
        list.add(ItemOption.USE);
        return list;
    }

    @Override
    public String toDetailedString() {
        return "A consumable item good for one use";
    }

    @Override
    public void activate(ItemOption option) {
        super.activate(option);
        if (option == ItemOption.USE) use();
    }
}
