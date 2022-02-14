package com.example.gymmanagement.Controllers;

import com.example.gymmanagement.DBConnection;
import com.example.gymmanagement.Models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.example.gymmanagement.Services.UserService.getUserById;
import static com.example.gymmanagement.Services.UserService.getUserFromUsername;
import static com.example.gymmanagement.Utils.ControllerUtils.renderView;


import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML private Button logoutButton, showAllMembersButton, showProfileButton, addMemberButton, paymentButton;
    @FXML private Label labelWelcome;

    private User currentUser;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            currentUser = getUserFromUsername(labelWelcome.getText());
            currentUser = getUserById(User.getUserID());
            setUserInformation(currentUser.getFirstName());

            if(User.getUserID() == 1) User.wasAdmin = true;

            if(!currentUser.getUsername().equals("admin")){
                showAllMembersButton.setVisible(false);
                addMemberButton.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        showProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Going to Profile - " + currentUser.getFirstName());
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                renderView(currentStage, "profile-view.fxml");
//                DBConnection.changeScene(event, "profile-view.fxml", currentUser);
            }
        });

        showAllMembersButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                renderView(currentStage, "users-view.fxml");
            }
        });

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User.wasAdmin = false;
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                renderView(currentStage, "login-view.fxml");
            }
        });

        addMemberButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                renderView(currentStage, "signup-view.fxml");
            }
        });

        paymentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                renderView(currentStage, "payment-view.fxml");
            }
        });
    }

    public void setUserInformation(String username) throws SQLException {
        labelWelcome.setText(username);
//        currentUser = getUserFromUsername(username);
    }


}
