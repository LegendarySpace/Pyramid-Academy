package world.fantasy.Items.equipment;

public class Melee extends Equipment {
    public Melee(int damage) {
        super(damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u2694');
    }
}