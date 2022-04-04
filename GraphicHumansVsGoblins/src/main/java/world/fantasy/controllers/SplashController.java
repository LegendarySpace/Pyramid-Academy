package world.fantasy.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import world.fantasy.Gate;

public class SplashController {
    @FXML
    protected void changeScene() {
        Gate.loadScene("new-game-view.fxml");
    }
}
