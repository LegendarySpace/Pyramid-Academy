package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import world.fantasy.Gate;
import world.fantasy.creatures.Creature;
import world.fantasy.items.equipment.Equipment;

import java.util.ArrayDeque;
import java.util.stream.Collectors;

public class PlayerTurnController {
    Creature unit;
    int selectedIdx = -1;
    public Creature update() {
        if (unit == null) return null;
        optionBase();
        fillGearDisplay();

        return unit;
    }
    protected void optionReset() {
        if (details == null) return;
        details.getChildren().remove(order);
        gearInput.setVisible(false);
        gearInput.setDisable(true);
        details.getChildren().remove(inv);
        details.getChildren().remove(move);
        btnInventory.setDisable(false);
        btnGear.setDisable(false);
        btnMove.setDisable(false);
        selectedIdx = -1;
    }
    public void optionBase() {
        optionReset();
        details.getChildren().add(0, order);
    }
    public void init() {
        optionBase();
        mapController.createMap(Gate.getInstance().getWorld().boardSize);
        turnEnd();
    }
    public ArrayDeque<Creature> getTurnQueue() {
        return Gate.getInstance().getWorld().actors.stream()
                .filter(a -> a instanceof Creature).map(a -> (Creature) a)
                // TODO: Sort by speed so fastest goes first
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    protected void fillGearDisplay() {
        equipmentDisplay.getChildren().clear();
        for (var slot : unit.getAllSlots()) {
            var e = unit.getGearFromSlot(slot);
            String str = slot.name() + ": " + ((e == null)?"NONE":e.getName());
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

    public void turnEnd() {
        // Don't know how this will work yet, but it will handle telling world to progress to the next player's turn
        optionBase();
        // TODO: If all enemies are dead return to player leveling
        // TODO: Else if all players are dead display game over
        var q = getTurnQueue();
        if (unit != null && q.contains(unit)) {
            while (q.peek() != unit) q.add(q.pop());
            q.add(q.pop());
        }
        unit = q.peek();
        ((Stage) details.getScene().getWindow()).setUserData(unit);
        // Update controllers
        mapController.updateTileContents();  //
        orderController.setOrder(q);
        infoController.update();
        update();
        // TODO: Pass turn control to next creature
    }



    @FXML
    private Pane mapS;

    @FXML
    private FullMapController mapController;

    @FXML
    private VBox details;

    @FXML
    private HBox order;

    @FXML
    private TurnOrderController orderController;

    @FXML
    private VBox info;

    @FXML
    private UnitInfoController infoController;

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
    public VBox unitOptions;

    @FXML
    private Button btnInventory;

    @FXML
    private Button btnGear;

    @FXML
    private Button btnMove;     // Pass just calls move with the current location

    @FXML
    public Button btnPass;

    @FXML
    private VBox inv;

    @FXML
    private InventoryController invController;

    @FXML
    private VBox move;

    @FXML
    private MoveInputController moveController;

    // Unit Options

    @FXML
    protected void onInventoryClick() {
        optionReset();
        btnInventory.setDisable(true);
        details.getChildren().add(6, inv);
        invController.fillInventoryDisplay();
    }

    @FXML
    protected void onGearClick() {
        optionReset();
        btnGear.setDisable(true);
        fillGearDisplay();
    }

    @FXML
    protected void onMoveClick() {
        optionReset();
        btnMove.setDisable(true);
        details.getChildren().add(6, move);
        moveController.update();
    }

    @FXML
    protected void onPassClick() {
        optionBase();
        unit.move(unit.getPosition());
        turnEnd();
    }


    // Gear
    @FXML
    protected void onEquipClick() {
        onInventoryClick();
        if (unit == null) return;
        invController.fillInventoryDisplay(unit.getInventory().stream()
                .filter(item -> item instanceof Equipment).toList());
    }

    @FXML
    protected void onUnequipClick() {
        if (selectedIdx < 0) return;
        unit.removeEquipment(unit.getAllSlots().get(selectedIdx));
        optionBase();
    }

    @FXML
    protected void onAutoClick() {
        unit.autoEquip();
    }

}