package main.java.com.project.app.dao;

import main.java.com.project.app.model.SuratPenawaranModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SuratPenawaranDAO {
    private Connection conn;

    public SuratPenawaranDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertSuratPenawaran(SuratPenawaranModel sp) {
        String sql = "INSERT INTO surat_penawaran (no_sp, user_id, company_id, perihal, tanggal_surat_penawaran) VALUES (?, ?, ?, ?, ?)";
        boolean success = false;

        try {
            // Matikan auto-commit
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, sp.getNoSP());
                ps.setInt(2, sp.getUserId());
                ps.setInt(3, sp.getCompanyId());
                ps.setString(4, sp.getPerihal());
                ps.setDate(5, java.sql.Date.valueOf(sp.getTanggalSuratPenawaran()));

                success = ps.executeUpdate() > 0;
            }

            // Kalau berhasil, commit
            if (success) {
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // rollback kalau ada error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true); // kembalikan auto-commit ke default
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }
}
