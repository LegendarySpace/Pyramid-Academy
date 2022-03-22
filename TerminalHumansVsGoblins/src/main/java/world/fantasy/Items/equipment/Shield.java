package world.fantasy.Items.equipment;

public class Shield extends Equipment {
    public Shield(int damage) {
        super(damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u26c9');
        // alternate shield (may be used for heal spot instead): '\u26e8'
    }
}
