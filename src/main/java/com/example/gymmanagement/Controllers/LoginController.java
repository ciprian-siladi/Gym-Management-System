package com.example.gymmanagement.Controllers;

import com.example.gymmanagement.DBConnection;
import com.example.gymmanagement.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.gymmanagement.Utils.ControllerUtils.renderView;

public class LoginController {

    @FXML private TextField usernameLogin, maskField;
    @FXML private PasswordField passwordLogin;
    @FXML private CheckBox showPasswordCheckBox;

    private User user = new User();

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        if(!usernameLogin.getText().trim().isEmpty() && !passwordLogin.getText().trim().isEmpty()) {
            user.setUsername(usernameLogin.getText());
            user.setPassword(passwordLogin.getText());
            DBConnection.loginUser(event, user);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please fill in all information!");
            alert.show();
        }

    }

    @FXML
    protected void onGoToSignUpButtonClick(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        renderView(currentStage, "signup-view.fxml");
    }


    @FXML
    protected void onShowPasswordCheckBox(){
        if(showPasswordCheckBox.isSelected()){
            maskField.setText(passwordLogin.getText());
            maskField.setVisible(true);
            passwordLogin.setVisible(false);
        }
        else{
            passwordLogin.setText(maskField.getText());
            passwordLogin.setVisible(true);
            maskField.setVisible(false);
        }
    }
}