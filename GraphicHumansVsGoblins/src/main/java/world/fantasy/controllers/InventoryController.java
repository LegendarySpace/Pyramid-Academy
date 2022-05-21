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
import world.fantasy.items.ItemOption;
import world.fantasy.items.consumable.Consumable;
import world.fantasy.items.equipment.Equipment;
import world.fantasy.world.Direction;

import java.util.List;

public class InventoryController {
    private Creature unit;
    private int selectedIdx = -1;

    protected void fillInventoryDisplay() {
        unit = (Creature) ((Stage) inventoryDisplay.getScene().getWindow()).getUserData();
        if (unit == null) return;
        fillInventoryDisplay(unit.getInventory());
    }

    protected void fillInventoryDisplay(List<Item> list) {
        inventoryDisplay.getChildren().clear();
        unit = (Creature) ((Stage) inventoryDisplay.getScene().getWindow()).getUserData();
        if (unit == null) return;
        if (list == null || list.isEmpty()) list = unit.getInventory();
        for (var item : list) {
            inventoryDisplay.getChildren().add(new ItemDisplay(item.toString()));
        }
        enableValidInventoryInput();
    }

    private void enableValidInventoryInput() {
        inventoryInput.setVisible(true);
        inventoryInput.setDisable(false);
        btnUse.setDisable(true);
        btnWear.setDisable(true);
        btnDrop.setDisable(selectedIdx < 0);
        if (unit == null || selectedIdx < 0) return;
        btnUse.setDisable(!(unit.getInventory().get(selectedIdx) instanceof Consumable));
        btnWear.setDisable(!(unit.getInventory().get(selectedIdx) instanceof Equipment));
    }

    private class ItemDisplay extends Button {
        public ItemDisplay() {
            super();
            setOnAction(event -> {
                selectedIdx = inventoryDisplay.getChildren().indexOf(this);
                System.out.printf("clicking on inventory item, setting selectedIdx to %d\n", selectedIdx);

                enableValidInventoryInput();
            });
        }

        public ItemDisplay(String label) {
            this();
            setText(label);
        }

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
        if (selectedIdx < 0) return;
        ((Consumable) unit.getInventory().get(selectedIdx)).activate(ItemOption.USE);
        selectedIdx = -1;
    }

    @FXML
    protected void onWearClick() {
        if (selectedIdx < 0) return;
        System.out.printf("Attempting to wear item at index %s%n", selectedIdx);
        ((Equipment) unit.getInventory().get(selectedIdx)).activate(ItemOption.EQUIP);
        selectedIdx = -1;
    }

    @FXML
    protected void onDropClick() {
        if (selectedIdx < 0) return;
        unit.getInventory().get(selectedIdx).activate(ItemOption.DROP);
        selectedIdx = -1;
    }

}