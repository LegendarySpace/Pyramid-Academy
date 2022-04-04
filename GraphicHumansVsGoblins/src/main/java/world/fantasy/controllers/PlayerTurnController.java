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

import java.util.List;

public class PlayerTurnController {
    Creature unit;
    int selectedIdx = -1;

    public PlayerTurnController() {
        optionInit();
    }

    public void setUnit(Creature unit) {
        this.unit = unit;
    }

    public Creature update() {
        if (unit == null) return null;
        health.setText(String.valueOf(unit.getHealth()));
        mana.setText(String.valueOf(unit.getMana()));
        intelligence.setText(String.valueOf(unit.getIntelligence()));
        strength.setText(String.valueOf(unit.getStrength()));
        constitution.setText(String.valueOf(unit.getConstitution()));
        dexterity.setText(String.valueOf(unit.getDexterity()));
        skillPoint.setText(String.valueOf(0));
        optionInit();
        fillGearDisplay();

        return unit;
    }

    protected void optionReset() {
        order.setVisible(false);
        gearInput.setVisible(false);
        gearInput.setDisable(true);
        moveInput.setVisible(false);
        moveInput.setDisable(true);
        inv.setVisible(false);
        inv.setDisable(true);
        btnInventory.setDisable(false);
        btnGear.setDisable(false);
        btnMove.setDisable(false);
        selectedIdx = -1;
    }

    protected void optionInit() {
        optionReset();
        order.setVisible(true);
    }

    protected void fillInventoryDisplay() {
        fillInventoryDisplay(unit.getInventory());
    }

    protected void fillInventoryDisplay(List<Item> list) {
        inventoryDisplay.getChildren().clear();
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
        btnUse.setDisable(!(unit.getInventory().get(selectedIdx) instanceof Consumable));
        btnWear.setDisable(!(unit.getInventory().get(selectedIdx) instanceof Equipment));
    }

    protected void fillGearDisplay() {
        equipmentDisplay.getChildren().clear();
        for (var slot : unit.getAllSlots()) {
            String str = slot.name() + ": " + unit.getGearFromSlot(slot).getName();
            equipmentDisplay.getChildren().add(new Label(str) {
                public void handle(MouseEvent mouseEvent) {
                    selectedIdx = equipmentDisplay.getChildren().indexOf(this);
                    enableValidGearInput();

                    getOnMouseClicked().handle(mouseEvent);
                }
            });
        }
    }

    private void enableValidGearInput() {
        gearInput.setVisible(true);
        gearInput.setDisable(false);
        btnEquip.setDisable(false);
        btnAuto.setDisable(false);
        btnUnequip.setDisable(unit.getGearFromSlot(unit.getAllSlots().get(selectedIdx)) == null);
    }

    private void enableValidMoveInput() {
        var valid = unit.validMovements();
        btnNorth.setDisable(!valid.contains(Direction.NORTH));
        btnEast.setDisable(!valid.contains(Direction.EAST));
        btnSouth.setDisable(!valid.contains(Direction.SOUTH));
        btnWest.setDisable(!valid.contains(Direction.WEST));
    }

    public void turnEnd() {
        // Don't know how this will work yet, but it will handle telling world to progress to the next player's turn
        optionInit();
        // TODO: Pass turn control
    }

    @FXML
    private HBox order;

    @FXML
    private VBox currentPlayer;

    @FXML
    private VBox player2;

    @FXML
    private Label name;

    @FXML
    private Label health;

    @FXML
    private Label mana;

    @FXML
    private Label intelligence;

    @FXML
    private Label strength;

    @FXML
    private Label constitution;

    @FXML
    private Label dexterity;

    @FXML
    private Label skillPoint;

    @FXML
    private VBox equipmentDisplay;

    @FXML
    private HBox gearInput;

    @FXML
    private Button btnEquip;

    @FXML
    private Button btnUnequip;

    @FXML
    private Button btnAuto;

    @FXML
    private Button btnInventory;

    @FXML
    private Button btnGear;

    @FXML
    private Button btnMove;     // Pass just calls move with the current location

    @FXML
    private VBox inv;

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

    @FXML
    private VBox moveInput;

    @FXML
    private Button btnNorth;

    @FXML
    private Button btnEast;

    @FXML
    private Button btnSouth;

    @FXML
    private Button btnWest;


    // Unit Options
    @FXML
    protected void onInventoryClick() {
        optionReset();
        btnInventory.setDisable(true);
        inv.setVisible(true);
        inv.setDisable(false);
        fillInventoryDisplay();
        inventoryInput.setDisable(true);
        inventoryInput.setVisible(false);
        btnUse.setDisable(true);
        btnEquip.setDisable(true);
        btnDrop.setDisable(true);
    }

    @FXML
    protected void onGearClick() {
        optionReset();
        btnGear.setDisable(true);
        gearInput.setVisible(true);
        gearInput.setDisable(false);
        fillGearDisplay();
    }

    @FXML
    protected void onMoveClick() {
        optionReset();
        btnMove.setDisable(true);
        moveInput.setVisible(true);
        moveInput.setDisable(false);
        enableValidMoveInput();
    }

    @FXML
    protected void onPassClick() {
        optionInit();
        unit.move(unit.getPosition());
        turnEnd();
    }


    // Inventory
    @FXML
    protected void onUseClick() {
        if (selectedIdx == -1) return;
        ((Consumable) unit.getInventory().get(selectedIdx)).use();
        optionInit();
    }

    @FXML
    protected void onWearClick() {
        if (selectedIdx == -1) return;
        ((Equipment) unit.getInventory().get(selectedIdx)).equip();
        optionInit();
    }

    @FXML
    protected void onDropClick() {
        if (selectedIdx == -1) return;
        unit.getInventory().get(selectedIdx).drop();
        optionInit();
    }



    // Gear
    @FXML
    protected void onEquipClick() {
        onInventoryClick();
        fillInventoryDisplay(unit.getInventory().stream().filter(item -> item instanceof Equipment).toList());
    }

    @FXML
    protected void onUnequipClick() {
        if (selectedIdx < 0) return;
        unit.removeEquipment(unit.getAllSlots().get(selectedIdx));
        optionInit();
    }

    @FXML
    protected void onAutoClick() {
        unit.autoEquip();
    }



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