package main.java.com.project.app.model;

public class UserModel {
    private final int id;
    private String nickname;
    private final String email;
    private final String password;
    private String roles;

    public UserModel(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }


    public int getId() {
        return id;
    }
    public String getNickname(){
        return nickname;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword() {
        return password;
    }
}
