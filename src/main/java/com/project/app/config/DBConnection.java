package main.java.com.project.app.config;

import main.java.com.project.app.session.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/budakmit";
    private static String USER;
    private static String PASS;

    public static Connection getConnection() {
        try {
            USER = "auth";
            PASS = "";
            if(Session.currentUser != null) {
                String role = Session.currentUser.getRoles();

                if(role.equals("staff")) {
                    USER = "staff";
                    PASS = "staff.mit.pekan123";
                } else if (role.equals("owner")){
                    USER = "owner";
                    PASS = "owner.mit.pekan123";
                }
            }
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
