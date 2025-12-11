package main.java.com.project.app.dao;

import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.model.PenawaranModel;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class PenawaranDAO {

    public Map<String, PenawaranModel.SPItem> loadSP() throws SQLException {
        String sql = "SELECT * FROM view_surat_penawaran_detail ORDER BY sp_id ASC, job_id ASC";

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

}