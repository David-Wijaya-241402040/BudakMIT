package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.SparepartDAO;
import main.java.com.project.app.model.SparepartModel;

import java.io.IOException;

public class DeleteSparepartPopupController implements MainInjectable {
    @FXML AnchorPane popupRoot;
    @FXML Button btnDelete;

    private MainController mainController;
    private SparepartModel selectedSparepart;
    private SparepartController sparepartController;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setSparepartController(SparepartController controller) {
        this.sparepartController = controller;
    }

    public void setSparepartData(SparepartModel sparepart) {
        this.selectedSparepart = sparepart;
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
    private void confirmDelete() {
        if (selectedSparepart == null) return;

        try {
            int id = selectedSparepart.getComponentId();

            SparepartDAO dao = new SparepartDAO(DBConnection.getConnection());

            boolean success = dao.deleteSparepart(id); // method delete di DAO

            if (success) {
                sparepartController.loadData(); // refresh tabel
                closePopup(); // tutup popup
            } else {
                mainController.showAlert("ERROR", "Gagal menghapus sparepart!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mainController.showAlert("ERROR", "Terjadi kesalahan saat menghapus sparepart.");
        }
    }

}

