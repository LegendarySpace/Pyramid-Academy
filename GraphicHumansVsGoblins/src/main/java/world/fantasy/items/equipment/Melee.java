package world.fantasy.items.equipment;

import world.fantasy.world.World;

public class Melee extends Weapon {
    public Melee(World world, int damage) {
        super(world, damage);
        setName("Sword of slashing");
        setImagePath("/melee.png");
    }

}
