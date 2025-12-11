package main.java.com.project.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.CompanyDAO;
import main.java.com.project.app.dao.SuratPenawaranDAO;
import main.java.com.project.app.model.CompanyModel;
import main.java.com.project.app.model.SuratPenawaranModel;
import main.java.com.project.app.session.Session;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CreatePenawaranPopupController implements MainInjectable {
    @FXML private AnchorPane popupRoot;

    @FXML private Button btnBack;
    @FXML private Button btnSearch;
    @FXML private Button btnAdd;

    @FXML private TextField noSPField;
    @FXML private TextField idCompanyField;
    @FXML private TextField searchField;

    @FXML private TextArea perihalArea;
    @FXML private DatePicker dateSP;

    @FXML private TableView<CompanyModel> tableCompany;
    @FXML private TableColumn<CompanyModel, Integer> colIdCompany;
    @FXML private TableColumn<CompanyModel, String> colNamaCompany;
    @FXML private TableColumn<CompanyModel, String> colTelpCompany;
    @FXML private TableColumn<CompanyModel, String> colEmailCompany;

    private Connection conn;
    private CompanyDAO companyDAO;
    private SuratPenawaranDAO spDAO;
    private ObservableList<CompanyModel> companyList = FXCollections.observableArrayList();
    private MainController mainController;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        conn = DBConnection.getConnection();
        companyDAO = new CompanyDAO(conn);
        spDAO = new SuratPenawaranDAO(conn);

        // Setup column
        colIdCompany.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        colNamaCompany.setCellValueFactory(new PropertyValueFactory<>("namaPerusahaan"));
        colTelpCompany.setCellValueFactory(new PropertyValueFactory<>("noTelpPerusahaan"));
        colEmailCompany.setCellValueFactory(new PropertyValueFactory<>("emailPerusahaan"));

        loadCompanies();
    }

    public void loadCompanies() {
        resetCellFactories();
        List<CompanyModel> companies = companyDAO.getAllCompanies();
        companyList.setAll(companies);
        tableCompany.setItems(companyList);
    }

    @FXML
    private void searchCompany(ActionEvent event) {
        String keyword = searchField.getText().trim();
        List<CompanyModel> results = companyDAO.searchCompanyByName(keyword);

        if (results.isEmpty()) {
            // Kosong, tampilkan pesan di table
            tableCompany.setItems(FXCollections.observableArrayList());
            tableCompany.getItems().add(new CompanyModel(0,
                    "Perusahaan tidak ditemukan, apakah anda ingin",
                    "", ""));
            tableCompany.getItems().add(new CompanyModel(0,
                    "menambahkan perusahaan sebagai customer baru?",
                    "", ""));
            colNamaCompany.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Hyperlink link = new Hyperlink(item);
                        link.setOnAction(e -> {
                            mainController.handleAddCompany();
                        });
                        setGraphic(link);
                    }
                }
            });
        } else {
            tableCompany.setItems(FXCollections.observableArrayList(results));
            resetCellFactories();
        }
    }

    @FXML
    private void selectCompany(MouseEvent event) {
        CompanyModel selected = tableCompany.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getCompanyId() != 0) {
            idCompanyField.setText(String.valueOf(selected.getCompanyId()));
        }
    }

    @FXML
    private void addSuratPenawaran(ActionEvent event) {
        String noSP = noSPField.getText();
        String perihal = perihalArea.getText();
        LocalDate tanggal = dateSP.getValue();
        int companyId = Integer.parseInt(idCompanyField.getText());
        int userId = Session.currentUser.getId();

        SuratPenawaranModel sp = new SuratPenawaranModel(noSP, userId, companyId, perihal, tanggal);
        boolean success = spDAO.insertSuratPenawaran(sp);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Surat Penawaran berhasil ditambahkan!");
            alert.showAndWait();

            goBack();

            noSPField.clear();
            idCompanyField.clear();
            perihalArea.clear();
            dateSP.setValue(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal menambahkan Surat Penawaran!");
            alert.showAndWait();

            goBack();
        }
    }

    public void goBack() {
        Parent parent = popupRoot.getParent();
        if(parent != null) {
            ((Pane) parent).getChildren().remove(popupRoot);
        }
    }

    private void resetCellFactories() {
        colIdCompany.setCellFactory(tc -> new TableCell<CompanyModel, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        colNamaCompany.setCellFactory(tc -> new TableCell<CompanyModel, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });

        colTelpCompany.setCellFactory(tc -> new TableCell<CompanyModel, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });

        colEmailCompany.setCellFactory(tc -> new TableCell<CompanyModel, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });
    }


}
