package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import world.fantasy.creatures.Creature;
import world.fantasy.items.Item;
import world.fantasy.items.consumable.Consumable;
import world.fantasy.items.equipment.Equipment;
import world.fantasy.world.Direction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class TurnOrderController {

    public void setOrder(ArrayDeque<Creature> queue) {
        if (queue == null || queue.isEmpty()) return;
        ArrayList<Creature> list = new ArrayList<>(queue);
        player1.setImage(list.get(0).loadImage());
        if (list.size() > 1) player2.setImage(list.get(1).loadImage());
        if (list.size() > 2) player3.setImage(list.get(2).loadImage());
    }

    @FXML
    private ImageView player1;

    @FXML
    private ImageView player2;

    @FXML
    private ImageView player3;

}