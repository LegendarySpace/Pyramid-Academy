package world.fantasy.items.equipment;

public class Magic extends Weapon {
    public Magic(int damage) {
        super(damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u21dd');
    }

    @Override
    public String name() { return "Wand of witchcraft"; }
}
