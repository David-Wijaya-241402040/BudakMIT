package main.java.com.project.app.controller;

import javafx.fxml.FXML;

public class HomeStaffController implements MainInjectable {
    private MainController mainController;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleAddPenawaran() {
        mainController.handleAddPenawaran();

    }
    @FXML
    private void handleAddTagihan() {
        mainController.handleManageTagihan("Add", null);
    }
}
