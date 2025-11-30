package main.java.com.project.app.dao;

import main.java.com.project.app.model.PenawaranModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SPDetailDAO {
    private Connection conn;

    public SPDetailDAO(Connection conn) {
        this.conn = conn;
    }

    public List<PenawaranModel.SPJobComponent> getDetailBySP(int spId) {
        List<PenawaranModel.SPJobComponent> list = new ArrayList<>();
        String sql = "SELECT * FROM view_surat_penawaran_jobs_komponen WHERE sp_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, spId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PenawaranModel.SPJobComponent(
                        rs.getInt("sp_id"),
                        rs.getString("perihal"),
                        rs.getString("tanggal_surat_penawaran"),
                        rs.getString("nama_perusahaan"),
                        rs.getInt("job_id"),
                        rs.getString("nama_pekerjaan"),
                        rs.getString("nama_mesin"),
                        rs.getString("spesifikasi_mesin"),
                        rs.getInt("component_id"),
                        rs.getString("nama_component"),
                        rs.getDouble("harga_acuan")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}


