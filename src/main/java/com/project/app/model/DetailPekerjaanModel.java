// main/java/com/project/app/model/DetailPekerjaan.java
package main.java.com.project.app.model;

import java.sql.Timestamp;

public class DetailPekerjaanModel {
    private Long id;
    private Long jobId;
    private Long componentId;
    private Integer qty;
    private Double hargaAktual;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public DetailPekerjaanModel() {}

    public DetailPekerjaanModel(Long jobId, Long componentId, Integer qty, Double hargaAktual) {
        this.jobId = jobId;
        this.componentId = componentId;
        this.qty = qty;
        this.hargaAktual = hargaAktual;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public Long getComponentId() { return componentId; }
    public void setComponentId(Long componentId) { this.componentId = componentId; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Double getHargaAktual() { return hargaAktual; }
    public void setHargaAktual(Double hargaAktual) { this.hargaAktual = hargaAktual; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}