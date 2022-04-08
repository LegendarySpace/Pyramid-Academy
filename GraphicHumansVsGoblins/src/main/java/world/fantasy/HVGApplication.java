package world.fantasy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import world.fantasy.controllers.*;
import world.fantasy.world.World;

import java.io.IOException;

public class HVGApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Gate.getInstance().setStage(stage);
        stage.setTitle("Humans Vs Goblins");
        Scene scene = Gate.loadScene("splash-view.fxml");
        if (scene == null) System.exit(-1);
/*
        do {
            world.playTurn();
        } while (world.hasEnemies() && world.hasPlayers());
        if (world.hasEnemies()) System.out.println("You failed to defeat all the goblins");
        else System.out.println("Congratulations on defeating the goblins");
 */
    }

    public static void main(String[] args) {
        launch();
    }
}