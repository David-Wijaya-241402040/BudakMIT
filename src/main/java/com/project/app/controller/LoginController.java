package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.UserDAO;
import main.java.com.project.app.model.UserModel;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private UserDAO dao;

    // ✅ HARUS ada dan TANPA parameter
    public LoginController() {
    }

    // ✅ Inisialisasi DAO di method initialize()
    @FXML
    public void initialize() {
        this.dao = new UserDAO(DBConnection.getConnection());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        UserModel user = dao.login(email, password);

        if (user != null) {
            try {
                // Load file FXML halaman selanjutnya
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/contents/home.fxml"));
                Parent root = loader.load();

                // Ambil stage saat ini (window-nya)
                Stage stage = (Stage) emailField.getScene().getWindow();

                // Ganti scene ke halaman baru
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Email atau password salah!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
