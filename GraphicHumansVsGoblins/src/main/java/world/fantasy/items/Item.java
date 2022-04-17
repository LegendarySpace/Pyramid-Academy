package world.fantasy.items;

import world.fantasy.Actor;
import world.fantasy.creatures.Creature;
import world.fantasy.world.World;

import java.util.List;

public class Item extends Actor {
    // This will include dropped item and treasure chests
    // toString gives general information (what type of item)
    // toDetailedString gives stat information (can only be called if in possession)
    private Creature owner;

    public Item(World world) {
        super(world);
        setName("Item");
        setImagePath("/item.png");
    }

    public Creature getOwner() { return owner; }
    public void setOwner(Creature owner) { this.owner = owner; }

    public List<ItemOption> getMenu() {
        return List.of(ItemOption.INSPECT, ItemOption.DROP);
    }

    public void drop() {
        if (getOwner() != null) getOwner().dropItem(this);
    }

    public void activate(ItemOption option) {
        switch (option) {
            case INSPECT -> toString();         // UPDATE: This should update text on some view
            case DROP -> drop();
        }
    }
}
