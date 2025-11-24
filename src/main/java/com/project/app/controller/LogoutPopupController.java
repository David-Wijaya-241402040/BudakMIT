package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class LogoutPopupController {
    @FXML AnchorPane popupRoot;

    private MainController mainController;

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    public void closePopup() {
        Parent parent = popupRoot.getParent();
        if (parent != null) {
            ((Pane) parent).getChildren().remove(popupRoot);
        }
    }

    @FXML private void initialize() { }

    @FXML private void cancel() {
        closePopup();
    }

    @FXML private void confirmLogout() {
        closePopup();
        mainController.loadPage("login");
    }
}

