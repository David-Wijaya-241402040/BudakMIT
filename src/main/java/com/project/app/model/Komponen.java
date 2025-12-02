// main/java/com/project/app/model/Komponen.java
package main.java.com.project.app.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Komponen {
    private Long componentId;
    private String namaComponent;
    private String namaSatuan;
    private Integer qty;
    private BigDecimal hargaAcuan;
    private Timestamp tanggalBerlaku;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Getters and Setters
    public Long getComponentId() { return componentId; }
    public void setComponentId(Long componentId) { this.componentId = componentId; }

    public String getNamaComponent() { return namaComponent; }
    public void setNamaComponent(String namaComponent) { this.namaComponent = namaComponent; }

    public String getNamaSatuan() { return namaSatuan; }
    public void setNamaSatuan(String namaSatuan) { this.namaSatuan = namaSatuan; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public BigDecimal getHargaAcuan() { return hargaAcuan; }
    public void setHargaAcuan(BigDecimal hargaAcuan) { this.hargaAcuan = hargaAcuan; }

    public Timestamp getTanggalBerlaku() { return tanggalBerlaku; }
    public void setTanggalBerlaku(Timestamp tanggalBerlaku) { this.tanggalBerlaku = tanggalBerlaku; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}