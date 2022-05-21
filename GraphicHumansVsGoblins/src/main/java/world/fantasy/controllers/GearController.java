package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import world.fantasy.creatures.Creature;
import world.fantasy.items.equipment.Equipment;

import java.util.List;

public class GearController {
    private Creature unit;
    private int selectedSlot = -1;
    private int selectedEquipment = -1;

    @FXML
    private VBox view;

    @FXML
    private VBox equipmentDisplay;

    @FXML
    private VBox equipmentList;

    @FXML
    private Label detailsLabel;

    @FXML
    private HBox gearInput;

    @FXML
    private Button btnEquip;

    @FXML
    private Button btnUnequip;

    @FXML
    private Button btnAuto;

    // Gear

    @FXML
    protected void onEquipClick() {
        if (unit == null) return;
        fillEquipmentList();
    }
    @FXML
    protected void onUnequipClick() {
        if (selectedEquipment < 0) return;
        unit.removeEquipment(unit.getAllSlots().get(selectedEquipment));
    }

    @FXML
    protected void onAutoClick() {
        unit.autoEquip();
    }



    protected void fillGearDisplay() {
        unit = (Creature) ((Stage) equipmentDisplay.getScene().getWindow()).getUserData();
        equipmentDisplay.getChildren().clear();
        for (var slot : unit.getAllSlots()) {
            var e = unit.getGearFromSlot(slot);
            String str = slot.name() + ": " + ((e == null)?"NONE":e);
            equipmentDisplay.getChildren().add(new GearDisplay(str));
        }
        reset();
        view.getChildren().remove(gearInput);
    }

    protected void fillEquipmentList() { fillEquipmentList(null); }
    protected void fillEquipmentList(List<Equipment> fill) {
        if (fill == null || fill.isEmpty()) fill = unit.getInventory().stream()
                .filter(item -> item instanceof Equipment).map(x->(Equipment)x).toList();

        equipmentList.getChildren().clear();
        for (var e : fill) {
            equipmentList.getChildren().add(new EquipmentDisplay(e.toString()));
        }
    }

    protected void reset() {
        view.getChildren().remove(equipmentList);
        view.getChildren().remove(detailsLabel);
        detailsLabel.setText("");
        gearInput.setVisible(true);
        btnEquip.setDisable(true);
        btnUnequip.setDisable(true);
        btnAuto.setDisable(false);
    }

    private void enableValidGearInput() {
        btnEquip.setDisable(false);
        btnUnequip.setDisable(unit.getGearFromSlot(unit.getAllSlots().get(selectedSlot)) == null);
    }

    private abstract class ItemDisplay extends Button {

        public ItemDisplay() {
            super();
        }
        public ItemDisplay(String label) {
            this();
            setText(label);
        }
        public abstract void setAction();

    }

    private class GearDisplay extends ItemDisplay {
        public GearDisplay(String label) {
            super(label);
        }

        @Override
        public void setAction() {
            setOnAction(event -> {
                selectedSlot = equipmentDisplay.getChildren().indexOf(this);
                enableValidGearInput();
            });
        }

    }

    private class EquipmentDisplay extends ItemDisplay {
        public EquipmentDisplay(String label) {
            super(label);
        }

        @Override
        public void setAction() {
            setOnAction(event -> {
                selectedEquipment = equipmentList.getChildren().indexOf(this);
                enableValidGearInput();
            });
        }

    }

}