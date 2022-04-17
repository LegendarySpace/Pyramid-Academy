package world.fantasy.items.equipment;

import world.fantasy.world.World;

public class Shield extends Weapon {
    public Shield(World world, int damage) {
        super(world, damage);
        setName("Shield of Defending");
        setImagePath(chooseImage());
    }

    protected String chooseImage() {
        if (getBonus() < 1) return "/shield.png";
        if (getBonus() < 2) return "/shield+.png";
        return "/dragonShield.png";
    }

}
