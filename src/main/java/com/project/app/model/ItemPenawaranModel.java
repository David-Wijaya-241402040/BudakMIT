package main.java.com.project.app.model;

public class ItemPenawaranModel {
    private String namaItem;
    private Integer qty;
    private Double harga;

    public ItemPenawaranModel(String namaItem, Integer qty, Double harga) {
        this.namaItem = namaItem;
        this.qty = qty;
        this.harga = harga;
    }

    public String getNamaItem() { return namaItem; }
    public void setNamaItem(String namaItem) { this.namaItem = namaItem; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Double getHarga() { return harga; }
    public void setHarga(Double harga) { this.harga = harga; }
}