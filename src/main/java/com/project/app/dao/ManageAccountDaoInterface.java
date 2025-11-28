package main.java.com.project.app.dao;

import main.java.com.project.app.model.UserSummaryModel;

import java.util.List;

public interface ManageAccountDaoInterface {
    List<UserSummaryModel> getAllUsers();
    List<UserSummaryModel> searchUsers(String category, String keyword);
}
