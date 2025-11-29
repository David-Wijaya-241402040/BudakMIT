package main.java.com.project.app.dao;

import main.java.com.project.app.model.UserSummaryModel;

import java.util.List;

public interface ManageAccountDAOInterface {
    List<UserSummaryModel> getAllUsers();
    List<UserSummaryModel> searchUsers(String category, String keyword);
}
