package com.example.gymmanagement.Utils;

import com.example.gymmanagement.LoginApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerUtils {
    private static Stage stage;
    private static Scene scene;

    /**
     * Renders a new view, based on the current stage and a view file name.
     *
     * @param currentStage
     * @param viewName
     */
    public static void renderView(Stage currentStage, String viewName) {
        try {
            Parent root = FXMLLoader.load(LoginApplication.class.getResource(viewName));
            stage = currentStage;
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
