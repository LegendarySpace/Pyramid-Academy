package world.fantasy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import world.fantasy.creatures.Creature;

public class HelloController {
    Creature player;

    public Creature getPlayer() {
        return player;
    }

    public void setPlayer(Creature player) {
        this.player = player;
    }

    public Creature update() {
        health.setText(String.valueOf(player.getHealth()));
        mana.setText(String.valueOf(player.getMana()));
        intelligence.setText(String.valueOf(player.getIntelligence()));
        strength.setText(String.valueOf(player.getStrength()));
        constitution.setText(String.valueOf(player.getConstitution()));
        dexterity.setText(String.valueOf(player.getDexterity()));
        skillPoint.setText(String.valueOf(0));

        return player;
    }

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
    private Label welcomeText;

    @FXML
    private Button armor;

    @FXML
    protected void onArmorClicked() {
        welcomeText.setText("Updating Text!");
    }

    @FXML
    private Button main;

    @FXML
    protected void onMainhandClicked() { main.setText("Weapon"); }

    @FXML
    private Button off;

    @FXML
    protected void onOffhandClicked() { off.setText("Shield"); }

    @FXML
    private Button north;

    @FXML
    protected void onNorthClicked() { north.setText("Moving"); }

    @FXML
    private Button east;

    @FXML
    protected void onEastClicked() { east.setText("Moving"); }

    @FXML
    private Button south;

    @FXML
    protected void onSouthClicked() { south.setText("Moving"); }

    @FXML
    private Button west;

    @FXML
    protected void onWestClicked() { west.setText("Moving"); }

}