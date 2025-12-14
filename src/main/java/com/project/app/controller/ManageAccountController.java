package main.java.com.project.app.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.com.project.app.dao.ManageAccountDAO;
import main.java.com.project.app.model.UserSummaryModel;

import java.io.IOException;

public class ManageAccountController implements MainInjectable {
    private MainController mainController;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchCategories;
    @FXML private Button btnSearch;
    @FXML private Button btnAddAccount;
    @FXML private Button btnRefresh;

    @FXML private TableView<UserSummaryModel> userTable;
    @FXML private TableColumn<UserSummaryModel, Integer> userIdColumn;
    @FXML private TableColumn<UserSummaryModel, String> usernameColumn;
    @FXML private TableColumn<UserSummaryModel, String> emailColumn;
    @FXML private TableColumn<UserSummaryModel, String> noTelpColumn;
    @FXML private TableColumn<UserSummaryModel, Integer> totalSuratColumn;
    @FXML private TableColumn<UserSummaryModel, String> statusColumn;

    private ManageAccountDAO dao = new ManageAccountDAO();

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {

        // ----- Setup ComboBox Category -----
        searchCategories.setItems(FXCollections.observableArrayList(
                "nickname",
                "email",
                "phone_number",
                "status"
        ));
        searchCategories.getSelectionModel().selectFirst();

        // ----- Setup Table Columns -----
        userIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getUserId()).asObject());
        usernameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNickname()));
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        noTelpColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNoTelp()));
        totalSuratColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTotalSurat()).asObject());
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        // Load data pertama kali
        loadAllUsers();
        setupContextMenu();
    }

    private void loadAllUsers() {
        ObservableList<UserSummaryModel> list =
                FXCollections.observableArrayList(dao.getAllUsers());
        userTable.setItems(list);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        String category = searchCategories.getValue();

        if (keyword.isEmpty()) {
            loadAllUsers();
            return;
        }

        ObservableList<UserSummaryModel> list =
                FXCollections.observableArrayList(dao.searchUsers(category, keyword));

        userTable.setItems(list);
    }

    @FXML
    private void goAddPopup() {
        mainController.handleAddAccount();
    }

    @FXML private void goRefresh() {
        loadAllUsers();
    }

    private void setupContextMenu() {
        userTable.setRowFactory(tv -> {
            TableRow<UserSummaryModel> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteItem = new MenuItem("Delete Account");
            deleteItem.setOnAction(event -> {
                UserSummaryModel selectedUser = row.getItem();
                boolean success = dao.deleteUser(selectedUser.getUserId());
                if (success) {
                    loadAllUsers();
                    showAlert("INFO", "User berhasil dihapus!");
                } else {
                    showAlert("WARNING", "Gagal menghapus user!");
                }
            });

            MenuItem toggleActiveItem = new MenuItem();
            toggleActiveItem.setOnAction(event -> {
                UserSummaryModel selectedUser = row.getItem();
                String newStatus = selectedUser.getStatus().equalsIgnoreCase("active") ? "inactive" : "active";
                boolean success = dao.updateUserStatus(selectedUser.getUserId(), newStatus);
                if (success) {
                    loadAllUsers();
                    showAlert("INFO", "Status user berhasil diubah!");
                } else {
                    showAlert("WARNING", "Gagal mengubah status user!");
                }
            });

            row.itemProperty().addListener((obs, oldUser, newUser) -> {
                if (newUser == null) {
                    row.setContextMenu(null);
                } else {
                    toggleActiveItem.setText(newUser.getStatus().equalsIgnoreCase("active") ? "Set Inactive" : "Set Active");
                    contextMenu.getItems().setAll(deleteItem, toggleActiveItem);
                    row.setContextMenu(contextMenu);
                }
            });

            return row;
        });
    }

    void showAlert(String title, String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
            alert.setTitle(title);
            alert.showAndWait();
        });
    }

}
