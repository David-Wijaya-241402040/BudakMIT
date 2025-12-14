package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.UserDAO;

<<<<<<< HEAD
=======
import java.security.MessageDigest;
import java.util.Base64;
import java.security.SecureRandom;

>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
public class CreateAccountPopupController implements MainInjectable {
    @FXML private AnchorPane popupRoot;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField noTelpField;
    @FXML private TextField passwordField;
    @FXML private Button btnAdd;
    @FXML private Button btnBack;

    private MainController mainController;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        // Tombol Add
        btnAdd.setOnAction(e -> addAccount());

        // Tombol Back
        btnBack.setOnAction(e -> closePopup());
    }

    private void addAccount() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String noTelp = noTelpField.getText().trim();
        String password = passwordField.getText().trim();

        if(username.isEmpty() || email.isEmpty() || noTelp.isEmpty() || password.isEmpty()) {
            System.out.println("Semua field harus diisi!");
            return;
        }

        // Panggil DAO
        UserDAO userDAO = new UserDAO(DBConnection.getConnection());
<<<<<<< HEAD
        boolean success = userDAO.addUser(username, email, noTelp, password);
=======
        String salt = userDAO.callSalt();
        boolean success = userDAO.addUser(username, email, noTelp, password, salt);
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116

        if(success) {
            System.out.println("User berhasil ditambahkan!");
            closePopup();

            // Refresh halaman Manage Account
            if(mainController != null) {
                mainController.loadPage("manageaccount");
            }
        } else {
            System.out.println("Gagal menambahkan user!");
        }
    }

    public void closePopup() {
        Parent parent = popupRoot.getParent();
        if(parent != null) {
            ((Pane) parent).getChildren().remove(popupRoot);
        }
    }
}
