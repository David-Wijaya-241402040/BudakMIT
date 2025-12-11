package main.java.com.project.app.model;

public class UserModel {
    private final int id;
    private String nickname;
    private final String email;
    private final String password;
    private String salt;
    private String roles;

    public UserModel(int id, String nickname, String email, String password, String salt, String roles) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.roles = roles;
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
    public String getSalt() { return salt; };
    public String getRoles() {
        return roles;
    }
}