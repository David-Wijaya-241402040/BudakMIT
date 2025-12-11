package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.UserDAO;
import main.java.com.project.app.session.Session;
import main.java.com.project.app.utils.PasswordUtils;

import java.io.IOException;

public class ChangePassController {

    @FXML private PasswordField curPassField;
    @FXML private PasswordField newPassField;
    @FXML private PasswordField conPassField;

    private UserDAO dao;

    @FXML
    public void initialize() {
        this.dao = new UserDAO(DBConnection.getConnection());
    }

    @FXML
    private void goSubmit() {
        String curPasswordInput = curPassField.getText();
        String newPassword = newPassField.getText();
        String confirmPassword = conPassField.getText();

        // 1️⃣ cek new password cocok
        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Password not matched", "Confirm Password berbeda dari New Password");
            return;
        }

        // 2️⃣ cek password lama
        String storedHash = Session.currentUser.getPassword();
        String salt = Session.currentUser.getSalt(); // pastikan ada method getSalt()

        if (!PasswordUtils.checkPassword(curPasswordInput, storedHash, salt)) {
            showAlert(Alert.AlertType.WARNING, "Password Salah", "Password lama anda salah!");
            return;
        }

        // 3️⃣ update password baru
        String newSalt = PasswordUtils.generateSalt();
        String newHashed = PasswordUtils.hashWithSalt(newPassword, newSalt);
        dao.updatePassword(Session.currentUser.getId(), newHashed, newSalt);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Password berhasil diubah. Silakan login kembali.");

        // 4️⃣ logout otomatis ke halaman login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/layout/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) curPassField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/project/app/fxml/layout/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) curPassField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
