package main.java.com.project.app.config;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.DriverManager;
=======
import main.java.com.project.app.session.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/budakmit";
<<<<<<< HEAD
    private static final String USER = "root";
    private static final String PASS = ""; // password kalau ada

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
=======
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
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
