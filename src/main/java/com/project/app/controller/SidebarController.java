package main.java.com.project.app.controller;

import javafx.fxml.FXML;
<<<<<<< HEAD
import javafx.scene.control.Button;
import main.java.com.project.app.session.Session;

=======
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import main.java.com.project.app.session.Session;

import java.io.IOException;

>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
public class SidebarController {
    private MainController mainController;
    @FXML Button btnLogoutAdmin;
    @FXML Button btnManageAccount;
    @FXML Button btnLogoutOwner;
<<<<<<< HEAD
=======
    @FXML Hyperlink goChangePass;
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116

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
<<<<<<< HEAD
    }

    @FXML private void goHome() {
        mainController.loadPage("home");
=======

        goChangePass.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/layout/changepassword.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) goChangePass.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    @FXML private void goHome() {
        if (Session.currentUser != null) {
            String role = Session.currentUser.getRoles();

            if (role.equals("owner")) {
                mainController.loadPage("home");
            } else if (role.equals("staff")) {
                mainController.loadPage("homestaff");
            }
        }
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
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
