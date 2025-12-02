package main.java.com.project.app.dao;

import main.java.com.project.app.model.AddCompanyModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddCompanyDAO {
    private Connection conn;

    public AddCompanyDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertCompany(AddCompanyModel company) {
        String sql = "INSERT INTO perusahaan (nama_perusahaan, alamat_perusahaan, no_telp_perusahaan, email_perusahaan) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, company.getNamaPerusahaan());
            ps.setString(2, company.getAlamatPerusahaan());
            ps.setString(3, company.getNoTelpPerusahaan());
            ps.setString(4, company.getEmailPerusahaan());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    company.setCompanyId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
