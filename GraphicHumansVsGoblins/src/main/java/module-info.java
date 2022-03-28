module world.fantasy.graphichumansvsgoblins {
    requires javafx.controls;
    requires javafx.fxml;


    opens world.fantasy.graphichumansvsgoblins to javafx.fxml;
    exports world.fantasy.graphichumansvsgoblins;
}