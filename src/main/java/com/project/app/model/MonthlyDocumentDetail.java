package main.java.com.project.app.model;

public class MonthlyDocumentDetail {
    private String name; // SP / Pending / Lunas / Batal
    private int total;

    public MonthlyDocumentDetail() {}

    public MonthlyDocumentDetail(String name, int total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
