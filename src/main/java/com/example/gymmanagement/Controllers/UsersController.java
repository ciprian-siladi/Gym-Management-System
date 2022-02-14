package com.example.gymmanagement.Controllers;

import com.example.gymmanagement.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.gymmanagement.Models.User.setCurrentUserID;
import static com.example.gymmanagement.Services.UserService.*;
import static com.example.gymmanagement.Utils.ControllerUtils.renderView;

public class UsersController implements Initializable {
    @FXML
    private Button goBackButton;
    @FXML private Label labelWelcome;

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, String> genderColumn;
    @FXML private TableColumn<User, String> dateOfBirthColumn;
    @FXML private TableColumn<User, Integer> weightColumn;
    @FXML private TableColumn<User, Integer> heightColumn;
    @FXML private TableColumn<User, String> phoneColumn;

    private ObservableList<User> users;

    private User currentUser;

        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            try {
                currentUser = getUserById(User.getUserID());
                labelWelcome.setText(currentUser.getFirstName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
            users = FXCollections.observableArrayList(getUsers());
            usersTable.setRowFactory(tv ->{
                TableRow<User> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if(event.getClickCount() == 2 && (!row.isEmpty())) {
                        User rowData = row.getItem();
                        // redirect user to his profile
                        try {
                            setCurrentUserID(getUserIdFromUsername(rowData.getUsername()));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        renderView(currentStage, "profile-view.fxml");
                    }
                });
                return row;
            });
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
            weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
            heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            usersTable.getItems().addAll(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }


            goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                renderView(currentStage, "home-view.fxml");
            }
        });
    }
}
