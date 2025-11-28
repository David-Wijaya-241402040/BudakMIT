package main.java.com.project.app.model;

import java.sql.Date;
import java.sql.Timestamp;

public class TagihanModel {
    private String noTagihan;
    private int spId;
    private Date tanggal;
    private double totalHarga;
    private String status;

    public TagihanModel(String noTagihan, int spId, Date tanggal, double totalHarga, String status) {
        this.noTagihan = noTagihan;
        this.spId = spId;
        this.tanggal = tanggal;
        this.totalHarga = totalHarga;
        this.status = status;
    }

    public String getNoTagihan() { return noTagihan; }
    public int getSpId() { return spId; }
    public Date getTanggal() { return tanggal; }
    public double getTotalHarga() { return totalHarga; }
    public String getStatus() { return status; }

    public String getTotalHargaRupiah() {
        return "Rp " + String.format("%,.0f", totalHarga).replace(",", ".");
    }
}


