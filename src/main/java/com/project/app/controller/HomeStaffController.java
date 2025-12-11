package main.java.com.project.app.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.project.app.dao.StaffDashboardDAO;
import main.java.com.project.app.dao.StaffDashboardDAOImpl;
import main.java.com.project.app.model.LogAktivitas;
import main.java.com.project.app.model.StatistikDokumen;
import main.java.com.project.app.session.Session;

import java.nio.file.SecureDirectoryStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeStaffController implements MainInjectable {

    // --- fx:id dari FXML ---
    @FXML private TextField searchField;
    @FXML private Button btnSearch;

    @FXML private TableView<LogAktivitas> tableAktivitas;
    @FXML private TableColumn<LogAktivitas, String> colAktivitas;
    @FXML private TableColumn<LogAktivitas, String> colTgl;
    @FXML private TableColumn<LogAktivitas, String> colPukul;

    @FXML private ComboBox<String> comboRentang;
    @FXML private TableView<StatistikDokumen> tableDokumen;
    @FXML private TableColumn<StatistikDokumen, String> colDokumen;
    @FXML private TableColumn<StatistikDokumen, Integer> colTotal;

    @FXML private TextField spField;
    @FXML private TextField tagihanField;
    @FXML private TextField tblField;
    @FXML private TextField tlField;
    @FXML private TextField tbField;
    @FXML private TextField tjtField;

    private StaffDashboardDAO dao = new StaffDashboardDAOImpl();
    private int currentUserId = 1; // <- ubah sesuai session user kamu
    private MainController mainController;

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleAddPenawaran() {
        mainController.handleAddPenawaran();

    }
    @FXML
    private void handleAddTagihan() {
        mainController.handleManageTagihan("Add", null);
    }
    @FXML
    public void initialize() {
        colAktivitas.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAktivitasRapi()));
        colTgl.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(extractDate(cell.getValue().getWaktu())));
        colPukul.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(extractTime(cell.getValue().getWaktu())));

        colDokumen.setCellValueFactory(new PropertyValueFactory<>("namaDokumen"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        populateMonthCombo();

        Platform.runLater(() -> {
            loadAktivitas();
            loadDokumenForSelectedMonth();
            loadTotalDokumenSaya();
        });

        btnSearch.setOnAction(e -> doSearch());
        searchField.setOnAction(e -> doSearch());

        comboRentang.setOnAction(e -> loadDokumenForSelectedMonth());

        tjtField.setEditable(false);
        tbField.setEditable(false);
        tlField.setEditable(false);
        tblField.setEditable(false);
        tagihanField.setEditable(false);
        spField.setEditable(false);
    }

    private void populateMonthCombo() {
        ObservableList<String> months = FXCollections.observableArrayList();
        YearMonth now = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            YearMonth ym = now.minusMonths(i);
            String label = ym.getMonth().getDisplayName(TextStyle.SHORT, new Locale("id")) + " " + ym.getYear();
            months.add(label);
        }
        comboRentang.setItems(months);
        comboRentang.getSelectionModel().selectFirst();
    }

    private void loadAktivitas() {
        try {
            int userId = Session.currentUser.getId();
            List<LogAktivitas> logs = dao.getAktivitasUser(userId, 50);
            ObservableList<LogAktivitas> obs = FXCollections.observableArrayList(logs);
            tableAktivitas.setItems(obs);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Gagal load aktivitas: " + ex.getMessage());
        }
    }

    private void doSearch() {
        String kw = searchField.getText();
        if (kw == null || kw.trim().isEmpty()) {
            loadAktivitas();
            return;
        }
        try {
            int userId = Session.currentUser.getId();
            List<LogAktivitas> logs = dao.searchAktivitas(userId, kw.trim(), 100);
            tableAktivitas.setItems(FXCollections.observableArrayList(logs));
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Search gagal: " + ex.getMessage());
        }
    }

    private void loadDokumenForSelectedMonth() {
        String sel = comboRentang.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        // parse e.g. "Des 2025" or "Dec 2025" depending locale
        try {
            String[] parts = sel.split(" ");
            String monShort = parts[0];
            int year = Integer.parseInt(parts[1]);

            // map short month (indonesian) to number - fallback by parsing English
            int month = parseMonthShort(monShort);

            int userId = Session.currentUser.getId();

            List<StatistikDokumen> stats = dao.getDokumenBulanan(userId, month, year);
            tableDokumen.setItems(FXCollections.observableArrayList(stats));
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Gagal load dokumen bulanan: " + ex.getMessage());
        }
    }

    private int parseMonthShort(String s) {
        // try Indonesian short names first
        String lower = s.toLowerCase();
        switch (lower) {
            case "jan": case "jan." : return 1;
            case "feb": return 2;
            case "mar": return 3;
            case "apr": return 4;
            case "mei": return 5;
            case "jun": return 6;
            case "jul": return 7;
            case "agu": case "agt": return 8;
            case "sep": return 9;
            case "okt": return 10;
            case "nov": return 11;
            case "des": return 12;
            default:
                // try parse English short
                try {
                    return java.time.Month.valueOf(s.toUpperCase()).getValue();
                } catch (Exception ex) {
                    // fallback to current month
                    return java.time.LocalDate.now().getMonthValue();
                }
        }
    }

    private void loadTotalDokumenSaya() {
        try {
            int userId= Session.currentUser.getId();
            Map<String, Integer> map = dao.getTotalDokumenSaya(userId);
            int sp = map.getOrDefault("surat_penawaran", 0);
            int tagihan = map.getOrDefault("tagihan", 0);
            int belum = map.getOrDefault("pending", map.getOrDefault("BELUM_DIBAYAR", map.getOrDefault("belum", 0)));
            int lunas = map.getOrDefault("dibayar", map.getOrDefault("LUNAS", map.getOrDefault("lunas", 0)));
            int batal = map.getOrDefault("batal", map.getOrDefault("BATAL", 0));
            int jatuh = map.getOrDefault("telat", map.getOrDefault("TERLAMBAT", 0));
            // set fields
            spField.setText(String.valueOf(sp));
            tagihanField.setText(String.valueOf(tagihan));
            tblField.setText(String.valueOf(belum));
            tlField.setText(String.valueOf(lunas));
            tbField.setText(String.valueOf(batal));
            tjtField.setText(String.valueOf(jatuh));
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Gagal load total dokumen: " + ex.getMessage());
        }
    }

    private String extractDate(String datetime) {
        if (datetime == null) return "";
        // assume format "YYYY-MM-DD ..." or ISO
        if (datetime.length() >= 10) return datetime.substring(0, 10);
        return datetime;
    }

    private String extractTime(String datetime) {
        if (datetime == null) return "";
        // try to extract time portion
        if (datetime.length() >= 19) return datetime.substring(11, 19);
        return "";
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}

