package main.java.com.project.app.dao;
import main.java.com.project.app.model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO implements UserDaoInterface {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserModel login(String email, String password) {
        String query = "select * from users where email = ? and password = ?";
        try(PreparedStatement pst = connection.prepareStatement(query)){
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                return new UserModel(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("roles")
                );
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addUser(String username, String email, String noTelp, String password) {
        String query = "INSERT INTO users (nickname, email, phone_number, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, noTelp);
            pst.setString(4, password);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}