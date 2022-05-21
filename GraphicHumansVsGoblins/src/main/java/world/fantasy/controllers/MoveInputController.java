package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import world.fantasy.Gate;
import world.fantasy.creatures.Creature;
import world.fantasy.items.Item;
import world.fantasy.items.consumable.Consumable;
import world.fantasy.items.equipment.Equipment;
import world.fantasy.world.Direction;
import world.fantasy.world.World;

import java.util.Arrays;
import java.util.List;

public class MoveInputController {
    private Creature unit;
    private PlayerTurnController turnCon;

    public void setTurnCon(PlayerTurnController turnCon) {
        this.turnCon = turnCon;
    }

    public void update() {
        for (var dir : Direction.values()) enableDirection(dir, false);
        unit = (Creature) ((Stage) btnNorth.getScene().getWindow()).getUserData();
        if (unit == null) return;

        var moves = unit.validMovements();
        for (var dir : Direction.values()) {
            if (moves.contains(dir)) enableDirection(dir, true);
        }
    }

    private void enableDirection(Direction dir, boolean enable) {
        var button = getButton(dir);
        button.setDisable(!enable);
        // TODO: Also need to set the color to clear or #42E6F55A
    }

    private Button getButton(Direction dir) {
        return switch (dir) {
            case NORTHWEST -> btnNorthWest;
            case NORTH -> btnNorth;
            case NORTHEAST -> btnNorthEast;
            case WEST -> btnWest;
            case EAST -> btnEast;
            case SOUTHWEST -> btnSouthWest;
            case SOUTH -> btnSouth;
            case SOUTHEAST -> btnSouthEast;
        };
    }

    public void setTileSize(double size) {
        for (var dir : Direction.values()) {
            getButton(dir).setPrefSize(size, size);
        }
    }

    @FXML
    private Button btnNorth;

    @FXML
    private Button btnNorthWest;

    @FXML
    private Button btnNorthEast;

    @FXML
    private Button btnEast;

    @FXML
    private Button btnSouth;

    @FXML
    private Button btnSouthWest;

    @FXML
    private Button btnSouthEast;

    @FXML
    private Button btnWest;


    // Movement

    @FXML
    protected void onNorthWestClick() {
        unit.move(unit.getPosition().getNorth().getWest());
        turnCon.turnEnd();
    }

    @FXML
    protected void onNorthClick() {
        unit.move(unit.getPosition().getNorth());
        turnCon.turnEnd();
    }

    @FXML
    protected void onNorthEastClick() {
        unit.move(unit.getPosition().getNorth().getEast());
        turnCon.turnEnd();
    }

    @FXML
    protected void onWestClick() {
        unit.move(unit.getPosition().getWest());
        turnCon.turnEnd();
    }

    @FXML
    protected void onEastClick() {
        unit.move(unit.getPosition().getEast());
        turnCon.turnEnd();
    }

    @FXML
    protected void onSouthClick() {
        unit.move(unit.getPosition().getSouth());
        turnCon.turnEnd();
    }

    @FXML
    protected void onSouthWestClick() {
        unit.move(unit.getPosition().getSouth().getWest());
        turnCon.turnEnd();
    }

    @FXML
    protected void onSouthEastClick() {
        unit.move(unit.getPosition().getSouth().getEast());
        turnCon.turnEnd();
    }

}