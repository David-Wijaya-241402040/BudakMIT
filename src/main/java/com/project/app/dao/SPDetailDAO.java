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

<<<<<<< HEAD
    public List<PenawaranModel.SPJobComponent> getDetailBySP(String noSP) {
        List<PenawaranModel.SPJobComponent> list = new ArrayList<>();
        String sql = "SELECT * FROM view_surat_penawaran_jobs_komponen WHERE no_sp = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noSP);
=======
    public List<PenawaranModel.SPJobComponent> getDetailBySP(int spId) {
        List<PenawaranModel.SPJobComponent> list = new ArrayList<>();
        String sql = "SELECT * FROM view_surat_penawaran_jobs_komponen WHERE sp_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, spId);
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PenawaranModel.SPJobComponent(
                        rs.getInt("sp_id"),
                        rs.getString("no_sp"),
                        rs.getString("perihal"),
<<<<<<< HEAD
=======
                        rs.getString("user_id"),
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
                        rs.getString("tanggal_surat_penawaran"),
                        rs.getString("nama_perusahaan"),
                        rs.getInt("job_id"),
                        rs.getString("nama_pekerjaan"),
                        rs.getString("nama_mesin"),
                        rs.getString("spesifikasi_mesin"),
<<<<<<< HEAD
                        rs.getInt("component_id"),
                        rs.getString("nama_component"),
                        rs.getDouble("harga_acuan")
=======
                        rs.getString("deskripsi_pekerjaan"),
                        rs.getInt("component_id"),
                        rs.getString("nama_component"),
                        rs.getInt("qty"),
                        rs.getDouble("harga_acuan"),
                        rs.getDouble("harga_aktual"),
                        rs.getDouble("harga_komponen")
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
<<<<<<< HEAD
    public void deleteComponent(int componentId) {
        String sql = "DELETE FROM komponen WHERE component_id = ?";
        // eksekusi delete ke :contentReference[oaicite:4]{index=4}
    }

    public void updateComponent(int componentId, String namaBaru) {
        String sql = "UPDATE komponen SET nama_component = ? WHERE component_id = ?";
    }


}


=======
    public boolean deleteJobById(int jobId) throws SQLException {
        String deleteDetail = "DELETE FROM detail_pekerjaan WHERE job_id = ?";
        String deleteJob = "DELETE FROM jobs WHERE job_id = ?";

        try (PreparedStatement psDetail = conn.prepareStatement(deleteDetail);
             PreparedStatement psJob = conn.prepareStatement(deleteJob)) {

            conn.setAutoCommit(false);

            psDetail.setInt(1, jobId);
            psDetail.executeUpdate();

            psJob.setInt(1, jobId);
            int affected = psJob.executeUpdate();

            conn.commit();
            return affected > 0;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

<<<<<<< HEAD
}
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
=======
    public List<PenawaranModel.SPJobComponent> searchDetailBySPAndNamaPekerjaan(int spId, String keyword) {
        List<PenawaranModel.SPJobComponent> list = new ArrayList<>();
        String sql = "SELECT * FROM view_surat_penawaran_jobs_komponen " +
                "WHERE sp_id = ? AND nama_pekerjaan LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, spId);
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PenawaranModel.SPJobComponent(
                        rs.getInt("sp_id"),
                        rs.getString("no_sp"),
                        rs.getString("perihal"),
                        rs.getString("user_id"),
                        rs.getString("tanggal_surat_penawaran"),
                        rs.getString("nama_perusahaan"),
                        rs.getInt("job_id"),
                        rs.getString("nama_pekerjaan"),
                        rs.getString("nama_mesin"),
                        rs.getString("spesifikasi_mesin"),
                        rs.getString("deskripsi_pekerjaan"),
                        rs.getInt("component_id"),
                        rs.getString("nama_component"),
                        rs.getInt("qty"),
                        rs.getDouble("harga_acuan"),
                        rs.getDouble("harga_aktual"),
                        rs.getDouble("harga_komponen")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
>>>>>>> c2c9aeaaabe1c6b3bac92ac3ab866f179bb78efd
