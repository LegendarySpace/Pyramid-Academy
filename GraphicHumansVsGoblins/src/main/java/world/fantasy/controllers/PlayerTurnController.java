package world.fantasy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import world.fantasy.Gate;
import world.fantasy.creatures.Creature;
import world.fantasy.creatures.UnitOption;
import world.fantasy.items.equipment.Equipment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class PlayerTurnController {
    Creature unit;
    int selectedIdx = -1;
    public Creature update() {
        if (unit == null) return null;
        optionBase();
        gearController.fillGearDisplay();

        return unit;
    }
    protected void optionReset() {
        if (details == null) return;
        details.getChildren().remove(order);
        // TODO: hide gear input
        details.getChildren().remove(inv);
        btnInventory.setDisable(false);
        btnGear.setDisable(false);
        selectedIdx = -1;
    }
    public void optionBase() {
        optionReset();
        details.getChildren().add(0, order);
    }
    public void init() {
        optionBase();
        mapController.createMap(Gate.getInstance().getWorld().boardSize, this);
        turnEnd();
    }
    public ArrayDeque<Creature> getTurnQueue() {
        return Gate.getInstance().getWorld().actors.stream()
                .filter(a -> a instanceof Creature).map(a -> (Creature) a)
                // TODO: Sort by speed so fastest goes first
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    public void turnEnd() {
        optionBase();
        // All enemies are dead return to player leveling
        if (!Gate.getInstance().getWorld().hasEnemies()) {
            var loader = Gate.getLoader("leveling-view.fxml");
            Gate.loadScene(loader);
            var con = (LevelingController) loader.getController();
            con.nextPlayer();
        }
        // All players are dead display game over
        if (Gate.getInstance().getWorld().getPlayers().size() < 1) {
            System.out.println("YOU LOSE");
            System.exit(0);
        }
        var q = getTurnQueue();
        if (q.isEmpty()) {      // All players are dead

        }
        if (unit != null && q.contains(unit)) {
            // cycle through queue until next player selected
            while (q.peek() != unit) q.add(q.pop());
            q.add(q.pop());
        }
        unit = q.peek();
        ((Stage) details.getScene().getWindow()).setUserData(unit);
        // Update controllers
        mapController.updateTileContents();  //
        mapController.updateMove(unit);
        orderController.setOrder(q);
        infoController.update();
        update();
        if (unit.isNPC()) processAIAction(unit);
    }

    private void processAIAction(Creature unit) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ie) {
            System.out.println("Failed to sleep");
            ie.printStackTrace();
        }
        if (unit == null) {
            turnEnd();
            return;
        }
        if (unit.getTarget() == null || !unit.getTarget().doesExist()) {
            var players = new ArrayList<>(Gate.getInstance().getWorld().getPlayers());
            if (players.isEmpty()) {
                turnEnd();
                return;
            }
            Collections.shuffle(players);
            unit.setTarget(players.get(0));
        }
        if (unit.determineAction() == UnitOption.MOVE) {
            unit.move(unit.getPosition().moveTowards(unit.getTarget()));
        }
        turnEnd();
    }



    @FXML
    private ScrollPane map;

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
    public VBox gear;

    @FXML
    public GearController gearController;

    @FXML
    public VBox unitOptions;

    @FXML
    private Button btnInventory;

    @FXML
    private Button btnGear;

    @FXML
    public Button btnPass;

    @FXML
    private VBox inv;

    @FXML
    private InventoryController invController;

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
        gearController.fillEquipmentList();
    }

    @FXML
    protected void onPassClick() {
        optionBase();
        unit.move(unit.getPosition());
        turnEnd();
    }


}