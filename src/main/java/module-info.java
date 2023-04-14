module com.example.simulationview {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.simulationview to javafx.fxml;
    exports com.example.simulationview;
    exports com.example.simulationview.Models;
    opens com.example.simulationview.Models to javafx.fxml;
    exports com.example.simulationview.Util;
    opens com.example.simulationview.Util to javafx.fxml;
}