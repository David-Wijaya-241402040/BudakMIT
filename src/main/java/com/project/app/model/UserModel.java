package main.java.com.project.app.model;

public class UserModel {
    private final int id;
    private String nickname;
    private final String email;
    private final String password;
<<<<<<< HEAD
    private String roles;

    public UserModel(int id, String email, String password, String roles) {
        this.id = id;
        this.email = email;
        this.password = password;
=======
    private String salt;
    private String roles;

    public UserModel(int id, String nickname, String email, String password, String salt, String roles) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.salt = salt;
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
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
<<<<<<< HEAD
=======
    public String getSalt() { return salt; };
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
    public String getRoles() {
        return roles;
    }
}