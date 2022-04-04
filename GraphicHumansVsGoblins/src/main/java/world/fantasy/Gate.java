package world.fantasy;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import world.fantasy.world.World;

public final class Gate {
    private World world;

    private static Stage stage;

    private int waveLimit;
    private int currentWave;
    private final int[] defaults;
    private final static Gate INSTANCE = new Gate();
    private Gate() {
        waveLimit = 0;
        currentWave = 0;
        defaults = new int[]{3, 0, 2};
    }

    public static Gate getInstance() { return INSTANCE; }

    public void setupInstance(World world, Stage stage, int waves) {
        this.world = world;
        Gate.stage = stage;
        setWaveLimit(waves);
    }

    public void setWorld(World world) { this.world = world; }

    public World getWorld() { return world; }

    public void setStage(Stage stage) { Gate.stage = stage; }

    public Stage getStage() { return stage; }

    public void setWaveLimit(int count) {
        waveLimit = count;
        currentWave = 0;
    }

    public void startWave() {
        if (currentWave >= waveLimit) return; // Should prompt some error
        currentWave++;

        int enemies = (int) Math.pow(1.2,currentWave) * defaults[0];
        int allies = (int) Math.pow(1.2,currentWave) * defaults[1];
        int items = (int) Math.pow(1.2,currentWave) * defaults[2];
        world.spawnWave(enemies, allies, items);

        // TODO: switch to turn view
        System.out.println("Starting turn view");
        //loadScene("turn-view.fxml");
    }

    public static Scene loadScene(String fxml) {
        if (fxml == null || fxml.isEmpty()) return null;
        fxml = "scenes/" + fxml;
        //Object[] res = new Object[2];
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HVGApplication.class.getResource(fxml));
            Scene s = new Scene(fxmlLoader.load(), 950, 630);
            if (stage != null) {
                stage.close();
                stage.setScene(s);
                stage.show();
            }
            return s;
        } catch (Exception e) {
            System.out.printf("Failed to load xml %s%n", fxml);
            e.printStackTrace();
            return null;
        }
    }

    public static Scene loadScene(String fxml, ActionEvent event) {
        if (fxml == null || event == null)  return loadScene(fxml);
        var stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        Scene scene = loadScene(fxml);
        stage.setScene(scene);
        stage.show();
        return scene;
    }

}
