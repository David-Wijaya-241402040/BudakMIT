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
    public Long insertJob(JobModel job) throws SQLException {
        String sql = "INSERT INTO jobs (sp_id, nama_pekerjaan, nama_mesin, spesifikasi_mesin, deskripsi_pekerjaan) " +
                "VALUES (?, ?, ?, ?, ?)";

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
                    return generatedKeys.getLong(1); // Return job_id yang baru dibuat
                } else {
                    throw new SQLException("Creating job failed, no ID obtained.");
                }
            }
        }
    }

    // Get jobs by SP ID
    public List<JobModel> getJobsBySpId(Long spId) throws SQLException {
        List<JobModel> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE sp_id = ? ORDER BY job_id";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, spId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                JobModel job = new JobModel();
                job.setJobId(rs.getLong("job_id"));
                job.setSpId(rs.getLong("sp_id"));
                job.setNamaPekerjaan(rs.getString("nama_pekerjaan"));
                job.setNamaMesin(rs.getString("nama_mesin"));
                job.setSpesifikasiMesin(rs.getString("spesifikasi_mesin"));
                job.setCreatedAt(rs.getTimestamp("created_at"));
                job.setUpdatedAt(rs.getTimestamp("updated_at"));

                jobs.add(job);
            }
        }
        return jobs;
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

    // Delete job (akan cascade delete detail_pekerjaan)
    public boolean deleteJob(Long jobId) throws SQLException {
        String sql = "DELETE FROM jobs WHERE job_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, jobId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}