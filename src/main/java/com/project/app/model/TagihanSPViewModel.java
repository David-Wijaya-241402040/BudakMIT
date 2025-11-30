package main.java.com.project.app.model;

public class TagihanSPViewModel {
    private int spId;
    private String noSp;
    private String namaPerusahaan;
    private String tanggal; // tampilkan sebagai string

    public TagihanSPViewModel(int spId, String noSp, String namaPerusahaan, String tanggal) {
        this.spId = spId;
        this.noSp = noSp;
        this.namaPerusahaan = namaPerusahaan;
        this.tanggal = tanggal;
    }

    public int getSpId() { return spId; }
    public String getNoSp() { return noSp; }
    public String getNamaPerusahaan() { return namaPerusahaan; }
    public String getTanggal() { return tanggal; }
}
