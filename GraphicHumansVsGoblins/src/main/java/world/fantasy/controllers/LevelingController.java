package world.fantasy.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import world.fantasy.Gate;
import world.fantasy.creatures.Creature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LevelingController {
    private List<Creature> remainingPlayers;
    private Creature player;

    public LevelingController() {
        remainingPlayers = playerList();
        nextPlayer();
    }

    public Creature getPlayer() {
        return player;
    }

    public void setPlayer(Creature player) {
        this.player = player;
    }

    public Creature update() {
        if (player == null) return null;
        // TODO: Somehow name is null
        name.setText(player.getName());
        health.setText(String.valueOf(player.getHealth()));
        mana.setText(String.valueOf(player.getMana()));
        intelligence.setText(String.valueOf(player.getIntelligence()));
        strength.setText(String.valueOf(player.getStrength()));
        constitution.setText(String.valueOf(player.getConstitution()));
        dexterity.setText(String.valueOf(player.getDexterity()));
        skillPoints.setText(String.valueOf(player.getRemainingSkillPoints()));
        // TODO: Disable increase button for max stats

        return player;
    }

    private List<Creature> playerList() {
        // This is null when it shouldn't be
        var w = Gate.getInstance().getWorld();
        var p = w.getPlayers();
        return Gate.getInstance().getWorld().getPlayers().stream().filter(c -> c.getRemainingSkillPoints() > 0).collect(Collectors.toCollection(ArrayList::new));
    }

    public void nextPlayer() {
        if (remainingPlayers.isEmpty()) {
            Gate.getInstance().startWave();
            return;
        }
        setPlayer(remainingPlayers.get(0));
        remainingPlayers.remove(0);
        update();
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
        nextPlayer();
    }

}