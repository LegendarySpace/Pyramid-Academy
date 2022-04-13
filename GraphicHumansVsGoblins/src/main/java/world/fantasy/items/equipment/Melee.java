package world.fantasy.items.equipment;

import world.fantasy.world.World;

public class Melee extends Weapon {
    public Melee(World world, int damage) {
        super(world, damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u2694');
    }

    @Override
    public String getName() { return "Sword of slashing"; }

    @Override
    public String getImagePath() {
        return "D:\\Pyramid-Academy\\GraphicHumansVsGoblins\\src\\main\\resources\\world\\fantasy\\images\\melee.png";
    }

}
