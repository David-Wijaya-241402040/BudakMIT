package main.java.com.project.app.dao;

import main.java.com.project.app.model.CompanyModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    private Connection conn;

    public CompanyDAO(Connection conn) {
        this.conn = conn;
    }

    public List<CompanyModel> getAllCompanies() {
        List<CompanyModel> list = new ArrayList<>();
        String sql = "SELECT * FROM view_company_list";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new CompanyModel(
                        rs.getInt("company_id"),
                        rs.getString("nama_perusahaan"),
                        rs.getString("no_telp_perusahaan"),
                        rs.getString("email_perusahaan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<CompanyModel> searchCompanyByName(String keyword) {
        List<CompanyModel> list = new ArrayList<>();
        String sql = "SELECT * FROM view_company_list WHERE nama_perusahaan LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CompanyModel(
                        rs.getInt("company_id"),
                        rs.getString("nama_perusahaan"),
                        rs.getString("no_telp_perusahaan"),
                        rs.getString("email_perusahaan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
