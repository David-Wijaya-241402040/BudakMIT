// main/java/com/project/app/dao/JobDAO.java
package main.java.com.project.app.dao;

import main.java.com.project.app.model.JobModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {
    private Connection connection;

    public JobDAO(Connection connection) {
        this.connection = connection;
    }

    // Insert Job (pekerjaan)
    public Long insertJob(JobModel job) {
        String sql = "INSERT INTO jobs (sp_id, nama_pekerjaan, nama_mesin, spesifikasi_mesin, deskripsi_pekerjaan) " +
                "VALUES (?, ?, ?, ?, ?)";
        Long jobId = null;

        try {
            connection.setAutoCommit(false); // mulai transaksi

            try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setLong(1, job.getSpId());
                pstmt.setString(2, job.getNamaPekerjaan());
                pstmt.setString(3, job.getNamaMesin());
                pstmt.setString(4, job.getSpesifikasiMesin());
                pstmt.setString(5, job.getDeskripsiPekerjaan());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating job failed, no rows affected.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        jobId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Creating job failed, no ID obtained.");
                    }
                }
            }

            connection.commit(); // commit kalau sukses
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // rollback kalau error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true); // kembalikan auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return jobId;
    }

    // Update job
    public boolean updateJob(JobModel job) throws SQLException {
        String sql = "UPDATE jobs SET nama_pekerjaan = ?, nama_mesin = ?, " +
                "spesifikasi_mesin = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE job_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, job.getNamaPekerjaan());
            pstmt.setString(2, job.getNamaMesin());
            pstmt.setString(3, job.getSpesifikasiMesin());
            pstmt.setLong(4, job.getJobId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public JobModel getJobById(Long jobId) throws SQLException {
        String sql = "SELECT * FROM jobs WHERE job_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, jobId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JobModel job = new JobModel();
                job.setJobId(rs.getLong("job_id"));
                job.setSpId(rs.getLong("sp_id"));
                job.setNamaPekerjaan(rs.getString("nama_pekerjaan"));
                job.setNamaMesin(rs.getString("nama_mesin"));
                job.setSpesifikasiMesin(rs.getString("spesifikasi_mesin"));
                job.setDeskripsiPekerjaan(rs.getString("deskripsi_pekerjaan"));
                return job;
            }
            return null;
        }
    }

}