package world.fantasy.items.equipment;

import world.fantasy.world.World;

public class Ranged extends Weapon {
    public Ranged(World world, int damage) {
        super(world, damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u21a3');
    }

    @Override
    public String getName() { return "Bow of Blessings"; }

    @Override
    public String getImagePath() {
        return "D:\\Pyramid-Academy\\GraphicHumansVsGoblins\\src\\main\\resources\\world\\fantasy\\images\\ranged.png";
    }

}
