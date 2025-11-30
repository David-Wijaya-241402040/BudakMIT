package main.java.com.project.app.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.model.TagihanModel;

import java.sql.*;

public class TagihanDAO {

    public ObservableList<TagihanModel> getAllTagihan() {
        ObservableList<TagihanModel> list = FXCollections.observableArrayList();

        String query = "SELECT * FROM view_tagihan_penawaran";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Date sqlDate = rs.getDate("tanggal");

                list.add(new TagihanModel(
                        rs.getString("no_tag"),
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

        String col;

        switch (category) {
            case "No Tagihan": col = "no_tag"; break;
            case "No Surat": col = "no_sp"; break;
            case "Perusahaan": col = "nama_perusahaan"; break;
            default: col = "no_tag";
        }

        String query = "SELECT * FROM view_tagihan_penawaran WHERE " + col + " LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + value + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date sqlDate = rs.getDate("tanggal");

                    list.add(new TagihanModel(
                            rs.getString("no_tag"),
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
}
