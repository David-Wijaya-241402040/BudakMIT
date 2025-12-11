package main.java.com.project.app.dao;
import main.java.com.project.app.model.UserModel;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;


public class UserDAO implements UserDaoInterface {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

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

            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                return new UserModel(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("salt"),
                        rs.getString("roles")
                );
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addUser(String username, String email, String noTelp, String password, String salt) {
        String passSalt = password + salt;
        String passHash = hashed(passSalt);
        String query = "INSERT INTO users (nickname, email, phone_number, password, salt) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, noTelp);
            pst.setString(4, passHash);
            pst.setString(5, salt);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}