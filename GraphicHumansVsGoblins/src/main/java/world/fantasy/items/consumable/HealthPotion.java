package world.fantasy.items.consumable;

import world.fantasy.world.World;

public class HealthPotion extends Consumable{
    public HealthPotion(World world) {
        super(world);
        setName("Health Potion");
        description = "a potion used to increase health by 10";
        setImagePath("/healthPotion.png");
    }

    @Override
    public void use() {
        getOwner().setHealth(getOwner().getHealth() + 10);
    }

}
