package world.fantasy.items.consumable;

import world.fantasy.world.World;

public class HealthPotion extends Consumable{
    private int value = 10;
    private float multiplier = 1.f;
    public HealthPotion(World world) {
        super(world);
        setName("Health Potion");
        description = "a potion used to increase health by 10";
        setImagePath("/healthPotion.png");
    }

    @Override
    public void use() {
        getOwner().applyDamage(-1 * (int)(value * multiplier), true);
    }

}
