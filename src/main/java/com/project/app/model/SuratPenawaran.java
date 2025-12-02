package main.java.com.project.app.model;

import javafx.beans.property.*;

public class SuratPenawaran {
    private final IntegerProperty spId;
    private final StringProperty namaPerusahaan;
    private final StringProperty adminId;
    private final IntegerProperty spNumber;
    private final DoubleProperty totalHarga;
    private final StringProperty namaPekerjaan;
    private final StringProperty tanggal;

    // Constructor
    public SuratPenawaran(Integer spId, String namaPerusahaan, String adminId,
                          Integer spNumber, Double totalHarga, String namaPekerjaan, String tanggal) {
        this.spId = new SimpleIntegerProperty(spId);
        this.namaPerusahaan = new SimpleStringProperty(namaPerusahaan);
        this.adminId = new SimpleStringProperty(adminId);
        this.spNumber = new SimpleIntegerProperty(spNumber);
        this.totalHarga = new SimpleDoubleProperty(totalHarga);
        this.namaPekerjaan = new SimpleStringProperty(namaPekerjaan);
        this.tanggal = new SimpleStringProperty(tanggal);
    }

    // Getters (Property)
    public IntegerProperty spIdProperty() { return spId; }
    public Integer getSpId() { return spId.get(); }

    public StringProperty namaPerusahaanProperty() { return namaPerusahaan; }
    public String getNamaPerusahaan() { return namaPerusahaan.get(); }

    public StringProperty adminIdProperty() { return adminId; }
    public String getAdminId() { return adminId.get(); }

    public IntegerProperty spNumberProperty() { return spNumber; }
    public Integer getSpNumber() { return spNumber.get(); }

    public DoubleProperty totalHargaProperty() { return totalHarga; }
    public Double getTotalHarga() { return totalHarga.get(); }

    public StringProperty namaPekerjaanProperty() { return namaPekerjaan; }
    public String getNamaPekerjaan() { return namaPekerjaan.get(); }

    public StringProperty tanggalProperty() { return tanggal; }
    public String getTanggal() { return tanggal.get(); }
}