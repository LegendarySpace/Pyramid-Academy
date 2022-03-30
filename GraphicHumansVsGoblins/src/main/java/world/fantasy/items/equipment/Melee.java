package world.fantasy.items.equipment;

public class Melee extends Weapon {
    public Melee(int damage) {
        super(damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u2694');
    }

    @Override
    public String name() { return "Sword of slashing"; }
}
