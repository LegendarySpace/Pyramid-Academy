package world.fantasy;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import world.fantasy.world.Land;
import world.fantasy.world.World;

public class Actor {
    protected String name;
    protected final World world;
    protected Land position;
    protected String description;
    protected String imagePath;

    private UnitPane unitPane;


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
        this(world, position, "/actor.png");
    }

    public Actor(World world, Land position, String imageURI) {
        if (world == null) throw new RuntimeException("World not valid");
        name = "Actor";
        description = "Object in space";
        setImagePath(imageURI);
        this.world = world;
        this.position = position;
        unitPane = createUnitPane();
    }

    public boolean move(Land moveTo) {
        if (!world.landExists(moveTo)) return false;
        var actors = world.actors.stream().filter(x -> x.getPosition().equals(moveTo)).toList();
        if (!actors.isEmpty()) {
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
        getUnitPane().updateImage(path);
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

    public UnitPane getUnitPane() {
        return unitPane;
    }

    public UnitPane getUnitPane(int size) {
        var p = getUnitPane();
        p.setSize(size);
        return p;
    }

    private UnitPane createUnitPane() {
        return new UnitPane(getImagePath());
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getDescription() {
        return description;
    }

    private class UnitPane extends StackPane {

        private ImageView background;
        private Label info;

        public UnitPane(String imageURI) {
            background = new ImageView(imageURI);
        }

        public UnitPane(Node... nodes) {
            background = new ImageView();
        }

        public void updateImage(String imageURI) {
            background.setImage(new Image(imageURI));
        }

        public void updateText() {
            info.setText("");
        }
        public void updateText(String text) {
            info.setText(text);
        }

        public void setSize(int size) {
            background.setFitWidth(size);
            background.setFitHeight(size);
            // Resize info label
        }
    }

}
