package world.fantasy.items.equipment;

public class Shield extends Weapon {
    public Shield(int damage) {
        super(damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u26c9');
        // alternate shield (may be used for heal spot instead): '\u26e8'
    }

    @Override
    public String name() { return "Shield of Defending"; }
}
