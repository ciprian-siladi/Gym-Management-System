package com.example.gymmanagement.Services;
import com.example.gymmanagement.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.gymmanagement.Utils.Database.DatabaseQueries.getQuery;
import static com.example.gymmanagement.Utils.Database.DatabaseQueriesNames.*;
import static com.example.gymmanagement.Models.User.getUserID;
public class UserService {
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

    public static List<User> getUsers() throws SQLException {
        ps = conn.prepareStatement(getQuery(GET_ALL_USERS));
        rs = ps.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            String usrname = rs.getString("username");
            String password = rs.getString("password");
            String email = rs.getString("email");
            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String gender = rs.getString("gender");
            String dateOfBirth = rs.getString("dateOfBirth");
            String phone = rs.getString("phone");
            int height = rs.getInt("height");
            int weight = rs.getInt("weight");
            User user = new User(usrname, password, firstName, lastName, email, gender, dateOfBirth, phone, height, weight);
            users.add(user);
        }
        return users;
    }

    public static User getUserById(int id) throws SQLException {
        ps = conn.prepareStatement(String.format(getQuery(GET_USER_BY_ID), id));
        rs = ps.executeQuery();
        User user = null;
        while (rs.next()) {
            String usrname = rs.getString("username");
            String password = rs.getString("password");
            String email = rs.getString("email");
            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String gender = rs.getString("gender");
            String dateOfBirth = rs.getString("dateOfBirth");
            String phone = rs.getString("phone");
            int height = rs.getInt("height");
            int weight = rs.getInt("weight");
            user = new User(usrname, password, firstName, lastName, email, gender, dateOfBirth, phone, height, weight);
        }
        return user;
    }

    public static User getUserFromUsername(String username) throws SQLException {
        ps = conn.prepareStatement(String.format(getQuery(GET_USER_FROM_USERNAME), username));
        rs = ps.executeQuery();
        User user = null;
        while (rs.next()) {
            String usrname = rs.getString("username");
            String password = rs.getString("password");
            String email = rs.getString("email");
            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String gender = rs.getString("gender");
            String dateOfBirth = rs.getString("dateOfBirth");
            String phone = rs.getString("phone");
            int height = rs.getInt("height");
            int weight = rs.getInt("weight");
            user = new User(usrname, password, firstName, lastName, email, gender, dateOfBirth, phone, height, weight);
        }
        return user;
    }

    public static int getUserIdFromUsername(String username) throws SQLException {
        ps = conn.prepareStatement(String.format(getQuery(GET_USER_ID_FROM_USERNAME), username));
        rs = ps.executeQuery();
        int userId = 0;
        while (rs.next()) {
            userId = rs.getInt("idaccount");
        }
        return userId;
    }

    public static List<String> getUserInformationFromUsername(String username) throws SQLException {
        ps = conn.prepareStatement(String.format(getQuery(GET_DETAILS_FROM_USERNAME), username));
        rs = ps.executeQuery();
        List<String> details = new ArrayList<>();
        if (!rs.isBeforeFirst()) {
            System.out.println("No data");
        }
        while (rs.next()) {
            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String weight = rs.getString("weight");
            String height = rs.getString("height");
            String phone = rs.getString("phone");
            details.add(firstName);
            details.add(lastName);
            details.add(weight);
            details.add(height);
            details.add(phone);
        }
        return details;
    }

    public static boolean deleteUserFromId(int id) throws SQLException {
        int currentUserId = getUserID();
        if (currentUserId != 0) {
            ps = conn.prepareStatement(String.format(getQuery(DELETE_USER_BY_ID), getUserID(), getUserID()));
            ps.execute();
            return true;
        }
        return false;
    }

    public static void updateUserInformationFromUsername(String username, String firstName, String lastName, int weight, int height, String phone) throws SQLException {
        ps = conn.prepareStatement(String.format(getQuery(UPDATE_DETAILS_FROM_USERNAME), firstName, lastName, weight, height, phone, username));
        ps.executeUpdate();
    }
}