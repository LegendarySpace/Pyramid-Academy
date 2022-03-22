package world.fantasy.Items.equipment;

public class Ranged extends Equipment {
    public Ranged(int damage) {
        super(damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u21a3');
    }
}
