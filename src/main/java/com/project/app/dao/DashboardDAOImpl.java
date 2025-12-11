package main.java.com.project.app.dao;

import main.java.com.project.app.model.*;
import main.java.com.project.app.config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAOImpl implements DashboardDAO {

    // ---------- Helper to compute current month in 'YYYY-MM' ----------
    private String currentYearMonth() {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT DATE_FORMAT(CURDATE(), '%Y-%m')")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ---------------- Legacy methods (call range with current month) ----------------
    @Override
    public int countSPThisMonth() {
        String ym = currentYearMonth();
        return countSPBetween(ym, ym);
    }

    @Override
    public int countTagihanPendingThisMonth() {
        String ym = currentYearMonth();
        return countTagihanPendingBetween(ym, ym);
    }

    @Override
    public int countTagihanLunasThisMonth() {
        String ym = currentYearMonth();
        return countTagihanLunasBetween(ym, ym);
    }

    @Override
    public int countTagihanBatalThisMonth() {
        String ym = currentYearMonth();
        return countTagihanBatalBetween(ym, ym);
    }

    @Override
    public List<UserSummary> getStaffSummariesThisMonth() {
        String ym = currentYearMonth();
        return getStaffSummariesBetween(ym, ym);
    }

    @Override
    public List<MonthlyDocumentDetail> getMonthlyDocumentDetails() {
        String ym = currentYearMonth();
        return getMonthlyDocumentDetailsBetween(ym, ym);
    }

    @Override
    public StatusSummary getStatusSummaryThisMonth() {
        String ym = currentYearMonth();
        return getStatusSummaryBetween(ym, ym);
    }

    // ---------------- New range methods (YYYY-MM) ----------------

    @Override
    public int countSPBetween(String startYearMonth, String endYearMonth) {
        String sql = "SELECT COUNT(*) FROM surat_penawaran " +
                "WHERE DATE_FORMAT(tanggal_surat_penawaran, '%Y-%m') BETWEEN ? AND ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, startYearMonth);
            ps.setString(2, endYearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int countTagihanPendingBetween(String startYearMonth, String endYearMonth) {
        String sql = "SELECT COUNT(*) FROM tagihan t " +
                "JOIN surat_penawaran sp ON t.sp_id = sp.sp_id " +
                "WHERE DATE_FORMAT(t.created_at, '%Y-%m') BETWEEN ? AND ? " +
                "AND (t.status_pembayaran IN ('pending','telat','overdue','belum_dibayar'))";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, startYearMonth);
            ps.setString(2, endYearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int countTagihanLunasBetween(String startYearMonth, String endYearMonth) {
        String sql = "SELECT COUNT(*) FROM tagihan " +
                "WHERE DATE_FORMAT(created_at, '%Y-%m') BETWEEN ? AND ? " +
                "AND (status_pembayaran IN ('dibayar','lunas'))";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, startYearMonth);
            ps.setString(2, endYearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int countTagihanBatalBetween(String startYearMonth, String endYearMonth) {
        String sql = "SELECT COUNT(*) FROM tagihan " +
                "WHERE DATE_FORMAT(created_at, '%Y-%m') BETWEEN ? AND ? " +
                "AND status_pembayaran = 'batal'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, startYearMonth);
            ps.setString(2, endYearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public List<UserSummary> getStaffSummariesBetween(String startYearMonth, String endYearMonth) {
        List<UserSummary> list = new ArrayList<>();
        String sql =
                "SELECT u.user_id, u.nickname, " +
                        "  (SELECT COUNT(*) FROM surat_penawaran sp WHERE sp.user_id = u.user_id AND DATE_FORMAT(sp.tanggal_surat_penawaran, '%Y-%m') BETWEEN ? AND ?) AS total_sp, " +
                        "  (SELECT COUNT(*) FROM tagihan t JOIN surat_penawaran sp2 ON t.sp_id = sp2.sp_id WHERE sp2.user_id = u.user_id AND DATE_FORMAT(t.created_at, '%Y-%m') BETWEEN ? AND ? AND (t.status_pembayaran IN ('pending','telat','overdue','belum_dibayar'))) AS total_pending, " +
                        "  (SELECT COUNT(*) FROM tagihan t JOIN surat_penawaran sp3 ON t.sp_id = sp3.sp_id WHERE sp3.user_id = u.user_id AND DATE_FORMAT(t.created_at, '%Y-%m') BETWEEN ? AND ? AND (t.status_pembayaran IN ('dibayar','lunas'))) AS total_lunas, " +
                        "  (SELECT COUNT(*) FROM tagihan t JOIN surat_penawaran sp4 ON t.sp_id = sp4.sp_id WHERE sp4.user_id = u.user_id AND DATE_FORMAT(t.created_at, '%Y-%m') BETWEEN ? AND ? AND t.status_pembayaran='batal') AS total_batal " +
                        "FROM users u " +
                        "WHERE u.roles = 'staff'";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            // set params (we have repeats)
            ps.setString(1, startYearMonth);
            ps.setString(2, endYearMonth);

            ps.setString(3, startYearMonth);
            ps.setString(4, endYearMonth);

            ps.setString(5, startYearMonth);
            ps.setString(6, endYearMonth);

            ps.setString(7, startYearMonth);
            ps.setString(8, endYearMonth);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserSummary s = new UserSummary(
                            rs.getInt("user_id"),
                            rs.getString("nickname"),
                            rs.getInt("total_sp"),
                            rs.getInt("total_pending"),
                            rs.getInt("total_lunas"),
                            rs.getInt("total_batal")
                    );
                    list.add(s);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<MonthlyDocumentDetail> getMonthlyDocumentDetailsBetween(String startYearMonth, String endYearMonth) {
        List<MonthlyDocumentDetail> list = new ArrayList<>();
        int sp = countSPBetween(startYearMonth, endYearMonth);
        int pending = countTagihanPendingBetween(startYearMonth, endYearMonth);
        int lunas = countTagihanLunasBetween(startYearMonth, endYearMonth);
        int batal = countTagihanBatalBetween(startYearMonth, endYearMonth);

        list.add(new MonthlyDocumentDetail("Surat Penawaran", sp));
        list.add(new MonthlyDocumentDetail("Tagihan Pending", pending));
        list.add(new MonthlyDocumentDetail("Tagihan Lunas", lunas));
        list.add(new MonthlyDocumentDetail("Tagihan Batal", batal));
        return list;
    }

    @Override
    public StatusSummary getStatusSummaryBetween(String startYearMonth, String endYearMonth) {
        String sql = "SELECT " +
                "SUM(CASE WHEN status_pembayaran IN ('pending','belum_dibayar') THEN 1 ELSE 0 END) AS pending, " +
                "SUM(CASE WHEN status_pembayaran IN ('telat','overdue') THEN 1 ELSE 0 END) AS telat, " +
                "SUM(CASE WHEN status_pembayaran IN ('dibayar','lunas') THEN 1 ELSE 0 END) AS lunas, " +
                "SUM(CASE WHEN status_pembayaran = 'batal' THEN 1 ELSE 0 END) AS batal " +
                "FROM tagihan WHERE DATE_FORMAT(created_at, '%Y-%m') BETWEEN ? AND ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, startYearMonth);
            ps.setString(2, endYearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StatusSummary(rs.getInt("pending"),
                            rs.getInt("telat"),
                            rs.getInt("lunas"),
                            rs.getInt("batal"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return new StatusSummary(0,0,0,0);
    }
}
