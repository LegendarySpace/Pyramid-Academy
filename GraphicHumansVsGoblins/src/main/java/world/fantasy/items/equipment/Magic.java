package world.fantasy.items.equipment;

import world.fantasy.world.World;

public class Magic extends Weapon {
    public Magic(World world, int damage) {
        super(world, damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u21dd');
    }

    @Override
    public String getName() { return "Wand of witchcraft"; }
}
