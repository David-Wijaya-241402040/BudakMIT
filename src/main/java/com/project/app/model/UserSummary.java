package main.java.com.project.app.model;

public class UserSummary {
    private int id;
    private String nickname;
    private int totalSP;
    private int totalPending;
    private int totalLunas;
    private int totalBatal;

    public UserSummary() {}

    public UserSummary(int id, String nickname, int totalSP, int totalPending, int totalLunas, int totalBatal) {
        this.id = id;
        this.nickname = nickname;
        this.totalSP = totalSP;
        this.totalPending = totalPending;
        this.totalLunas = totalLunas;
        this.totalBatal = totalBatal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTotalSP() {
        return totalSP;
    }

    public void setTotalSP(int totalSP) {
        this.totalSP = totalSP;
    }

    public int getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(int totalPending) {
        this.totalPending = totalPending;
    }

    public int getTotalLunas() {
        return totalLunas;
    }

    public void setTotalLunas(int totalLunas) {
        this.totalLunas = totalLunas;
    }

    public int getTotalBatal() {
        return totalBatal;
    }

    public void setTotalBatal(int totalBatal) {
        this.totalBatal = totalBatal;
    }
}
