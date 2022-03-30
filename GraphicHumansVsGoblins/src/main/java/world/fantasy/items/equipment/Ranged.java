package world.fantasy.items.equipment;

public class Ranged extends Weapon {
    public Ranged(int damage) {
        super(damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u21a3');
    }

    @Override
    public String name() { return "Bow of Blessings"; }
}
