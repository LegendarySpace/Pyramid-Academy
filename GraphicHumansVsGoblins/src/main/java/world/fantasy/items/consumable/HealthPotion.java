package world.fantasy.items.consumable;

import world.fantasy.world.World;

public class HealthPotion extends Consumable{
    public HealthPotion(World world) {
        super(world);
    }

    @Override
    public void use() {
        getOwner().setHealth(getOwner().getHealth() + 10);
    }

    @Override
    public String toDetailedString() {
        return "a potion used to increase health by 10";
    }

    @Override
    public String getImagePath() {
        return "D:\\Pyramid-Academy\\GraphicHumansVsGoblins\\src\\main\\resources\\world\\fantasy\\images\\healthPotion.png";
    }

}
