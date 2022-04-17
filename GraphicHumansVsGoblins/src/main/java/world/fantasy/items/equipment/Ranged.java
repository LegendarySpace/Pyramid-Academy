package world.fantasy.items.equipment;

import world.fantasy.world.World;

public class Ranged extends Weapon {
    public Ranged(World world, int damage) {
        super(world, damage);
        setName("Bow of Blessings");
        setImagePath("/ranged.png");
    }

}
