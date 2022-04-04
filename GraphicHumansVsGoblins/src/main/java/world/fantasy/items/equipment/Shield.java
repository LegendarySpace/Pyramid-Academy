package world.fantasy.items.equipment;

import world.fantasy.world.World;

public class Shield extends Weapon {
    public Shield(World world, int damage) {
        super(world, damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u26c9');
        // alternate shield (may be used for heal spot instead): '\u26e8'
    }

    @Override
    public String getName() { return "Shield of Defending"; }
}
