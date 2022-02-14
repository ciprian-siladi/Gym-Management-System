module com.example.gymmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.gymmanagement to javafx.fxml;
    exports com.example.gymmanagement;
    exports com.example.gymmanagement.Controllers;
    opens com.example.gymmanagement.Controllers to javafx.fxml;
    exports com.example.gymmanagement.Utils;
    opens com.example.gymmanagement.Utils to javafx.fxml;
    opens com.example.gymmanagement.Services to javafx.fxml;
    exports com.example.gymmanagement.Services;
    exports com.example.gymmanagement.Models;
    opens com.example.gymmanagement.Models to javafx.fxml;
    exports com.example.gymmanagement.Utils.Database;
    opens com.example.gymmanagement.Utils.Database to javafx.fxml;
}