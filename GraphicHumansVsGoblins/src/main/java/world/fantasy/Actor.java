package world.fantasy;

import world.fantasy.world.Land;
import world.fantasy.world.World;

public class Actor {
    protected String name;
    protected final World world;
    protected Land position;


    public Land getPosition() {
        return position;
    }

    public void setPosition(Land position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Actor(World world) {
        this.world = world;
        this.position = new Land(0,0);
    }

    public Actor(World world, Land position) {
        this.world = world;
        this.position = position;
    }

    public boolean move(Land moveTo) {
        if (!world.landExists(moveTo)) return false;
        var actors = world.actors.stream().filter(x -> x.getPosition().equals(getPosition())).toList();
        if (actors.size() > 0) {
            for (var act : actors) {
                if (act == this) continue;
                var a = encounter(act);
                if (a != null) encounter(a);
            }
        }
        if (doesExist()) setPosition(moveTo);
        return true;
    }

    public boolean doesExist() {
        return getPosition() != null;
    }

    public Actor encounter(Actor other) {
        // 2 items collide, return any newly created actor (mostly items, maybe spawns)
        setPosition(null);
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
