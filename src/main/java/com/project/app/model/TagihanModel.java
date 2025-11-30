package main.java.com.project.app.model;

import java.time.LocalDate;

public class TagihanModel {
    private String noTag;
    private String noSP;
    private String perusahaan;
    private LocalDate tanggal;
    private double total;
    private String status;

    public TagihanModel(String noTag, String noSP, String perusahaan, LocalDate tanggal, double total, String status) {
        this.noTag = noTag;
        this.noSP = noSP;
        this.perusahaan = perusahaan;
        this.tanggal = tanggal;
        this.total = total;
        this.status = status;
    }

    public String getNoTag() { return noTag; }
    public void setNoTag(String noTag) { this.noTag = noTag; }

    public String getNoSP() { return noSP; }
    public void setNoSP(String noSP) { this.noSP = noSP; }

    public String getPerusahaan() { return perusahaan; }
    public void setPerusahaan(String perusahaan) { this.perusahaan = perusahaan; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
