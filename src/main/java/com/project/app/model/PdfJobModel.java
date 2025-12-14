package main.java.com.project.app.model;

import java.util.List;

public class PdfJobModel {
    private String namaPekerjaan;
    private List<PdfItemModel> items;

    public String getNamaPekerjaan() { return namaPekerjaan; }
    public void setNamaPekerjaan(String namaPekerjaan) { this.namaPekerjaan = namaPekerjaan; }

    public List<PdfItemModel> getItems() { return items; }
    public void setItems(List<PdfItemModel> items) { this.items = items; }
}
