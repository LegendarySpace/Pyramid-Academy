package world.fantasy.items.consumable;

import world.fantasy.items.Item;
import world.fantasy.items.ItemOption;
import world.fantasy.world.World;

import java.util.ArrayList;
import java.util.List;

public class Consumable extends Item {
    public Consumable(World world) {
        super(world);
    }

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

    @Override
    public String getImagePath() {
        return "D:\\Pyramid-Academy\\GraphicHumansVsGoblins\\src\\main\\resources\\world\\fantasy\\images\\consumable.png";
    }

}
