package world.fantasy.Items;

import world.fantasy.creatures.Humanoid;

import java.util.List;

public class Item {
    // This will include dropped item and treasure chests
    // toString gives general information (what type of item)
    // toDetailedString gives stat information (can only be called if in possession)
    private Humanoid owner;

    public Humanoid getOwner() { return owner; }
    public void setOwner(Humanoid owner) { this.owner = owner; }

    @Override
    public String toString() {
        return Character.toString('\u26b1');
    }

    public String toDetailedString() {
        return "Item";
    }

    public List<String> getMenu() {
        return List.of("inspect", "drop");
    }
}
