package main.java.com.project.app.model;

public class AddCompanyModel {
    private int companyId;
    private String namaPerusahaan;
    private String alamatPerusahaan;
    private String noTelpPerusahaan;
    private String emailPerusahaan;

    public AddCompanyModel(String namaPerusahaan, String alamatPerusahaan, String noTelpPerusahaan, String emailPerusahaan) {
        this.namaPerusahaan = namaPerusahaan;
        this.alamatPerusahaan = alamatPerusahaan;
        this.noTelpPerusahaan = noTelpPerusahaan;
        this.emailPerusahaan = emailPerusahaan;
    }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public String getNamaPerusahaan() { return namaPerusahaan; }
    public String getAlamatPerusahaan() { return alamatPerusahaan; }
    public String getNoTelpPerusahaan() { return noTelpPerusahaan; }
    public String getEmailPerusahaan() { return emailPerusahaan; }
}
