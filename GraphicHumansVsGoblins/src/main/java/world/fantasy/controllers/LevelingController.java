package world.fantasy.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import world.fantasy.Gate;
import world.fantasy.creatures.Creature;
import world.fantasy.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LevelingController {
    private List<Creature> remainingPlayers;
    private Creature player;

    public LevelingController() {
        remainingPlayers = playerList(Gate.getInstance().getWorld());
    }

    public Creature getPlayer() {
        return player;
    }

    public void setPlayer(Creature player) {
        this.player = player;
    }

    public Creature update() {
        if (player == null) return null;
        final int LIMIT = 999;

        name.setText(player.getName());
        health.setText(String.valueOf(player.getHealth()));
        mana.setText(String.valueOf(player.getMana()));
        intelligence.setText(String.valueOf(player.getIntelligence()));
        strength.setText(String.valueOf(player.getStrength()));
        constitution.setText(String.valueOf(player.getConstitution()));
        dexterity.setText(String.valueOf(player.getDexterity()));
        int sp = player.getRemainingSkillPoints();
        skillPoints.setText(String.valueOf(sp));
        // TODO: Disable increase button for max stats
        hthUp.setDisable(sp < 1 || player.getHealth() > 1000000);
        mnaUp.setDisable(sp < 1 || player.getMana() > 10000);
        intUp.setDisable(sp < 1 || player.getIntelligence() > LIMIT);
        strUp.setDisable(sp < 1 || player.getStrength() > LIMIT);
        conUp.setDisable(sp < 1 || player.getConstitution() > LIMIT);
        dexUp.setDisable(sp < 1 || player.getDexterity() > LIMIT);

        return player;
    }

    private List<Creature> playerList(World world) {
        if (world == null) return null;
        var p = world.getPlayers();
        return Gate.getInstance().getWorld().getPlayers().stream().filter(c -> c.getRemainingSkillPoints() > 0).collect(Collectors.toCollection(ArrayList::new));
    }

    public void nextPlayer() {
        if (remainingPlayers == null || remainingPlayers.isEmpty()) {
            Gate.getInstance().startWave();
            return;
        }
        setPlayer(remainingPlayers.get(0));
        remainingPlayers.remove(0);
        update();
    }

    private void applyChanges() {
        player.setName(name.getText());
    }

    @FXML
    private TextField name;

    @FXML
    private Label skillPoints;

    @FXML
    private Label health;

    @FXML
    private Button hthUp;

    @FXML
    private Label mana;

    @FXML
    private Button mnaUp;

    @FXML
    private Label intelligence;

    @FXML
    private Button intUp;

    @FXML
    private Label strength;

    @FXML
    private Button strUp;

    @FXML
    private Label constitution;

    @FXML
    private Button conUp;

    @FXML
    private Label dexterity;

    @FXML
    private Button dexUp;

    @FXML
    protected void onAcceptClick(ActionEvent event) {
        applyChanges();
        nextPlayer();
    }

    @FXML
    protected void onHthUpClick(ActionEvent event) {
        player.statUp(1);
        applyChanges();
        update();
    }

    @FXML
    protected void onMnaUpClick(ActionEvent event) {
        player.statUp(2);
        applyChanges();
        update();
    }

    @FXML
    protected void onIntUpClick(ActionEvent event) {
        player.statUp(3);
        applyChanges();
        update();
    }

    @FXML
    protected void onStrUpClick(ActionEvent event) {
        player.statUp(4);
        applyChanges();
        update();
    }

    @FXML
    protected void onConUpClick(ActionEvent event) {
        player.statUp(5);
        applyChanges();
        update();
    }

    @FXML
    protected void onDexUpClick(ActionEvent event) {
        player.statUp(6);
        applyChanges();
        update();
    }

}