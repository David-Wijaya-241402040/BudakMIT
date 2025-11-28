package main.java.com.project.app.model;

public class UserSummaryModel {
    private int userId;
    private String nickname;
    private String email;
    private String noTelp;
    private String status;
    private int totalSurat;

    public UserSummaryModel(int userId, String nickname, String email, String noTelp, String status, int totalSurat) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.noTelp = noTelp;
        this.status = status;
        this.totalSurat = totalSurat;
    }

    public int getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public String getEmail() { return email; }
    public String getNoTelp() { return noTelp; }
    public String getStatus() { return status; }
    public int getTotalSurat() { return totalSurat; }
}
