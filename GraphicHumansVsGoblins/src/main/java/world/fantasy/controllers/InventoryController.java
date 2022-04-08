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

public class InventoryController {
    private Creature unit;
    private int selectedIdx = -1;

    protected void fillInventoryDisplay() {
        Creature unit = (Creature) ((Stage) inventoryDisplay.getScene().getWindow()).getUserData();
        if (unit == null) return;
        fillInventoryDisplay(unit.getInventory());
    }

    protected void fillInventoryDisplay(List<Item> list) {
        inventoryDisplay.getChildren().clear();
        Creature unit = (Creature) ((Stage) inventoryDisplay.getScene().getWindow()).getUserData();
        if (list == null || list.isEmpty()) list = unit.getInventory();
        for (var item : list) {
            inventoryDisplay.getChildren().add(new Label(item.toDetailedString()) {
                public void handle(MouseEvent mouseEvent) {
                    selectedIdx = inventoryDisplay.getChildren().indexOf(this);
                    enableValidInventoryInput();

                    getOnMouseClicked().handle(mouseEvent);
                }
            });
        }
    }

    private void enableValidInventoryInput() {
        inventoryInput.setVisible(true);
        inventoryInput.setDisable(false);
        btnDrop.setDisable(selectedIdx < 0);
        if (unit == null) return;
        btnUse.setDisable(!(unit.getInventory().get(selectedIdx) instanceof Consumable));
        btnWear.setDisable(!(unit.getInventory().get(selectedIdx) instanceof Equipment));
    }

    @FXML
    private VBox inventoryDisplay;

    @FXML
    private VBox inventoryInput;

    @FXML
    private Button btnUse;

    @FXML
    private Button btnWear;

    @FXML
    private Button btnDrop;


    // Inventory
    @FXML
    protected void onUseClick() {
        if (selectedIdx == -1) return;
        ((Consumable) unit.getInventory().get(selectedIdx)).use();
    }

    @FXML
    protected void onWearClick() {
        if (selectedIdx == -1) return;
        ((Equipment) unit.getInventory().get(selectedIdx)).equip();
    }

    @FXML
    protected void onDropClick() {
        if (selectedIdx == -1) return;
        unit.getInventory().get(selectedIdx).drop();
    }

}