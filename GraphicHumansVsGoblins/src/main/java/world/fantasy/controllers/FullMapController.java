package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import world.fantasy.Gate;
import world.fantasy.creatures.Creature;

import java.io.FileInputStream;

public class FullMapController {
    // TODO: Make map size of board, cells should range in size from MIN to MAX
    public Map map;


    public void createMap(int size) {
        display.getChildren().clear();
        map = new Map(size);
        display.getChildren().add(0, map);
    }

    public void updateTileContents() {
        // TODO: This will handle updating the display
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
            rows = Gate.SCENE_HEIGHT / tileSize;
            cols = Gate.SCENE_HEIGHT / tileSize;         // TODO: This should take the details panel into consideration

            return tileSize;
        }

        private void addRows(int size, int row, int col) {
            var children = getChildren();
            for (int i = 0; i < row; i++) {
                children.add(new Row(size, col));
            }
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
                // TODO: This needs to be made relative instead of absolute
                Image image = new Image(new FileInputStream("D:\\Pyramid-Academy\\GraphicHumansVsGoblins\\src\\main\\resources\\world\\fantasy\\ground.png"));
                for (int i = 0; i < cols; i++) {
                    var pic = new ImageView(image);
                    pic.setFitHeight(size);
                    pic.setFitWidth(size);
                    children.add(pic);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
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
    private Pane display;
}