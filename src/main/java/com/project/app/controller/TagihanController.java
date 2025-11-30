package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.project.app.Main;
import main.java.com.project.app.dao.TagihanDAO;
import main.java.com.project.app.model.SparepartModel;
import main.java.com.project.app.model.TagihanModel;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TagihanController implements Initializable, MainInjectable {
    private MainController mainController;

    @FXML
    private Button btnSearch, btnRefresh, btnAdd, btnUpdate;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryBox;
    @FXML private TableView<TagihanModel> tableTagihan;

    @FXML private TableColumn<TagihanModel, String> colNoTag, colNoSP, colPerusahaan, colStatus;
    @FXML private TableColumn<TagihanModel, LocalDate> colTanggal;
    @FXML private TableColumn<TagihanModel, Double> colTotal;

    private TagihanDAO dao = new TagihanDAO();

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        setupCategory();
        loadData();

        btnUpdate.setDisable(true);
        tableTagihan.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnUpdate.setDisable(newVal == null);
        });
    }

    private void setupTable() {
        colNoTag.setCellValueFactory(new PropertyValueFactory<>("noTag"));
        colNoSP.setCellValueFactory(new PropertyValueFactory<>("noSP"));
        colPerusahaan.setCellValueFactory(new PropertyValueFactory<>("perusahaan"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colTanggal.setCellFactory(column -> new TableCell<TagihanModel, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null); // JANGAN TAMPILKAN "-"
                    return;
                }

                setText(item == null ? "-" : item.toString());
            }
        });

        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupCategory() {
        categoryBox.getItems().addAll("No Tagihan", "No Surat", "Perusahaan");
        categoryBox.getSelectionModel().selectFirst();
    }

    public void loadData() {
        tableTagihan.setItems(dao.getAllTagihan());
    }

    @FXML
    private void onSearch() {
        String value = searchField.getText().trim();
        String category = categoryBox.getValue();

        if (value.isEmpty()) {
            loadData();
            return;
        }

        tableTagihan.setItems(dao.searchTagihan(value, category));
    }

    @FXML
    private void onRefresh() {
        loadData();
        searchField.clear();
    }

    @FXML
    public void goUpdate() {
        TagihanModel selected = tableTagihan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        int spId = selected.getSpId();
        mainController.handleManageTagihan("Update", selected);
    }

    @FXML
    public void goAdd() {
        mainController.handleManageTagihan("Add", null);
    }
}
