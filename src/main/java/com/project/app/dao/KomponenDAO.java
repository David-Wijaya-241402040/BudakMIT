// Update di KomponenDAO.java
package main.java.com.project.app.dao;

import main.java.com.project.app.model.Komponen;
import main.java.com.project.app.config.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KomponenDAO {
    private Connection connection;

    public KomponenDAO(Connection connection) {
        this.connection = connection;
    }

    // Get all nama komponen
    public List<String> getAllNamaKomponen() throws SQLException {
        List<String> komponenList = new ArrayList<>();
        String sql = "SELECT nama_component FROM komponen ORDER BY nama_component";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                komponenList.add(rs.getString("nama_component"));
            }
        }
        return komponenList;
    }

    // Get komponen by name
    public Komponen getKomponenByName(String nama) throws SQLException {
        String sql = "SELECT * FROM komponen WHERE nama_component = ? LIMIT 1";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nama);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Komponen komponen = new Komponen();
                komponen.setComponentId(rs.getLong("component_id"));
                komponen.setNamaComponent(rs.getString("nama_component"));
                komponen.setNamaSatuan(rs.getString("nama_satuan"));
                komponen.setQty(rs.getInt("qty"));
                komponen.setHargaAcuan(rs.getBigDecimal("harga_acuan"));
                komponen.setTanggalBerlaku(rs.getTimestamp("tanggal_berlaku"));
                komponen.setCreatedAt(rs.getTimestamp("created_at"));
                komponen.setUpdatedAt(rs.getTimestamp("updated_at"));
                return komponen;
            }
            return null;
        }
    }

    // Get component ID by name (method baru)
    public Long getComponentIdByName(String namaComponent) throws SQLException {
        String sql = "SELECT component_id FROM komponen WHERE nama_component = ? LIMIT 1";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, namaComponent);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("component_id");
            }
            return null;
        }
    }

    // Insert new komponen
    public Long insertKomponen(Komponen komponen) throws SQLException {
        String sql = "INSERT INTO komponen (nama_component, nama_satuan, qty, harga_acuan, tanggal_berlaku) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, komponen.getNamaComponent());
            pstmt.setString(2, komponen.getNamaSatuan());
            pstmt.setInt(3, komponen.getQty());
            pstmt.setBigDecimal(4, komponen.getHargaAcuan());
            pstmt.setTimestamp(5, komponen.getTanggalBerlaku());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating komponen failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating komponen failed, no ID obtained.");
                }
            }
        }
    }
    // Tambahkan method ini di KomponenDAO
    public BigDecimal getHargaAcuanByComponentId(Long componentId) throws SQLException {
        String sql = "SELECT harga_acuan FROM komponen WHERE component_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, componentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("harga_acuan");
            }
            return BigDecimal.ZERO;
        }
    }

    // Atau jika ingin mendapatkan data lengkap
    public Komponen getKomponenById(Long componentId) throws SQLException {
        String sql = "SELECT * FROM komponen WHERE component_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, componentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Komponen komponen = new Komponen();
                komponen.setComponentId(rs.getLong("component_id"));
                komponen.setNamaComponent(rs.getString("nama_component"));
                komponen.setNamaSatuan(rs.getString("nama_satuan"));
                komponen.setQty(rs.getInt("qty"));
                komponen.setHargaAcuan(rs.getBigDecimal("harga_acuan"));
                komponen.setTanggalBerlaku(rs.getTimestamp("tanggal_berlaku"));
                komponen.setCreatedAt(rs.getTimestamp("created_at"));
                komponen.setUpdatedAt(rs.getTimestamp("updated_at"));
                return komponen;
            }
            return null;
        }
    }
}