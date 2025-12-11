package main.java.com.project.app.dao;

import main.java.com.project.app.model.LogAktivitas;
import main.java.com.project.app.model.StatistikDokumen;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.session.Session;

import java.sql.*;
import java.util.*;

public class StaffDashboardDAOImpl implements StaffDashboardDAO {

    @Override
    public List<LogAktivitas> getAktivitasUser(int userId, int limit) throws Exception {
        String sql = "SELECT log_id, nama_tabel, aksi, data_lama, data_baru, user_id, waktu " +
                "FROM log_aktivitas WHERE user_id = ? ORDER BY waktu DESC LIMIT ?";
        List<LogAktivitas> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new LogAktivitas(
                            rs.getInt("log_id"),
                            rs.getString("nama_tabel"),
                            rs.getString("aksi"),
                            rs.getString("data_lama"),
                            rs.getString("data_baru"),
                            rs.getInt("user_id"),
                            rs.getString("waktu")
                    ));
                }
            }
        }
        return list;
    }

    @Override
    public List<StatistikDokumen> getDokumenBulanan(int userId, int month, int year) throws Exception {
        // counts for: surat penawaran, tagihan (all), tagihan by status optionally
        List<StatistikDokumen> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection()) {
            // Surat Penawaran by tanggal_surat_penawaran
            String qSp = "SELECT COUNT(*) AS cnt FROM surat_penawaran WHERE user_id = ? AND MONTH(tanggal_surat_penawaran)=? AND YEAR(tanggal_surat_penawaran)=?";
            try (PreparedStatement ps = c.prepareStatement(qSp)) {
                ps.setInt(1, userId);
                ps.setInt(2, month);
                ps.setInt(3, year);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) list.add(new StatistikDokumen("Surat Penawaran", rs.getInt("cnt")));
                }
            }

            // Tagihan by created_at (assumption: tagihan.created_at exists)
            String qTag = "SELECT COUNT(*) AS cnt FROM tagihan t JOIN surat_penawaran sp ON t.sp_id = sp.sp_id WHERE sp.user_id = ? AND MONTH(t.created_at)=? AND YEAR(t.created_at)=?";
            try (PreparedStatement ps = c.prepareStatement(qTag)) {
                ps.setInt(1, userId);
                ps.setInt(2, month);
                ps.setInt(3, year);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) list.add(new StatistikDokumen("Tagihan (bulan)", rs.getInt("cnt")));
                }
            }

            // Tagihan by status for that month (optional breakdown)
            String qStatus = "SELECT status_pembayaran, COUNT(*) AS cnt FROM tagihan t JOIN surat_penawaran sp ON t.sp_id = sp.sp_id WHERE sp.user_id = ? AND MONTH(t.created_at)=? AND YEAR(t.created_at)=? GROUP BY status_pembayaran";
            try (PreparedStatement ps = c.prepareStatement(qStatus)) {
                ps.setInt(1, userId);
                ps.setInt(2, month);
                ps.setInt(3, year);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String status = rs.getString("status_pembayaran");
                        int cnt = rs.getInt("cnt");
                        list.add(new StatistikDokumen("Tagihan - " + status, cnt));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Map<String, Integer> getTotalDokumenSaya(int userId) throws Exception {
        Map<String, Integer> map = new HashMap<>();
        try (Connection c = DBConnection.getConnection()) {
            // total surat penawaran
            String q1 = "SELECT COUNT(*) AS cnt FROM surat_penawaran WHERE user_id = ?";
            try (PreparedStatement ps = c.prepareStatement(q1)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) map.put("surat_penawaran", rs.getInt("cnt"));
                }
            }

            // total tagihan
            String q2 = "SELECT COUNT(*) AS cnt FROM tagihan t JOIN surat_penawaran sp ON t.sp_id = sp.sp_id WHERE sp.user_id = ?";
            try (PreparedStatement ps = c.prepareStatement(q2)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) map.put("tagihan", rs.getInt("cnt"));
                }
            }

            // tagihan by statuses
            String q3 = "SELECT status_pembayaran, COUNT(*) AS cnt FROM tagihan t JOIN surat_penawaran sp ON t.sp_id = sp.sp_id WHERE sp.user_id = ? GROUP BY status_pembayaran";
            try (PreparedStatement ps = c.prepareStatement(q3)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        map.put(rs.getString("status_pembayaran"), rs.getInt("cnt"));
                    }
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, Integer> getTagihanStatusCounts(int userId) throws Exception {
        return getTotalDokumenSaya(userId); // alias: same implementation returns status counts
    }

    @Override
    public List<LogAktivitas> searchAktivitas(int userId, String keyword, int limit) throws Exception {
        String sql = "SELECT log_id, nama_tabel, aksi, data_lama, data_baru, user_id, waktu " +
                "FROM log_aktivitas WHERE user_id = ? AND (nama_tabel LIKE ? OR aksi LIKE ? OR data_baru LIKE ? OR data_lama LIKE ?) ORDER BY waktu DESC LIMIT ?";
        List<LogAktivitas> list = new ArrayList<>();
        String like = "%" + keyword + "%";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ps.setString(5, like);
            ps.setInt(6, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new LogAktivitas(
                            rs.getInt("log_id"),
                            rs.getString("nama_tabel"),
                            rs.getString("aksi"),
                            rs.getString("data_lama"),
                            rs.getString("data_baru"),
                            rs.getInt("user_id"),
                            rs.getString("waktu")
                    ));
                }
            }
        }
        return list;
    }
}
