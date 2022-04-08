package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import world.fantasy.creatures.Creature;
import world.fantasy.items.Item;
import world.fantasy.items.consumable.Consumable;
import world.fantasy.items.equipment.Equipment;
import world.fantasy.world.Direction;

import java.util.ArrayDeque;
import java.util.List;

public class TurnOrderController {

    public void setOrder(ArrayDeque<Creature> queue) {
        // TODO: set the character icons to the creature's pics
    }

    @FXML
    private VBox currentPlayer;

    @FXML
    private VBox player2;

}