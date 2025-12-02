package main.java.com.project.app.model;

public class CompanyModel {
    private int companyId;
    private String namaPerusahaan;
    private String noTelpPerusahaan;
    private String emailPerusahaan;

    public CompanyModel(int companyId, String namaPerusahaan, String noTelpPerusahaan, String emailPerusahaan) {
        this.companyId = companyId;
        this.namaPerusahaan = namaPerusahaan;
        this.noTelpPerusahaan = noTelpPerusahaan;
        this.emailPerusahaan = emailPerusahaan;
    }

    public int getCompanyId() { return companyId; }
    public String getNamaPerusahaan() { return namaPerusahaan; }
    public String getNoTelpPerusahaan() { return noTelpPerusahaan; }
    public String getEmailPerusahaan() { return emailPerusahaan; }
}
