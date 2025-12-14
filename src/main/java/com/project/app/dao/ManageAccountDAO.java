package main.java.com.project.app.dao;

import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.model.UserSummaryModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageAccountDAO implements ManageAccountDaoInterface {

    private Connection conn;

    public ManageAccountDAO() {
        conn = DBConnection.getConnection();
    }

    @Override
    public List<UserSummaryModel> getAllUsers() {
        List<UserSummaryModel> list = new ArrayList<>();
        String query = "SELECT * FROM view_user_surat_summary";

        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(new UserSummaryModel(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("status"),
                        rs.getInt("total_surat_dibuat")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<UserSummaryModel> searchUsers(String category, String keyword) {
        List<UserSummaryModel> list = new ArrayList<>();

        String query = "SELECT * FROM view_user_surat_summary WHERE " + category + " LIKE ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, "%" + keyword + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new UserSummaryModel(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("status"),
                        rs.getInt("total_surat_dibuat")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        boolean success = false;

        try {
            conn.setAutoCommit(false); // mulai transaksi

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, userId);
                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    success = true;
                } else {
                    conn.rollback(); // rollback kalau tidak ada baris terhapus
                    return false;
                }
            }

            conn.commit(); // commit kalau sukses
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // rollback kalau ada error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            success = false;
        } finally {
            try {
                conn.setAutoCommit(true); // kembalikan auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public boolean updateUserStatus(int userId, String newStatus) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        boolean success = false;

        try {
            conn.setAutoCommit(false); // mulai transaksi

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, newStatus);
                pst.setInt(2, userId);

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    success = true;
                } else {
                    conn.rollback(); // rollback kalau update gagal
                    return false;
                }
            }

            conn.commit(); // commit kalau sukses
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // rollback kalau error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            success = false;
        } finally {
            try {
                conn.setAutoCommit(true); // kembalikan auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }
}
