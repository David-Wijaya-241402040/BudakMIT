package main.java.com.project.app.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.model.TagihanModel;

import java.sql.*;

public class TagihanDAO {

    public ObservableList<TagihanModel> listTagihan() {

        String query = """
        SELECT t.no_tag AS no_tagihan,
               t.sp_id,
               t.status_pembayaran AS status,
               t.tenggat_pembayaran AS tanggal,
               (
                   SELECT SUM(dp.qty * h.harga_acuan)
                   FROM jobs j
                   JOIN detail_pekerjaan dp ON dp.job_id = j.job_id
                   JOIN history_harga_komponen h ON h.component_id = dp.component_id
                   WHERE j.sp_id = t.sp_id
                   AND h.tanggal_berlaku = (
                        SELECT MAX(h2.tanggal_berlaku)
                        FROM history_harga_komponen h2
                        WHERE h2.component_id = dp.component_id
                   )
               ) AS total_harga
        FROM tagihan t
        ORDER BY t.created_at DESC;
        """;

        ObservableList<TagihanModel> list = FXCollections.observableArrayList();

        try (Connection conn = DBConnection.getConnection();
             Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {


            while (rs.next()) {
                list.add(new TagihanModel(
                        rs.getString("no_tagihan"),
                        rs.getInt("sp_id"),
                        rs.getDate("tanggal"),
                        rs.getDouble("total_harga"), // computed total
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public double hitungGrandTotal() {
        String query = """
        SELECT SUM(dp.qty * h.harga_acuan) AS grand_total
        FROM detail_pekerjaan dp
        JOIN history_harga_komponen h ON h.component_id = dp.component_id
        WHERE h.tanggal_berlaku = (
            SELECT MAX(h2.tanggal_berlaku)
            FROM history_harga_komponen h2
            WHERE h2.component_id = dp.component_id
        );
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {


            if (rs.next()) {
                return rs.getDouble("grand_total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

