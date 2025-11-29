package main.java.com.project.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.SparepartDAO;
import main.java.com.project.app.model.SparepartModel;

public class SparepartController implements MainInjectable {
    private MainController mainController;

    @FXML private Button btnRefresh;
    @FXML private Button btnUpdate;
    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private Button btnSearch;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryBox;

    @FXML private TableView<SparepartModel> tableKomponen;
    @FXML private TableColumn<SparepartModel, Integer> colID;
    @FXML private TableColumn<SparepartModel, String> colNamaKomponen;
    @FXML private TableColumn<SparepartModel, String> colSatuan;
    @FXML private TableColumn<SparepartModel, Integer> colQty;
    @FXML private TableColumn<SparepartModel, Double> colHALama;
    @FXML private TableColumn<SparepartModel, Double> colHABaru;
    @FXML private TableColumn<SparepartModel, String> colTglBerlaku;

    ObservableList<SparepartModel> data = FXCollections.observableArrayList();
    SparepartDAO dao;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        dao = new SparepartDAO(DBConnection.getConnection());

        colID.setCellValueFactory(v -> new javafx.beans.property.SimpleIntegerProperty(v.getValue().getComponentId()).asObject());
        colNamaKomponen.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getNamaComponent()));
        colSatuan.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getNamaSatuan()));
        colQty.setCellValueFactory(v -> new javafx.beans.property.SimpleIntegerProperty(v.getValue().getQty()).asObject());
        colHALama.setCellValueFactory(v -> new javafx.beans.property.SimpleDoubleProperty(v.getValue().getHargaLama()).asObject());
        colHABaru.setCellValueFactory(v -> new javafx.beans.property.SimpleDoubleProperty(v.getValue().getHargaBaru()).asObject());
        colTglBerlaku.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getTanggalBerlakuBaru()));

        categoryBox.setItems(FXCollections.observableArrayList(
                "All",
                "Nama Komponen",
                "Satuan"
        ));
        categoryBox.getSelectionModel().select("All");

        loadData();

        // Update hanya aktif jika ada baris terpilih
        btnUpdate.setDisable(true);
        tableKomponen.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnUpdate.setDisable(newVal == null);
        });
    }

    public void loadData() {
        try {
            data.setAll(dao.getAll());
            tableKomponen.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goRefresh() {
        loadData();
        searchField.clear();
    }

    @FXML
    public void goSearch() {
        try {
            String keyword = searchField.getText();
            String category = categoryBox.getValue();

            if (keyword.isEmpty()) {
                loadData();
                return;
            }

            data.setAll(dao.searchByCategory(keyword, category));
            tableKomponen.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goUpdate() {
        SparepartModel selected = tableKomponen.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        mainController.handleManageSparepart("Update", selected);
    }

    @FXML
    public void goAdd() {
        mainController.handleManageSparepart("Add", null);
    }

    @FXML
    public void goDelete() {

    }
}
