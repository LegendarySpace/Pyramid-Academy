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

public class UnitInfoController {
    private Creature unit;

    public void update() {
        unit = (Creature) ((Stage) name.getScene().getWindow()).getUserData();
        if (unit == null) return;
        name.setText(unit.getName());
        health.setText(String.valueOf(unit.getHealth()));
        mana.setText(String.valueOf(unit.getMana()));
        intelligence.setText(String.valueOf(unit.getIntelligence()));
        strength.setText(String.valueOf(unit.getStrength()));
        constitution.setText(String.valueOf(unit.getConstitution()));
        dexterity.setText(String.valueOf(unit.getDexterity()));
        skillPoint.setText(String.valueOf(unit.getRemainingSkillPoints()));
    }

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

}