package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LogoutPopupController implements MainInjectable {
    @FXML AnchorPane popupRoot;
    @FXML Button btnLogout;

    private MainController mainController;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
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

    @FXML
    private void confirmLogout() {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        closePopup();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/layout/login.fxml"));
            Parent root = loader.load();


            // Ganti scene ke login
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}