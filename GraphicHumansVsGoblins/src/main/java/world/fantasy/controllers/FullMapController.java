package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import world.fantasy.Actor;
import world.fantasy.Gate;
import world.fantasy.creatures.Creature;
import world.fantasy.world.Direction;

public class FullMapController {
    public Map map;

    @FXML
    private VBox move;

    @FXML
    private MoveInputController moveController;

    public void createMap(int size, PlayerTurnController turncon) {
        display.getChildren().clear();
        moveController.setTurnCon(turncon);
        map = new Map(size);
        display.getChildren().add(0, map);
        display.getChildren().add(1, move);
    }

    public void updateTileContents() {
        // Reset display, for each actor in the world update the display at their position
        map.reset();
        for (Actor actor :Gate.getInstance().getWorld().actors) map.updateTile(actor);
    }

    public void updateMove(Creature unit) {
        var corner = unit.getPosition().getDirection(Direction.NORTHWEST);
        var size = map.calculateTileSize(map.rows);

        move.setLayoutX(corner.getColumn() * size);
        move.setLayoutY(corner.getRow() * size);
        moveController.update();
    }

    private class Map extends VBox {
        public static final int MIN_SIZE = 32;
        public static final int MAX_SIZE = 128;

        private int rows;

        private int cols;

        public Map(int size) {
            addRows(calculateTileSize(size), rows, cols);
            moveController.setTileSize(calculateTileSize(size));
        }

        public int calculateTileSize(int size) {
            size = Math.max(5, size);
            int min = Math.min(Gate.SCENE_HEIGHT, Gate.SCENE_WIDTH);
            int tileSize = Math.min(MAX_SIZE,Math.max(MIN_SIZE, (min / size)));
            rows = size;
            cols = size;

            return tileSize;
        }

        private void addRows(int size, int row, int col) {
            var children = getChildren();
            for (int i = 0; i < row; i++) {
                children.add(new Row(size, col));
            }
        }

        public Row getRow(int index) {
            return (Row) getChildren().get(index);
        }

        public void reset() {
            for (var c : getChildren()) {
                ((Row) c).reset();
            }
        }

        public void updateTile(Actor actor) {
            var land = actor.getPosition();
            var row = getRow(land.getRow());
            row.updateTile(land.getColumn(), actor);
        }

    }

    private class Row extends HBox {
        public Row(int size, int columns) {
            addCols(size, columns);
        }

        private void addCols(int size, int cols) {
            var children = getChildren();
            try {
                for (int i = 0; i < cols; i++) {
                    var pic = new Tile(size);
                    pic.reset();
                    children.add(pic);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        public void reset() {
            for (var c : getChildren()) {
                ((Tile) c).reset();
            }
        }

        public Tile get(int index) {
            return (Tile) getChildren().get(index);
        }

        public void updateTile(int column, Actor actor) {
            get(column).add(actor);
        }

        public void clear() {
            getChildren().clear();
        }

        public int size() { return getChildren().size(); }
    }

    private class Tile extends StackPane {
        private static final String GROUND="/ground.png";
        private final ImageView background;
        private int size;

        public Tile() {
            this(0);
        }

        public Tile(int size) {
            background = create();
            reset();
            background.setFitHeight(size);
            background.setFitWidth(size);
        }

        public Tile(Node... nodes) {
            super(nodes);
            background = create();
            reset();
        }

        public void setBackground(Image image) {
            background.setImage(image);
        }

        public void clear() {
            var c = getChildren();
            c.clear();
            c.add(0, background);
        }

        public void reset() {
            setBackground(new Image(GROUND));
            clear();
        }

        public void add(Actor actor) {
            // TODO: Should give actor a way to temporarily display string
            getChildren().add(actor.getUnitPane(size));
        }

        public void remove(Actor actor) {
            getChildren().remove(actor.getUnitPane());
        }

        private ImageView create() {
            var iv = new ImageView();
            size = restrictSize(size);
            iv.setFitWidth(size);
            iv.setFitHeight(size);
            return iv;
        }

        private int restrictSize(int size) {
            return Math.min(128, Math.max(size, 32));
        }
    }

    @FXML
    private AnchorPane display;
}