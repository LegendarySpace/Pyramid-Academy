package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import world.fantasy.Actor;
import world.fantasy.Gate;

import java.io.FileInputStream;
import java.io.IOException;

public class FullMapController {
    public Map map;


    public void createMap(int size) {
        display.getChildren().clear();
        map = new Map(size);
        display.getChildren().add(0, map);
    }

    public void updateTileContents() {
        // TODO: This will handle updating the display
        // Reset display, for each actor in the world update the display at their position
        map.reset();
        for (Actor actor :Gate.getInstance().getWorld().actors) map.updateTile(actor);
    }

    private class Map extends VBox {
        public static final int MIN_SIZE = 32;
        public static final int MAX_SIZE = 128;

        private int rows;

        private int cols;

        public Map(int size) {
            addRows(calculateTileSize(size), rows, cols);
        }

        public int calculateTileSize(int size) {
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
            row.updateImageAt(land.getColumn(), actor.loadImage());
        }

        public void updateImageAt(int row, int col, Image image) {
            ((Row) getChildren().get(row)).updateImageAt(col, image);
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
                    var pic = new ImageView();
                    resetView(pic);
                    pic.setFitHeight(size);
                    pic.setFitWidth(size);
                    children.add(pic);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        private void resetView(ImageView iv) {
            iv.setImage(getImage());
        }

        public void reset() {
            for (var c : getChildren()) {
                resetView((ImageView) c);
            }
        }

        private Image getImage() {
            try {
                return new Image(new FileInputStream("D:\\Pyramid-Academy\\GraphicHumansVsGoblins\\src\\main\\resources\\world\\fantasy\\images\\ground.png"));
            } catch (IOException e) {
                System.out.println("Map image could not be loaded!");
                return null;
            }
        }

        public ImageView get(int index) {
            return (ImageView) getChildren().get(index);
        }

        public void updateImageAt(int column, Image image) {
            get(column).setImage(image);
        }

        public void clear() {
            getChildren().clear();
        }

        public int size() { return getChildren().size(); }
    }

    @FXML
    private AnchorPane display;
}