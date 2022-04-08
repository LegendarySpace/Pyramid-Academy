package world.fantasy.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import world.fantasy.Gate;
import world.fantasy.world.World;

public class NewGameController {
    private static final int DEFAULT_SIZE = 5;
    private static final int DEFAULT_LIVES = 1;
    private static final int DEFAULT_WAVES = 10;

    public NewGameController() {
        initialize();
        if (sizeInput != null) sizeInput.setText(String.valueOf(DEFAULT_SIZE));
        if (livesInput != null) livesInput.setText(String.valueOf(DEFAULT_LIVES));
        if (wavesInput != null) wavesInput.setText(String.valueOf(DEFAULT_WAVES));
    }

    public void initialize() {
        restrictInput(sizeInput);
        restrictInput(livesInput);
        restrictInput(wavesInput);
    }
    private void restrictInput(TextField field) {
        if (field == null) return;
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private TextField sizeInput;

    @FXML
    private TextField livesInput;

    @FXML
    private TextField wavesInput;

    @FXML
    private void onStartClick(ActionEvent event) {
        if (event == null) return;
        // if not all input valid return
        World world = new World(Integer.parseInt(sizeInput.getText()));
        world.generatePlayers(Integer.parseInt(livesInput.getText()));
        var gate = Gate.getInstance();
        gate.setWorld(world);
        gate.setWaveLimit(Integer.parseInt(wavesInput.getText()));
        var loader = Gate.getLoader("leveling-view.fxml");
        Gate.loadScene(loader);
        var con = (LevelingController) loader.getController();
        con.nextPlayer();
    }

    @FXML
    protected void onCancelClick() {
    }

}