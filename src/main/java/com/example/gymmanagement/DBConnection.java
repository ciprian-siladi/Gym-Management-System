package com.example.gymmanagement;

import com.example.gymmanagement.Controllers.HomeController;
import com.example.gymmanagement.Controllers.ProfileController;
import com.example.gymmanagement.Models.User;
import com.example.gymmanagement.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;

import static com.example.gymmanagement.Utils.ControllerUtils.renderView;

public class DBConnection {



    /** Function to switch to any given page
     * @param fxmlFile a string with the fxml page name (example.fxml)
     */
    public static void changeScene(ActionEvent event, String fxmlFile, User user){
        Parent root = null;

        if(user.getUsername() != null && user.getPassword() != null){
            System.out.println("YES");
            try{
                URL fxmlLocation = DBConnection.class.getResource(fxmlFile);
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                root = loader.load();
                if(fxmlFile.equals("home-view.fxml")){
                    HomeController homeController = loader.getController();
                    homeController.setUserInformation(user.getUsername());
                }
                else if(fxmlFile.equals("profile-view.fxml")) {

                    ProfileController profileController = loader.getController();
                    profileController.setUserInformation1(user.getUsername());
                    profileController.setProfileInformation(user);
                }

            } catch (IOException | SQLException e){
                e.printStackTrace();
            }
        } else {
            System.out.println("NO");
            try{
                root = FXMLLoader.load(DBConnection.class.getResource(fxmlFile));
                if(fxmlFile.equals("home-view.fxml")){
                    HomeController homeController = new HomeController();
                    if(!user.getFirstName().isEmpty())
                        homeController.setUserInformation(user.getFirstName());
                    else homeController.setUserInformation(user.getUsername());
                }
                else if(fxmlFile.equals("profile-view.fxml")) {
                    List<String> loadDetails = UserService.getUserInformationFromUsername(user.getUsername());
                    System.out.println(loadDetails.get(0));

                    ProfileController profileController = new ProfileController();
                    profileController.setProfileInformation(user);
                }
            } catch (IOException | SQLException e){
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, User user){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;
        String url = "jdbc:postgresql://localhost:5432/GymMembers";


        try {
            connection = DriverManager.getConnection(url, "postgres", "zijaji82");
            psCheckUserExists = connection.prepareStatement("Select * from \"Accounts\" WHERE username = ?");
            psCheckUserExists.setString(1, user.getUsername());
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username!");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO \"Accounts\" (username, password, email) VALUES (?, ?, ?)");
                psInsert.setString(1, user.getUsername());
                psInsert.setString(2, user.getPassword());
                psInsert.setString(3, user.getEmail());
                psInsert.executeUpdate();

                psInsert = connection.prepareStatement("insert into \"Members\" (firstname, lastname, gender, dateofbirth, weight, height, phone) VALUES (?,?,?,?,?,?,?)") ;
                psInsert.setString(1, user.getFirstName());
                psInsert.setString(2, user.getLastName());
                psInsert.setString(3, user.getGender());
                psInsert.setString(4, user.getDateOfBirth());
                psInsert.setInt(5,user.getWeight());
                psInsert.setInt(6, user.getHeight());
                psInsert.setString(7, user.getPhone());
                psInsert.executeUpdate();

                User.setCurrentUserID(UserService.getUserIdFromUsername(user.getUsername()));
                System.out.println(User.getUserID());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Successfully signed-up user!");
                alert.show();
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            /*
                We use the finally statement in order to close all the connections made before so that we don't waste memory
             */
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null){
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void loginUser(ActionEvent event, User user){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String url = "jdbc:postgresql://localhost:5432/GymMembers";

        try {
            connection = DriverManager.getConnection(url, "postgres", "zijaji82");
            preparedStatement = connection.prepareStatement("SELECT password FROM \"Accounts\" WHERE username = ? ");
            preparedStatement.setString(1, user.getUsername());
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                System.out.println("User not found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Username does not exist!");
                alert.setContentText("Please try again!");
                alert.show();
            } else {
                while(resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    if(retrievedPassword.equals(user.getPassword())) {
                        User.setCurrentUserID(UserService.getUserIdFromUsername(user.getUsername()));
                        user = UserService.getUserById(User.getUserID());
                        System.out.println("USER ID: " + User.getUserID());
                        changeScene(event, "home-view.fxml", user);
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Incorrect password!");
                        alert.setContentText("Please try again!");
                        alert.show();
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
