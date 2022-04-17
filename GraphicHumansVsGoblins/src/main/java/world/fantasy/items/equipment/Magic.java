package world.fantasy.items.equipment;

import world.fantasy.world.World;

public class Magic extends Weapon {
    public Magic(World world, int damage) {
        super(world, damage);
        setName("Wand of witchcraft");
        setImagePath("/magic.png");
    }

}
