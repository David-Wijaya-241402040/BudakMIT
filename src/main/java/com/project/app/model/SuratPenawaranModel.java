package main.java.com.project.app.model;

import java.time.LocalDate;

public class SuratPenawaranModel {
    private int spId;
    private String noSP;
    private int userId;
    private int companyId;
    private String perihal;
    private LocalDate tanggalSuratPenawaran;

    public SuratPenawaranModel(String noSP, int userId, int companyId, String perihal, LocalDate tanggalSuratPenawaran) {
        this.noSP = noSP;
        this.userId = userId;
        this.companyId = companyId;
        this.perihal = perihal;
        this.tanggalSuratPenawaran = tanggalSuratPenawaran;
    }

    public int getSpId() { return spId; }
    public String getNoSP() { return noSP; }
    public int getUserId() { return userId; }
    public int getCompanyId() { return companyId; }
    public String getPerihal() { return perihal; }
    public LocalDate getTanggalSuratPenawaran() { return tanggalSuratPenawaran; }
}
