package com.example.gymmanagement.Controllers;

import com.example.gymmanagement.DBConnection;
import com.example.gymmanagement.Models.User;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.gymmanagement.Models.User.wasAdmin;
import static com.example.gymmanagement.Services.UserService.getUserById;
import static com.example.gymmanagement.Utils.ControllerUtils.renderView;

public class SignUpController implements Initializable {

    @FXML TextField usernameSignUp, emailSignUp, firstNameSignUp, lastNameSignUp, phoneNumberSignUp, weightSignUp, heightSignUp;
    @FXML PasswordField passwordSignUp, rpasswordSignUp;
    @FXML Button signUpButton, backButton;
    @FXML RadioButton maleRadioButton, femaleRadioButton;
    @FXML DatePicker birthDate;
    @FXML Label passwordNegative, completedNegative;

    private String birthDateString;
    private User newUser;

    public void getDate(){
        LocalDate localDate = birthDate.getValue();
        birthDateString = localDate.toString();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        completedNegative.setVisible(false);
        passwordNegative.setVisible(false);

        try {
            newUser = getUserById(User.getUserID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                if(wasAdmin){
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    renderView(currentStage, "home-view.fxml");
                }
                else{
                    User.setCurrentUserID(0);
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    renderView(currentStage, "login-view.fxml");
                }
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!usernameSignUp.getText().trim().isEmpty() && !passwordSignUp.getText().trim().isEmpty() &&
                        !emailSignUp.getText().trim().isEmpty() && passwordSignUp.getText().equals(rpasswordSignUp.getText()) &&
                        !firstNameSignUp.getText().trim().isEmpty() && !lastNameSignUp.getText().trim().isEmpty() &&
                        !phoneNumberSignUp.getText().trim().isEmpty() && !weightSignUp.getText().trim().isEmpty() &&
                !heightSignUp.getText().trim().isEmpty())
                {
                    String gender;
                    if(maleRadioButton.isSelected()) gender = maleRadioButton.getText();
                    else gender = femaleRadioButton.getText();

                    newUser = new User(usernameSignUp.getText(),passwordSignUp.getText(),firstNameSignUp.getText(),lastNameSignUp.getText(),emailSignUp.getText(),gender
                            ,birthDateString,phoneNumberSignUp.getText(), Integer.parseInt(heightSignUp.getText()), Integer.parseInt(weightSignUp.getText()));

                    DBConnection.signUpUser(event, newUser);
                } else if(!passwordSignUp.getText().equals(rpasswordSignUp.getText())) {
                    passwordNegative.setVisible(true);
                    completedNegative.setVisible(false);
                } else {
                    completedNegative.setVisible(true);
                    passwordNegative.setVisible(false);
                }
            }
        });
    }

    // Control Radio Buttons
    @FXML
    public void controlRadioButton(){
        ToggleGroup group = new ToggleGroup();
        maleRadioButton.setToggleGroup(group);
        femaleRadioButton.setToggleGroup(group);
    }
}
