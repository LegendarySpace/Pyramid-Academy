package world.fantasy;

import javafx.scene.image.Image;
import world.fantasy.world.Land;
import world.fantasy.world.World;

public class Actor {
    protected String name;
    protected final World world;
    protected Land position;
    protected String description;
    protected String imagePath;


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
        if (name == null) return;
        this.name = name;
    }

    public Actor(World world) {
        this(world, new Land(0,0));
    }

    public Actor(World world, Land position) {
        name = "Actor";
        description = "Object in space";
        setImagePath("/actor.png");
        this.world = world;
        this.position = position;
    }

    public boolean move(Land moveTo) {
        if (!world.landExists(moveTo)) return false;
        var actors = world.actors.stream().filter(x -> x.getPosition().equals(moveTo)).toList();
        if (actors.size() > 0) {
            for (var act : actors) {
                if (act == this) continue;
                var a = encounter(act);
                // while (a != null) a = encounter(a);
            }
        }
        if (doesExist()) setPosition(moveTo);
        world.gc();
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

    public void setImagePath(String path) {
        if (path == null) return;
        imagePath = path;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Image loadImage() {
        try {
            return new Image(getImagePath());
        } catch (Exception e) {
            System.out.printf("Image invalid for %s%n", getName());
            return null;
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getDescription() {
        return description;
    }
}
