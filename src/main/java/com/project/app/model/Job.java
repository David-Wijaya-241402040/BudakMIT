// main/java/com/project/app/model/Job.java
package main.java.com.project.app.model;

import java.sql.Timestamp;

public class Job {
    private Long jobId;
    private Long spId;
    private String namaPekerjaan;
    private String namaMesin;
    private String spesifikasiMesin;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public Job() {}

    public Job(Long spId, String namaPekerjaan, String namaMesin, String spesifikasiMesin) {
        this.spId = spId;
        this.namaPekerjaan = namaPekerjaan;
        this.namaMesin = namaMesin;
        this.spesifikasiMesin = spesifikasiMesin;
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

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}