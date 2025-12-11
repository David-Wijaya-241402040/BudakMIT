package main.java.com.project.app.config;

import main.java.com.project.app.session.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            if (Session.currentUser != null) {
                try (PreparedStatement st = connection.prepareStatement("SET @current_user_id = ?")) {
                    st.setInt(1, Session.currentUser.getId());
                    st.execute();
                }
            }

            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
