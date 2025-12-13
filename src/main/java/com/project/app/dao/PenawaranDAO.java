package main.java.com.project.app.dao;

import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.model.PenawaranModel;
import main.java.com.project.app.session.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PenawaranDAO {

    public Map<String, PenawaranModel.SPItem> loadSP() throws SQLException {
        int userId = Session.currentUser.getId();
        String role = Session.currentUser.getRoles();
        String sql = "";

        if(role.equals("owner")) {
            sql = "SELECT * FROM view_surat_penawaran_detail ORDER BY sp_id ASC, job_id ASC";
        } else if (role.equals("staff")) {
            sql = "SELECT * FROM view_surat_penawaran_detail WHERE user_id = " + userId +  " ORDER BY sp_id ASC, job_id ASC";
        }

        Map<String, PenawaranModel.SPItem> spMap = new LinkedHashMap<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int spId = rs.getInt("sp_id"); // 🔥 AMBIL SP ID DARI VIEW
                String noSP = rs.getString("no_sp");
                String perusahaan = rs.getString("nama_perusahaan");
                String pembuat = rs.getString("nama_pembuat");

                int jobId = rs.getInt("job_id");
                String pekerjaan = rs.getString("nama_pekerjaan");
                String namaMesin = rs.getString("nama_mesin");
                String spesifikasi = rs.getString("spesifikasi_mesin");
                double harga = rs.getDouble("harga_aktual");

                // jika belum ada → buat SPItem baru
                spMap.putIfAbsent(noSP, new PenawaranModel.SPItem(noSP, perusahaan));

                // SET SP ID BIAR GA 0, harus masuk di tiap iterasi biar selalu update object yang benar
                spMap.get(noSP).sp_id = spId; // ✅ SEKARANG NILAI INI AKAN TERSIMPAN

                // masukin job detail
                if (jobId > 0 && pekerjaan != null) {
                    spMap.get(noSP).jobs.add(
                            new PenawaranModel.JobDetail(jobId, pekerjaan, namaMesin, spesifikasi, harga)
                    );
                }
            }
        }

        return spMap;
    }

    public Map<String, PenawaranModel.SPItem> searchSP(String keyword) throws SQLException {
        String sql =
                "SELECT * FROM view_surat_penawaran_detail " +
                        "WHERE no_sp LIKE ? " +
                        "ORDER BY sp_id ASC, job_id ASC";

        Map<String, PenawaranModel.SPItem> spMap = new LinkedHashMap<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int spId = rs.getInt("sp_id");
                    String noSP = rs.getString("no_sp");
                    String perusahaan = rs.getString("nama_perusahaan");
                    String pembuat = rs.getString("nama_pembuat");

                    int jobId = rs.getInt("job_id");
                    String pekerjaan = rs.getString("nama_pekerjaan");
                    String namaMesin = rs.getString("nama_mesin");
                    String spesifikasi = rs.getString("spesifikasi_mesin");
                    double harga = rs.getDouble("harga_aktual");

                    // kalau SP belum ada → buat
                    spMap.putIfAbsent(noSP, new PenawaranModel.SPItem(noSP, perusahaan));

                    // pastikan SP ID masuk
                    spMap.get(noSP).sp_id = spId;

                    // masukin jobnya
                    if (jobId > 0 && pekerjaan != null) {
                        spMap.get(noSP).jobs.add(
                                new PenawaranModel.JobDetail(jobId, pekerjaan, namaMesin, spesifikasi, harga)
                        );
                    }
                }
            }
        }

        return spMap;
    }

    public boolean deleteSP(int spId) throws SQLException {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Ambil job_id
            List<Integer> jobIds = new ArrayList<>();
            String getJobs = "SELECT job_id FROM jobs WHERE sp_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(getJobs)) {
                ps.setInt(1, spId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        jobIds.add(rs.getInt("job_id"));
                    }
                }
            }

            // 2. Hapus detail_pekerjaan (PALING DALAM)
            String deleteDetail = "DELETE FROM detail_pekerjaan WHERE job_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteDetail)) {
                for (int jobId : jobIds) {
                    ps.setInt(1, jobId);
                    ps.executeUpdate();
                }
            }

            // 3. Hapus jobs
            String deleteJobs = "DELETE FROM jobs WHERE sp_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteJobs)) {
                ps.setInt(1, spId);
                ps.executeUpdate();
            }

            // 4. Hapus tagihan (FK LANGSUNG KE SP)
            String deleteTagihan = "DELETE FROM tagihan WHERE sp_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteTagihan)) {
                ps.setInt(1, spId);
                ps.executeUpdate();
            }

            // 5. BARU hapus surat_penawaran
            int affected;
            String deleteSP = "DELETE FROM surat_penawaran WHERE sp_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSP)) {
                ps.setInt(1, spId);
                affected = ps.executeUpdate();
            }

            conn.commit();
            return affected > 0;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

}