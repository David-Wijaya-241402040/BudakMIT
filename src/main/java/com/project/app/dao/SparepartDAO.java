package main.java.com.project.app.dao;

import main.java.com.project.app.model.SparepartModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SparepartDAO {

    private Connection conn;

    public SparepartDAO(Connection conn) {
        this.conn = conn;
    }

    public List<SparepartModel> getAll() throws SQLException {
        List<SparepartModel> list = new ArrayList<>();

        String sql = "SELECT component_id, nama_component, nama_satuan, qty, " +
                "harga_acuan_lama, harga_acuan_baru, tanggal_berlaku_baru " +
                "FROM view_komponen_harga_detail";

        PreparedStatement st = conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(new SparepartModel(
                    rs.getInt("component_id"),
                    rs.getString("nama_component"),
                    rs.getString("nama_satuan"),
                    rs.getInt("qty"),
                    rs.getDouble("harga_acuan_lama"),
                    rs.getDouble("harga_acuan_baru"),
                    rs.getString("tanggal_berlaku_baru")
            ));
        }

        return list;
    }

    public List<SparepartModel> searchKomponen(String keyword) throws SQLException {
        List<SparepartModel> list = new ArrayList<>();

        String sql = "SELECT * FROM view_komponen_harga_detail " +
                "WHERE nama_component LIKE ? OR nama_satuan LIKE ?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, "%" + keyword + "%");
        st.setString(2, "%" + keyword + "%");

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(new SparepartModel(
                    rs.getInt("component_id"),
                    rs.getString("nama_component"),
                    rs.getString("nama_satuan"),
                    rs.getInt("qty"),
                    rs.getDouble("harga_acuan_lama"),
                    rs.getDouble("harga_acuan_baru"),
                    rs.getString("tanggal_berlaku_baru")
            ));
        }

        return list;
    }

    public List<SparepartModel> searchByCategory(String keyword, String category) throws SQLException {
        List<SparepartModel> list = new ArrayList<>();

        String sql;

        if (category.equals("Nama Komponen")) {
            sql = "SELECT * FROM view_komponen_harga_detail WHERE nama_component LIKE ?";
        }
        else if (category.equals("Satuan")) {
            sql = "SELECT * FROM view_komponen_harga_detail WHERE nama_satuan LIKE ?";
        }
        else {
            // All: cari keduanya
            sql = "SELECT * FROM view_komponen_harga_detail " +
                    "WHERE nama_component LIKE ? OR nama_satuan LIKE ?";
        }

        PreparedStatement st = conn.prepareStatement(sql);

        if (category.equals("All")) {
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
        } else {
            st.setString(1, "%" + keyword + "%");
        }

        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            list.add(new SparepartModel(
                    rs.getInt("component_id"),
                    rs.getString("nama_component"),
                    rs.getString("nama_satuan"),
                    rs.getInt("qty"),
                    rs.getDouble("harga_acuan_lama"),
                    rs.getDouble("harga_acuan_baru"),
                    rs.getString("tanggal_berlaku_baru")
            ));
        }

        return list;
    }

    public boolean addSparepart(String nama, String satuan, int qty, double hargaBaru) throws SQLException {
        String sql = "INSERT INTO komponen (nama_component, nama_satuan, qty, harga_acuan, tanggal_berlaku) " +
                "VALUES (?, ?, ?, ?, CURRENT_DATE)";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, nama);
        st.setString(2, satuan);
        st.setInt(3, qty);
        st.setDouble(4, hargaBaru);

        return st.executeUpdate() > 0;
    }

    public boolean updateSparepart(int id, String nama, String satuan, int qty, double hargaBaru) throws SQLException {
        String sql = "UPDATE komponen SET nama_component=?, nama_satuan=?, qty=?, " +
                "harga_acuan=?, tanggal_berlaku=CURRENT_DATE WHERE component_id=?";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, nama);
        st.setString(2, satuan);
        st.setInt(3, qty);
        st.setDouble(4, hargaBaru);
        st.setInt(5, id);

        return st.executeUpdate() > 0;
    }

    public boolean deleteSparepart(int id) {
        String sql = "DELETE FROM komponen WHERE component_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // true kalau ada baris terhapus
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
