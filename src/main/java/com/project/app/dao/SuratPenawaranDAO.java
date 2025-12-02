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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getNoSP());
            ps.setInt(2, sp.getUserId());
            ps.setInt(3, sp.getCompanyId());
            ps.setString(4, sp.getPerihal());
            ps.setDate(5, java.sql.Date.valueOf(sp.getTanggalSuratPenawaran()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
