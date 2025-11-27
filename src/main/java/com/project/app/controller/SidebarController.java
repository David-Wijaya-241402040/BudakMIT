package main.java.com.project.app.controller;

import javafx.fxml.FXML;

public class SidebarController {
    private MainController mainController;

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    @FXML private void goHome() {
        mainController.loadPage("home");
    }

    @FXML private void goPenawaran() {
        mainController.loadPage("penawaran");
    }

    @FXML private void goTagihan() {
        mainController.loadPage("tagihan");
    }

    @FXML private void goSparepart() {
        mainController.loadPage("sparepart");
    }

    @FXML private void goManageAccount() {
        mainController.loadPage("manageaccount");
    }

    @FXML private void goLogout() {
        mainController.handleLogout();
    }
}
