package main.java.com.project.app.model;

public class SparepartModel {

    private int componentId;
    private String namaComponent;
    private String namaSatuan;
    private int qty;
    private Double hargaLama;
    private Double hargaBaru;
    private String tanggalBerlakuBaru;

    public SparepartModel(int componentId, String namaComponent, String namaSatuan, int qty,
                             Double hargaLama, Double hargaBaru, String tanggalBerlakuBaru) {
        this.componentId = componentId;
        this.namaComponent = namaComponent;
        this.namaSatuan = namaSatuan;
        this.qty = qty;
        this.hargaLama = hargaLama;
        this.hargaBaru = hargaBaru;
        this.tanggalBerlakuBaru = tanggalBerlakuBaru;
    }

    public int getComponentId() { return componentId; }
    public String getNamaComponent() { return namaComponent; }
    public String getNamaSatuan() { return namaSatuan; }
    public int getQty() { return qty; }
    public Double getHargaLama() { return hargaLama; }
    public Double getHargaBaru() { return hargaBaru; }
    public String getTanggalBerlakuBaru() { return tanggalBerlakuBaru; }
}
