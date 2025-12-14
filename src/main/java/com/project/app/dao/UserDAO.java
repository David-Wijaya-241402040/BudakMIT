package main.java.com.project.app.dao;
import main.java.com.project.app.model.UserModel;

<<<<<<< HEAD
=======
import java.security.MessageDigest;
import java.security.SecureRandom;
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
<<<<<<< HEAD
=======
import java.util.Base64;
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116


public class UserDAO implements UserDaoInterface {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

<<<<<<< HEAD
    @Override
    public UserModel login(String email, String password) {
        String query = "select * from users where email = ? and password = ?";
        try(PreparedStatement pst = connection.prepareStatement(query)){
            pst.setString(1, email);
            pst.setString(2, password);
=======
    private String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String callSalt() {
        return generateSalt();
    }

    private String checkPassword(String email) {
        String query = "select * from users where email = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getString("salt");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String hashed(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public UserModel login(String email, String password) {
        String salt = checkPassword(email);
        String hashPass = hashed((password + salt));

        String query = "select * from users where email = ? and password = ?";
        try(PreparedStatement pst = connection.prepareStatement(query)){
            pst.setString(1, email);
            pst.setString(2, hashPass);
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116

            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                return new UserModel(
                        rs.getInt("user_id"),
<<<<<<< HEAD
                        rs.getString("email"),
                        rs.getString("password"),
=======
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("salt"),
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
                        rs.getString("roles")
                );
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

<<<<<<< HEAD
    public boolean addUser(String username, String email, String noTelp, String password) {
        String query = "INSERT INTO users (nickname, email, phone_number, password) VALUES (?, ?, ?, ?)";
=======
    public boolean addUser(String username, String email, String noTelp, String password, String salt) {
        String passSalt = password + salt;
        String passHash = hashed(passSalt);
        String query = "INSERT INTO users (nickname, email, phone_number, password, salt) VALUES (?, ?, ?, ?, ?)";
<<<<<<< HEAD
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, noTelp);
<<<<<<< HEAD
            pst.setString(4, password);
=======
            pst.setString(4, passHash);
            pst.setString(5, salt);
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
=======
        boolean success = false;

        try {
            connection.setAutoCommit(false); // mulai transaksi

            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, username);
                pst.setString(2, email);
                pst.setString(3, noTelp);
                pst.setString(4, passHash);
                pst.setString(5, salt);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                } else {
                    connection.rollback(); // rollback kalau insert gagal
                    return false;
                }
            }

            connection.commit(); // commit kalau sukses
>>>>>>> c2c9aeaaabe1c6b3bac92ac3ab866f179bb78efd
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // rollback kalau error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            success = false;
        } finally {
            try {
                connection.setAutoCommit(true); // kembalikan auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

<<<<<<< HEAD
=======
    public boolean updatePassword(int userId, String newHashedPassword, String newSalt) {
        String sql = "UPDATE users SET password = ?, salt = ? WHERE user_id = ?";
        boolean success = false;

        try {
            connection.setAutoCommit(false); // mulai transaksi

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, newHashedPassword);
                stmt.setString(2, newSalt);
                stmt.setInt(3, userId);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    success = true;
                } else {
                    connection.rollback(); // rollback kalau update gagal
                    return false;
                }
            }

            connection.commit(); // commit kalau sukses
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // rollback kalau error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            success = false;
        } finally {
            try {
                connection.setAutoCommit(true); // kembalikan auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
}