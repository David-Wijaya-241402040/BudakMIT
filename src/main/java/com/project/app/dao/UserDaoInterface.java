package main.java.com.project.app.dao;

import main.java.com.project.app.model.UserModel;

public interface UserDaoInterface {
    UserModel login(String email, String password);
}