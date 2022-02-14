package com.example.gymmanagement.Controllers;

import com.example.gymmanagement.DBConnection;
import com.example.gymmanagement.Models.User;
import com.example.gymmanagement.Services.UserService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.gymmanagement.Models.User.setCurrentUserID;
import static com.example.gymmanagement.Services.UserService.*;
import static com.example.gymmanagement.Utils.ControllerUtils.renderView;
import static com.example.gymmanagement.Models.User.getUserID;

public class ProfileController implements Initializable {

    @FXML
    Label labelWelcome;
    @FXML
    TextField firstNameTextField, lastNameTextField, weightTextField, heightTextField, phoneTextField;
    @FXML
    Button saveProfileButton, goBackButton, deleteProfileButton, payButton;
    private User currentUser;


    public void setProfileInformation(User user) {
        firstNameTextField.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        weightTextField.setText(String.valueOf(user.getWeight()));
        heightTextField.setText(String.valueOf(user.getHeight()));
        phoneTextField.setText(user.getPhone());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            currentUser = getUserFromUsername(labelWelcome.getText());
            currentUser = getUserById(User.getUserID());
            setProfileInformation(currentUser);
            setUserInformation1(currentUser.getFirstName());
            System.out.println("USER ID = " + User.getUserID());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        payButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    currentUser = getUserById(User.getUserID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                renderView(currentStage, "payment-view.fxml");
            }
        });

        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    currentUser = getUserById(User.getUserID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(User.wasAdmin) setCurrentUserID(1);
                Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                renderView(currentStage, "home-view.fxml");
            }
        });

        saveProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    UserService.updateUserInformationFromUsername(currentUser.getUsername(), firstNameTextField.getText(), lastNameTextField.getText(),
                            Integer.parseInt(weightTextField.getText()), Integer.parseInt(heightTextField.getText()), phoneTextField.getText());
                    currentUser = getUserFromUsername(currentUser.getUsername());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                setProfileInformation(currentUser);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Profile saved!");
                alert.show();
            }
        });

        deleteProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(deleteUserFromId(getUserID())){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success!");
                        alert.setHeaderText("User successfully deleted!");
                        alert.show();
                        currentUser = getUserFromUsername("admin");
                        setCurrentUserID(getUserIdFromUsername("admin"));
                        setProfileInformation(currentUser);
                        setUserInformation1(currentUser.getFirstName());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setUserInformation1(String username) throws SQLException {
        labelWelcome.setText(username);
        System.out.println("USERNAME IS: " + username);
//        currentUser = getUserFromUsername(username);
    }
}



