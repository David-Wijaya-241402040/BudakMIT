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

    public List<PenawaranModel.SPJobComponent> getDetailBySP(String noSP) {
        List<PenawaranModel.SPJobComponent> list = new ArrayList<>();
        String sql = "SELECT * FROM view_surat_penawaran_jobs_komponen WHERE no_sp = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noSP);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PenawaranModel.SPJobComponent(
                        rs.getInt("sp_id"),
                        rs.getString("no_sp"),
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
    public void deleteComponent(int componentId) {
        String sql = "DELETE FROM komponen WHERE component_id = ?";
        // eksekusi delete ke :contentReference[oaicite:4]{index=4}
    }

    public void updateComponent(int componentId, String namaBaru) {
        String sql = "UPDATE komponen SET nama_component = ? WHERE component_id = ?";
    }


}


