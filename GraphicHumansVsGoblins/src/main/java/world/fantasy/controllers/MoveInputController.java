package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import world.fantasy.creatures.Creature;
import world.fantasy.items.Item;
import world.fantasy.items.consumable.Consumable;
import world.fantasy.items.equipment.Equipment;
import world.fantasy.world.Direction;

import java.util.List;

public class MoveInputController {
    private Creature unit;

    public void update() {
        unit = (Creature) ((Stage) btnNorth.getScene().getWindow()).getUserData();
        if (unit == null) {
            btnNorth.setDisable(true);
            btnEast.setDisable(true);
            btnSouth.setDisable(true);
            btnWest.setDisable(true);
        } else {
            btnNorth.setDisable(false);
            btnEast.setDisable(false);
            btnSouth.setDisable(false);
            btnWest.setDisable(false);
        }
    }

    public void turnEnd() {
        // Don't know how this will work yet, but it will handle telling world to progress to the next player's turn
        // TODO: Should call turnEnd on parent      // May move back into turn controller
    }

    @FXML
    private Button btnNorth;

    @FXML
    private Button btnEast;

    @FXML
    private Button btnSouth;

    @FXML
    private Button btnWest;


    // Movement
    @FXML
    protected void onNorthClick() {
        unit.move(unit.getPosition().getNorth());
        turnEnd();
    }

    @FXML
    protected void onEastClick() {
        unit.move(unit.getPosition().getEast());
        turnEnd();
    }

    @FXML
    protected void onSouthClick() {
        unit.move(unit.getPosition().getSouth());
        turnEnd();
    }

    @FXML
    protected void onWestClick() {
        unit.move(unit.getPosition().getWest());
        turnEnd();
    }

}