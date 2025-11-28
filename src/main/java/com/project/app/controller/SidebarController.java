package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.java.com.project.app.session.Session;

public class SidebarController {
    private MainController mainController;
    @FXML Button btnLogoutAdmin;
    @FXML Button btnManageAccount;
    @FXML Button btnLogoutOwner;

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    @FXML private void initialize() {
        String role = Session.currentUser.getRoles();
        if(role.equals("owner")) {
            btnLogoutAdmin.setVisible(false);
            btnLogoutOwner.setVisible(true);
            btnManageAccount.setVisible(true);
        } else {
            btnLogoutAdmin.setVisible(true);
            btnLogoutOwner.setVisible(false);
            btnManageAccount.setVisible(false);
        }
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
