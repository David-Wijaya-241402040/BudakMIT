// main/java/com/project/app/dao/DetailPekerjaanDAO.java
package main.java.com.project.app.dao;

import main.java.com.project.app.model.DetailPekerjaanModel;
import main.java.com.project.app.model.ItemPenawaranModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetailPekerjaanDAO {
    private Connection connection;

    public DetailPekerjaanDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean deleteByJobId(Long jobId) {
        String sql = "DELETE FROM detail_pekerjaan WHERE job_id = ?";
        boolean success = false;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, jobId);
                int affectedRows = ps.executeUpdate();
                success = affectedRows > 0;
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    // Insert batch detail pekerjaan
    public boolean insertDetailPekerjaanBatch(List<DetailPekerjaanModel> details) {
        String sql = "INSERT INTO detail_pekerjaan (job_id, component_id, qty, harga_aktual) VALUES (?, ?, ?, ?)";
        boolean success = false;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                for (DetailPekerjaanModel detail : details) {
                    pstmt.setLong(1, detail.getJobId());
                    pstmt.setLong(2, detail.getComponentId());
                    pstmt.setInt(3, detail.getQty());
                    pstmt.setDouble(4, detail.getHargaAktual());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            connection.commit();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    // Check if component exists in job (untuk constraint UNIQUE)
    public boolean isComponentInJob(Long jobId, Long componentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM detail_pekerjaan " +
                "WHERE job_id = ? AND component_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, jobId);
            pstmt.setLong(2, componentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public List<ItemPenawaranModel> getItemsByJobId(Long jobId) throws SQLException {
        List<ItemPenawaranModel> items = new ArrayList<>();

        String sql = """
        SELECT k.nama_component, dp.qty, dp.harga_aktual
        FROM detail_pekerjaan dp
        JOIN komponen k ON dp.component_id = k.component_id
        WHERE dp.job_id = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, jobId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemPenawaranModel item = new ItemPenawaranModel(
                        rs.getString("nama_component"),
                        rs.getInt("qty"),
                        rs.getDouble("harga_aktual")
                );
                items.add(item);
            }
        }
        return items;
    }

}