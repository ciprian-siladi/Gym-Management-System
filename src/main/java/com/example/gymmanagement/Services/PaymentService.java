package com.example.gymmanagement.Services;

import com.example.gymmanagement.Models.Payment;
import com.example.gymmanagement.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


import static com.example.gymmanagement.Utils.Database.DatabaseQueries.getQuery;
import static com.example.gymmanagement.Utils.Database.DatabaseQueriesNames.*;
import static com.example.gymmanagement.Models.User.getUserID;

public class PaymentService {
    private static final String connectionUrl = "jdbc:postgresql://localhost:5432/GymMembers";
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    static {
        try {
            conn = DriverManager.getConnection(connectionUrl, "postgres", "zijaji82");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Payment> getPaymentsForCurrentUser() throws SQLException {
        int currentUserId = User.getUserID();
        if (currentUserId != 0) {
            ps = conn.prepareStatement(String.format(getQuery(GET_PAYMENTS_FOR_CURRENT_USER), currentUserId));
            rs = ps.executeQuery();
            List<Payment> payments = new ArrayList<>();
            while (rs.next()) {
                String time = rs.getString("time");
                String subscription = rs.getString("subscription");
                int amount = rs.getInt("amount");
                Payment payment = new Payment(time, subscription, amount);
                payments.add(payment);
            }
            return payments;
        }
        return Collections.emptyList();
    }


    public static boolean checkIfUserAlreadyPaid(int userId, String time) {
        try {
            ps = conn.prepareStatement("select time from \"Payments\" where idaccount=? and time=?");
            ps.setInt(1, userId);
            ps.setString(2, time);
            rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean buildPaymentForCurrentUser(int userId, String subscription, int amount, String time) {
        if (!checkIfUserAlreadyPaid(userId, time)) {
            try {
                ps = conn.prepareStatement(String.format(getQuery(ADD_PAYMENT), userId, subscription, amount, time));
                ps.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

