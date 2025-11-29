package main.java.com.project.app.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {
    @FXML private AnchorPane contentArea;
    @FXML private SidebarController sidebarController;
    @FXML private StackPane rootPane;

    @FXML public void initialize() {
        sidebarController.setMainController(this);
        loadPage("home");
    }

//    public void loadPage(String page){
//        try {
//            Node node = FXMLLoader.load(getClass().getResource("/main/resources/com/project/app/fxml/contents/" + page + ".fxml"));
//            contentArea.getChildren().setAll(node);
//
//            AnchorPane.setTopAnchor(node, 0.0);
//            AnchorPane.setBottomAnchor(node, 0.0);
//            AnchorPane.setLeftAnchor(node, 0.0);
//            AnchorPane.setRightAnchor(node, 0.0);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void loadPage(String page){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/main/resources/com/project/app/fxml/contents/" + page + ".fxml"
            ));

            Node node = loader.load();

            Object controller = loader.getController();

            if (controller instanceof MainInjectable) {
                ((MainInjectable) controller).setMainController(this);
            }

            contentArea.getChildren().setAll(node);

            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/popup/logout_popup.fxml"));

            AnchorPane popup = loader.load();

            LogoutPopupController popupController = loader.getController();
            popupController.setMainController(this);

            rootPane.getChildren().add(popup);

            AnchorPane.setTopAnchor(popup, 0.0);
            AnchorPane.setBottomAnchor(popup, 0.0);
            AnchorPane.setLeftAnchor(popup, 0.0);
            AnchorPane.setRightAnchor(popup, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAddAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/popup/createaccount_popup.fxml"));

            AnchorPane popup = loader.load();

            CreateAccountPopupController popupController = loader.getController();
            popupController.setMainController(this);

            rootPane.getChildren().add(popup);

            AnchorPane.setTopAnchor(popup, 0.0);
            AnchorPane.setBottomAnchor(popup, 0.0);
            AnchorPane.setLeftAnchor(popup, 0.0);
            AnchorPane.setRightAnchor(popup, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}