module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.demo.gameboard to javafx.fxml;
    exports com.example.demo.gameboard;
    exports com.example.demo;
    opens com.example.demo to javafx.fxml;
}