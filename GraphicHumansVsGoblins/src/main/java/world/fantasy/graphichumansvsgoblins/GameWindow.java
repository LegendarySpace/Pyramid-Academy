package world.fantasy.graphichumansvsgoblins;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameWindow {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}