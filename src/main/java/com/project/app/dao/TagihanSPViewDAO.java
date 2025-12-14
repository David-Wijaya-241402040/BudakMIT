package main.java.com.project.app.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.model.TagihanSPViewModel;
<<<<<<< HEAD
=======
import main.java.com.project.app.session.Session;
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TagihanSPViewDAO {

    public ObservableList<TagihanSPViewModel> getAllSPView() {
        ObservableList<TagihanSPViewModel> list = FXCollections.observableArrayList();
<<<<<<< HEAD
        String sql = "SELECT sp_id, no_sp, nama_perusahaan, tanggal FROM view_list_sp";
=======
        String userRole = Session.currentUser.getRoles();
        int userId = Session.currentUser.getId();
        String sql = "";

        if(userRole.equals("owner")) {
            sql = "SELECT sp_id, no_sp, nama_perusahaan, tanggal FROM view_list_sp";
        } else if (userRole.equals("staff")) {
            sql = "SELECT sp_id, no_sp, nama_perusahaan, tanggal FROM view_list_sp WHERE user_id = " + userId;
        }
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int spId = rs.getInt("sp_id");
                String noSp = rs.getString("no_sp");
                String nama = rs.getString("nama_perusahaan");
                Date d = rs.getDate("tanggal");
                String tanggal = d != null ? d.toString() : "";

                list.add(new TagihanSPViewModel(spId, noSp, nama, tanggal));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
