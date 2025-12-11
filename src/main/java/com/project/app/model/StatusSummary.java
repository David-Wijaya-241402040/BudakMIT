package main.java.com.project.app.model;

public class StatusSummary {
    private int pending;
    private int telat;
    private int lunas;
    private int batal;

    public StatusSummary() {}

    public StatusSummary(int pending, int telat, int lunas, int batal) {
        this.pending = pending;
        this.telat = telat;
        this.lunas = lunas;
        this.batal = batal;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getTelat() {
        return telat;
    }

    public void setTelat(int telat) {
        this.telat = telat;
    }

    public int getLunas() {
        return lunas;
    }

    public void setLunas(int lunas) {
        this.lunas = lunas;
    }

    public int getBatal() {
        return batal;
    }

    public void setBatal(int batal) {
        this.batal = batal;
    }
}
