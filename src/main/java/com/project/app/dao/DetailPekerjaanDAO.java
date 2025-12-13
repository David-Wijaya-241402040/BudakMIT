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

    // Insert detail pekerjaan (komponen)
    public void insertDetailPekerjaan(DetailPekerjaanModel detail) throws SQLException {
        String sql = "INSERT INTO detail_pekerjaan (job_id, component_id, qty, harga_aktual) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, detail.getJobId());
            pstmt.setLong(2, detail.getComponentId());
            pstmt.setInt(3, detail.getQty());
            pstmt.setDouble(4, detail.getHargaAktual());
            pstmt.executeUpdate();
        }
    }

    public void deleteByJobId(Long jobId) throws SQLException {
        String sql = "DELETE FROM detail_pekerjaan WHERE job_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, jobId);
            ps.executeUpdate();
        }
    }


    // Insert batch detail pekerjaan
    public void insertDetailPekerjaanBatch(List<DetailPekerjaanModel> details) throws SQLException {
        String sql = "INSERT INTO detail_pekerjaan (job_id, component_id, qty, harga_aktual) " +
                "VALUES (?, ?, ?, ?)";

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
    }

    // Get detail pekerjaan by job ID
    public List<DetailPekerjaanModel> getDetailByJobId(Long jobId) throws SQLException {
        List<DetailPekerjaanModel> details = new ArrayList<>();
        String sql = "SELECT dp.*, k.nama_component " +
                "FROM detail_pekerjaan dp " +
                "JOIN komponen k ON dp.component_id = k.component_id " +
                "WHERE dp.job_id = ? " +
                "ORDER BY dp.id";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, jobId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                DetailPekerjaanModel detail = new DetailPekerjaanModel();
                detail.setId(rs.getLong("id"));
                detail.setJobId(rs.getLong("job_id"));
                detail.setComponentId(rs.getLong("component_id"));
                detail.setQty(rs.getInt("qty"));
                detail.setHargaAktual(rs.getDouble("harga_aktual"));
                detail.setCreatedAt(rs.getTimestamp("created_at"));
                detail.setUpdatedAt(rs.getTimestamp("updated_at"));

                details.add(detail);
            }
        }
        return details;
    }

    // Update detail pekerjaan
    public boolean updateDetailPekerjaan(DetailPekerjaanModel detail) throws SQLException {
        String sql = "UPDATE detail_pekerjaan SET qty = ?, harga_aktual = ?, " +
                "updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, detail.getQty());
            pstmt.setDouble(2, detail.getHargaAktual());
            pstmt.setLong(3, detail.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete detail pekerjaan
    public boolean deleteDetailPekerjaan(Long id) throws SQLException {
        String sql = "DELETE FROM detail_pekerjaan WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Get component ID by name
    public Long getComponentIdByName(String componentName) throws SQLException {
        String sql = "SELECT component_id FROM komponen WHERE nama_component = ? LIMIT 1";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, componentName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("component_id");
            }
            return null;
        }
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