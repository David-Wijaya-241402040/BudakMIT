// main/java/com/project/app/model/Job.java
package main.java.com.project.app.model;

import java.sql.Timestamp;

public class JobModel {
    private Long jobId;
    private Long spId;
    private String namaPekerjaan;
    private String namaMesin;
    private String spesifikasiMesin;
    private String deskripsiPekerjaan;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public JobModel() {}

    public JobModel(Long spId, String namaPekerjaan, String namaMesin, String spesifikasiMesin, String deskripsiPekerjaan) {
        this.spId = spId;
        this.namaPekerjaan = namaPekerjaan;
        this.namaMesin = namaMesin;
        this.spesifikasiMesin = spesifikasiMesin;
        this.deskripsiPekerjaan = deskripsiPekerjaan;
    }

    // Getters and Setters
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public Long getSpId() { return spId; }
    public void setSpId(Long spId) { this.spId = spId; }

    public String getNamaPekerjaan() { return namaPekerjaan; }
    public void setNamaPekerjaan(String namaPekerjaan) { this.namaPekerjaan = namaPekerjaan; }

    public String getNamaMesin() { return namaMesin; }
    public void setNamaMesin(String namaMesin) { this.namaMesin = namaMesin; }

    public String getSpesifikasiMesin() { return spesifikasiMesin; }
    public void setSpesifikasiMesin(String spesifikasiMesin) { this.spesifikasiMesin = spesifikasiMesin; }

    public String getDeskripsiPekerjaan() { return deskripsiPekerjaan; }
    public void setDeskripsiPekerjaan(String deskripsiPekerjaan) { this.deskripsiPekerjaan = deskripsiPekerjaan; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}