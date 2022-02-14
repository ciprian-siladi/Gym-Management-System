package com.example.gymmanagement.Controllers;

import com.example.gymmanagement.Models.Payment;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.example.gymmanagement.Services.PaymentService.*;
import static com.example.gymmanagement.Services.UserService.getUserById;
import static com.example.gymmanagement.Services.UserService.getUserFromUsername;
import static com.example.gymmanagement.Utils.ControllerUtils.renderView;

public class PaymentController implements Initializable {

    private ObservableList<Payment> payments;

    @FXML private Label chooseMonthLabel;
    @FXML
    private ComboBox<String> chooseSubscriptionBox, chooseMonthBox;
    @FXML
    private TextField amountTextField;
    @FXML private RadioButton monthlyPay, dailyPay;
    @FXML private TableView<Payment> paymentsTable;
    @FXML private TableColumn<Payment, String> timeColumn;
    @FXML private TableColumn<Payment, String> subscriptionColumn;
    @FXML private TableColumn<Payment, Integer> amountColumn;


    private User currentUser;


    @FXML
    Button goBackButton, payButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseMonthBox.setVisible(false);
        chooseMonthLabel.setVisible(false);
        try {
            currentUser = getUserById(User.getUserID());
            System.out.println("USER ID PRODUCTS = " + User.getUserID());
            List<Payment> paymentList = getPaymentsForCurrentUser();

            paymentsTable.setEditable(true);
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            subscriptionColumn.setCellValueFactory(new PropertyValueFactory<>("subscription"));
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
            payments = FXCollections.observableArrayList(paymentList);
            paymentsTable.getItems().addAll(payments);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(User.wasAdmin) User.setCurrentUserID(1);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                renderView(currentStage, "home-view.fxml");
            }
        });
    }


    @FXML
    public void createNewPayment(ActionEvent event){
        int currentUserId = User.getUserID();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(dailyPay.isSelected()){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime now = LocalDateTime.now();
            if(buildPaymentForCurrentUser(currentUserId, chooseSubscriptionBox.getValue(), Integer.parseInt(amountTextField.getText()), dtf.format(now))){
                alert.setHeaderText("Payment was done successfully!");
                alert.show();
            }
            else{
                alert.setHeaderText("User already paid today!");
                alert.show();
            }
        }
        else{
            if(buildPaymentForCurrentUser(currentUserId, chooseSubscriptionBox.getValue(), Integer.parseInt(amountTextField.getText()), chooseMonthBox.getValue())){
                alert.setHeaderText("Payment was done successfully!");
                alert.show();
            }
            else{
                alert.setHeaderText("User already paid for " + chooseMonthBox.getValue() + "!");
                alert.show();
            }
        }
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        renderView(currentStage, "payment-view.fxml");
    }

    @FXML
    public void setChooseSubscriptionBox(){
        if(dailyPay.isSelected()){
            if(chooseSubscriptionBox.getValue().equals("Male - Under 18"))
                amountTextField.setText("15");
            else if(chooseSubscriptionBox.getValue().equals("Female - Under 18"))
                amountTextField.setText("10");
            else if(chooseSubscriptionBox.getValue().equals("Male - Adult"))
                amountTextField.setText("25");
            else
                amountTextField.setText("20");
        }
        else{
            if(chooseSubscriptionBox.getValue().equals("Male - Under 18"))
                amountTextField.setText("80");
            else if(chooseSubscriptionBox.getValue().equals("Female - Under 18"))
                amountTextField.setText("70");
            else if(chooseSubscriptionBox.getValue().equals("Male - Adult"))
                amountTextField.setText("120");
            else
                amountTextField.setText("110");
        }
    }

    @FXML
    public void controlRadioButton(){
        ToggleGroup group = new ToggleGroup();
        dailyPay.setToggleGroup(group);
        monthlyPay.setToggleGroup(group);
        if(monthlyPay.isSelected()){
            chooseMonthBox.setVisible(true);
            chooseMonthLabel.setVisible(true);
        }
        else {
            chooseMonthBox.setVisible(false);
            chooseMonthLabel.setVisible(false);
        }
    }
}
