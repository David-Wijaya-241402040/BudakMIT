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
            System.out.println("Role detected: " + role);

            if(role.equals("owner")) {
                btnManageAccount.setVisible(true);
                btnManageAccount.setManaged(true);

                btnLogoutOwner.setVisible(true);
                btnLogoutOwner.setManaged(true);

                btnLogoutAdmin.setVisible(false);
                btnLogoutAdmin.setManaged(false);
            } else {
                btnManageAccount.setVisible(false);
                btnManageAccount.setManaged(false);

                btnLogoutOwner.setVisible(false);
                btnLogoutOwner.setManaged(false);

                btnLogoutAdmin.setVisible(true);
                btnLogoutAdmin.setManaged(true);
            }

            System.out.println("Manage visible: " + btnManageAccount.isVisible());
            System.out.println("Manage managed: " + btnManageAccount.isManaged());
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
