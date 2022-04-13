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

import java.util.List;

public class MoveInputController {
    private Creature unit;
    private PlayerTurnController turnCon;

    public void setTurnCon(PlayerTurnController turnCon) {
        this.turnCon = turnCon;
    }

    public void update() {
        unit = (Creature) ((Stage) btnNorth.getScene().getWindow()).getUserData();
        if (unit == null || turnCon == null) {
            btnNorth.setDisable(true);
            btnEast.setDisable(true);
            btnSouth.setDisable(true);
            btnWest.setDisable(true);
        } else {
            World w = Gate.getInstance().getWorld();
            btnNorth.setDisable(!w.landExists(unit.getPosition().getNorth()));
            btnEast.setDisable(!w.landExists(unit.getPosition().getEast()));
            btnSouth.setDisable(!w.landExists(unit.getPosition().getSouth()));
            btnWest.setDisable(!w.landExists(unit.getPosition().getWest()));
        }
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
    protected void onWestClick() {
        unit.move(unit.getPosition().getWest());
        turnCon.turnEnd();
    }

}