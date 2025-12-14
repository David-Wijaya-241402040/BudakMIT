package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.AddCompanyDAO;
import main.java.com.project.app.model.AddCompanyModel;

import java.sql.Connection;

public class CreatePerusahaanPopupController implements MainInjectable {
    @FXML private AnchorPane popupRoot;

    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField telpField;
    @FXML private TextField emailField;
    @FXML private Button btnSubmit;
    private MainController mainController;


    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private Connection conn;
    private AddCompanyDAO companyDAO;

    public void initialize() {
        conn = DBConnection.getConnection();
        companyDAO = new AddCompanyDAO(conn);
    }

    @FXML
    private void submitCompany() {
        String nama = namaField.getText().trim();
        String alamat = alamatField.getText().trim();
        String telp = telpField.getText().trim();
        String email = emailField.getText().trim();

        if (nama.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nama perusahaan wajib diisi!");
            alert.showAndWait();
            return;
        }

        AddCompanyModel company = new AddCompanyModel(nama, alamat, telp, email);
        boolean success = companyDAO.insertCompany(company);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Perusahaan berhasil ditambahkan!");
            alert.showAndWait();

            // Reset form
            namaField.clear();
            alamatField.clear();
            telpField.clear();
            emailField.clear();

            // panggil callback supaya TableView di popup SP refresh
            if (onCompanyAdded != null) {
                onCompanyAdded.run();
            }

            goBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menambahkan perusahaan!");
            alert.showAndWait();
        }
    }

    public void goBack() {
        Parent parent = popupRoot.getParent();
        if(parent != null) {
            ((Pane) parent).getChildren().remove(popupRoot);
        }
    }

    private Runnable onCompanyAdded;

    public void setOnCompanyAdded(Runnable onCompanyAdded) {
        this.onCompanyAdded = onCompanyAdded;
    }
}

