package main.java.com.project.app.model;

public class StatistikDokumen {
    private String namaDokumen;
    private int total;

    public StatistikDokumen(String namaDokumen, int total) {
        this.namaDokumen = namaDokumen;
        this.total = total;
    }

    public String getNamaDokumen() { return namaDokumen; }
    public int getTotal() { return total; }
}
