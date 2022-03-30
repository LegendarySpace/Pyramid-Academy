module world.fantasy.graphichumansvsgoblins {
    requires javafx.controls;
    requires javafx.fxml;


    opens world.fantasy to javafx.fxml;
    exports world.fantasy;
}