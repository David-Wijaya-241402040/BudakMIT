package main.java.com.project.app.model;

import java.time.LocalDate;
import java.util.List;

public class PdfSPModel {
    // Header
    private String noSP;
    private LocalDate tanggal;
    private String perihal;
    private String namaPerusahaan;
    private String alamatPerusahaan;

    // Jobs
    private List<PdfJobModel> jobs;

    // Getter & Setter
    public String getNoSP() { return noSP; }
    public void setNoSP(String noSP) { this.noSP = noSP; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public String getPerihal() { return perihal; }
    public void setPerihal(String perihal) { this.perihal = perihal; }

    public String getNamaPerusahaan() { return namaPerusahaan; }
    public void setNamaPerusahaan(String namaPerusahaan) { this.namaPerusahaan = namaPerusahaan; }

    public String getAlamatPerusahaan() { return alamatPerusahaan; }
    public void setAlamatPerusahaan(String alamatPerusahaan) { this.alamatPerusahaan = alamatPerusahaan; }

    public List<PdfJobModel> getJobs() { return jobs; }
    public void setJobs(List<PdfJobModel> jobs) { this.jobs = jobs; }
}
