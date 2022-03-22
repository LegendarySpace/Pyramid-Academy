package world.fantasy.Items.equipment;

public class Magic extends Equipment {
    public Magic(int damage) {
        super(damage);
    }

    @Override
    public String toString() {
        return Character.toString('\u21dd');
    }
}
