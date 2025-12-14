package main.java.com.project.app.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.model.TagihanModel;
import main.java.com.project.app.session.Session;

import java.sql.*;

public class TagihanDAO {

    public ObservableList<TagihanModel> getAllTagihan() {
        ObservableList<TagihanModel> list = FXCollections.observableArrayList();

        int userId = Session.currentUser.getId();
        String userRole = Session.currentUser.getRoles();
        String query = "";

        if(userRole.equals("owner")) {
            query = "SELECT * FROM view_tagihan_penawaran";
        } else if (userRole.equals("staff")) {
            query = "SELECT * FROM view_tagihan_penawaran WHERE user_id = " + userId;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Date sqlDate = rs.getDate("tanggal");

                list.add(new TagihanModel(
                        rs.getString("no_tag"),
                        rs.getInt("sp_id"),
                        rs.getString("no_sp"),
                        rs.getString("nama_perusahaan"),
                        sqlDate != null ? sqlDate.toLocalDate() : null,
                        rs.getDouble("total"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public ObservableList<TagihanModel> searchTagihan(String value, String category) {
        ObservableList<TagihanModel> list = FXCollections.observableArrayList();
        String role = Session.currentUser.getRoles();
        int userID = Session.currentUser.getId();

        String col;

        switch (category) {
            case "No Tagihan": col = "no_tag"; break;
            case "No Surat": col = "no_sp"; break;
            case "Perusahaan": col = "nama_perusahaan"; break;
            default: col = "no_tag";
        }

        String query = "";

        if(role.equals("owner")) {
            query = "SELECT * FROM view_tagihan_penawaran WHERE " + col + " LIKE ?";
        } else if (role.equals("staff")) {
            query = "SELECT * FROM view_tagihan_penawaran WHERE " + col + " LIKE ?" + "AND user_id = " + userID;
        }


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + value + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date sqlDate = rs.getDate("tanggal");

                    list.add(new TagihanModel(
                            rs.getString("no_tag"),
                            rs.getInt("sp_id"),
                            rs.getString("no_sp"),
                            rs.getString("nama_perusahaan"),
                            sqlDate != null ? sqlDate.toLocalDate() : null,
                            rs.getDouble("total"),
                            rs.getString("status")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertTagihan(String noTag, int spId, String status, java.time.LocalDate tenggat) {
        String sqlCheck = "SELECT user_id FROM surat_penawaran WHERE sp_id = ?";
        String sqlInsert = "INSERT INTO tagihan (no_tag, sp_id, status_pembayaran, tenggat_pembayaran) VALUES (?, ?, ?, ?)";
        boolean success = false;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // mulai transaksi

            // ===== CEK USER =====
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, spId);
                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {
                    int spUserId = rs.getInt("user_id");
                    int currentUserId = Session.currentUser.getId();

                    if (currentUserId != spUserId) {
                        System.out.println("❌ Gagal: SP ini bukan milik user saat ini");
                        conn.rollback();
                        return false;
                    }
                } else {
                    System.out.println("❌ SP ID tidak ditemukan");
                    conn.rollback();
                    return false;
                }
            }

            // ===== INSERT TAGIHAN =====
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setString(1, noTag);
                psInsert.setInt(2, spId);
                psInsert.setString(3, status);
                if (tenggat != null) {
                    psInsert.setDate(4, Date.valueOf(tenggat));
                } else {
                    psInsert.setDate(4, null);
                }

                if (psInsert.executeUpdate() > 0) {
                    success = true;
                } else {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit(); // commit kalau semua sukses
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // rollback kalau ada error
                Connection conn = DBConnection.getConnection();
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            success = false;
        } finally {
            try {
                Connection conn = DBConnection.getConnection();
                conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public boolean updateTagihan(String noTag, int spId, String status, java.time.LocalDate tenggat) {
        String sql = "UPDATE tagihan SET sp_id = ?, status_pembayaran = ?, tenggat_pembayaran = ? WHERE no_tag = ?";
        boolean success = false;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // mulai transaksi

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, spId);
                ps.setString(2, status);
                if (tenggat != null) {
                    ps.setDate(3, Date.valueOf(tenggat));
                } else {
                    ps.setDate(3, null);
                }
                ps.setString(4, noTag);

                if (ps.executeUpdate() > 0) {
                    success = true;
                } else {
                    conn.rollback(); // rollback kalau update gagal
                    return false;
                }
            }

            conn.commit(); // commit kalau sukses
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Connection conn = DBConnection.getConnection();
                conn.rollback(); // rollback kalau ada error
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            success = false;
        } finally {
            try {
                Connection conn = DBConnection.getConnection();
                conn.setAutoCommit(true); // kembalikan auto-commit
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return success;
    }
}
