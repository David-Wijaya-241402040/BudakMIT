package main.java.com.project.app.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import main.java.com.project.app.model.SparepartModel;
import main.java.com.project.app.model.TagihanModel;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.sql.Connection;

public class MainController implements SharedControllerProvider {
    @FXML private AnchorPane contentArea;
    @FXML private SidebarController sidebarController;
    @FXML private StackPane rootPane;
    private SparepartController sparepartController;
    private TagihanController tagihanController;
    private AddNewPenawaranController addNewPenawaranController;

    @FXML public void initialize() {
        sidebarController.setMainController(this);
        loadPage("home");
    }

    @Override
    public void setSparepartController(SparepartController controller) {
        this.sparepartController = controller;
    }

    @Override
    public SparepartController getSparepartController() {
        return sparepartController;
    }

    @Override
    public void setTagihanController(TagihanController controller) {
        this.tagihanController = controller;
    }

    @Override
    public TagihanController getTagihanController() {
        return tagihanController;
    }


    private int spIdBuffer = 0;

    public void setSPIdBuffer(int spId) {
        this.spIdBuffer = spId;
    }

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

            if (controller instanceof SparepartController) {
                this.setSparepartController((SparepartController) controller);
            } else if (controller instanceof TagihanController) {
                this.setTagihanController((TagihanController) controller);
            } else if (controller instanceof AddNewPenawaranController) {
                ((AddNewPenawaranController) controller).setMainController(this);
                ((AddNewPenawaranController) controller).showDetailPenawaran(spIdBuffer);
            } else if (controller instanceof AddDetailPenawaranController) {
                    ((AddDetailPenawaranController) controller).setMainController(this);
                    ((AddDetailPenawaranController) controller).showDetailPenawaran(spIdBuffer);
                    System.out.println("✅ Passing spIdBuffer to AddDetailPenawaranController: " + spIdBuffer);
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

    public void handleManageSparepart(String actions, SparepartModel selectedSparepart) {
        try {
            FXMLLoader loader = null;
            if(actions.equals("Add") || actions.equals("Update")) {
                loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/popup/createsparepart_popup.fxml"));
            } else if (actions.equals("Delete")) {
                loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/popup/deletesparepart_popup.fxml"));
            }

            if(loader == null) {
                showAlert("WARNING", "Loader is null, you can't continue forward!");
                return;
            }

            AnchorPane popup = loader.load();

            if(actions.equals("Add") || actions.equals("Update")) {
                CreateSparepartPopupController popupController = loader.getController();

                popupController.setAction(actions);
                popupController.setMainController(this);
                popupController.setSparepartController(this.getSparepartController());

                if (actions.equalsIgnoreCase("Update") && selectedSparepart != null) {
                    popupController.setSparepartData(selectedSparepart);
                }
            } else if(actions.equals("Delete")) {
                DeleteSparepartPopupController popupController = loader.getController();

                popupController.setMainController(this);
                popupController.setSparepartController(this.getSparepartController());
                popupController.setSparepartData(selectedSparepart);
            }


            rootPane.getChildren().add(popup);

            AnchorPane.setTopAnchor(popup, 0.0);
            AnchorPane.setBottomAnchor(popup, 0.0);
            AnchorPane.setLeftAnchor(popup, 0.0);
            AnchorPane.setRightAnchor(popup, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleManageTagihan(String actions, TagihanModel selectedTagihan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/popup/createtagihan_popup.fxml"));

            AnchorPane popup = loader.load();

            CreateTagihanPopupController popupController = loader.getController();

            popupController.setAction(actions);
            popupController.setMainController(this);
            popupController.setTagihanController(this.getTagihanController());

            if (actions.equalsIgnoreCase("Update") && selectedTagihan != null) {
                popupController.setTagihanData(selectedTagihan);
            }

            rootPane.getChildren().add(popup);

            AnchorPane.setTopAnchor(popup, 0.0);
            AnchorPane.setBottomAnchor(popup, 0.0);
            AnchorPane.setLeftAnchor(popup, 0.0);
            AnchorPane.setRightAnchor(popup, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    void showAlert(String title, String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
            alert.setTitle(title);
            alert.showAndWait();
        });
    }



}
