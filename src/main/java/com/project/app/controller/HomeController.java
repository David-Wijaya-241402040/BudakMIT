package main.java.com.project.app.controller;

<<<<<<< HEAD
public class HomeController {
=======
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import main.java.com.project.app.dao.DashboardDAO;
import main.java.com.project.app.dao.DashboardDAOImpl;
import main.java.com.project.app.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class HomeController {

    // Card 1
    @FXML private Text totalDokumen;
    @FXML private AnchorPane barSP;
    @FXML private AnchorPane barTBL;
    @FXML private AnchorPane barTL;
    @FXML private AnchorPane barTB;
    @FXML private TextField fieldSP;
    @FXML private TextField fieldTBL;
    @FXML private TextField fieldTL;
    @FXML private TextField fieldTB;

    // Card 2 (left)
    @FXML private TextField searchField;
    @FXML private Button btnSearch;
    @FXML private TableView<UserSummary> tableUsers;
    @FXML private TableColumn<UserSummary, Integer> colId;
    @FXML private TableColumn<UserSummary, String> colUsername;
    @FXML private TableColumn<UserSummary, Integer> colSP;
    @FXML private TableColumn<UserSummary, Integer> colTBL;
    @FXML private TableColumn<UserSummary, Integer> colTL;
    @FXML private TableColumn<UserSummary, Integer> colTB;

    // Card 3 (detail)
    @FXML private ComboBox<String> comboMonth; // This Month, Last 6, Last 12, Select Month...
    @FXML private TableView<MonthlyDocumentDetail> tableDetail;
    @FXML private TableColumn<MonthlyDocumentDetail, String> colTransaksi;
    @FXML private TableColumn<MonthlyDocumentDetail, Integer> colTotal;

    // Card 4 (status)
    @FXML private TextField fieldBelumBayar;
    @FXML private TextField fieldLunas;
    @FXML private TextField fieldBatal;
    @FXML private TextField fieldJatuhTempo;

    private final DashboardDAO dao = new DashboardDAOImpl();
    private static final double TOTAL_BAR_WIDTH = 979.0;
    private static final DateTimeFormatter YM_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    @FXML
    public void initialize() {
        setupCombo();
        setupUserTable();
        setupDetailTable();

        // default load = This Month
        loadDataForThisMonth();

        // search button action
        btnSearch.setOnAction(e -> filterUsers(searchField.getText()));

        fieldSP.setEditable(false);
        fieldTBL.setEditable(false);
        fieldTL.setEditable(false);
        fieldTB.setEditable(false);

        fieldBelumBayar.setEditable(false);
        fieldLunas.setEditable(false);
        fieldBatal.setEditable(false);
        fieldJatuhTempo.setEditable(false);
    }

    private void setupCombo() {
        comboMonth.getItems().addAll("This Month", "Last 6 Months", "Last 12 Months", "Select Month...");
        comboMonth.setValue("This Month");
        comboMonth.setOnAction(e -> {
            String val = comboMonth.getValue();
            if ("Select Month...".equals(val)) {
                Pair<String,String> sel = openMonthRangeDialog();
                if (sel != null) {
                    loadAllBetween(sel.getKey(), sel.getValue());
                } else {
                    // reset to previous value or this month
                    comboMonth.setValue("This Month");
                    loadDataForThisMonth();
                }
            } else if ("This Month".equals(val)) {
                loadDataForThisMonth();
            } else if ("Last 6 Months".equals(val)) {
                Pair<String,String> range = calcLastNMonthsRange(6);
                loadAllBetween(range.getKey(), range.getValue());
            } else if ("Last 12 Months".equals(val)) {
                Pair<String,String> range = calcLastNMonthsRange(12);
                loadAllBetween(range.getKey(), range.getValue());
            }
        });
    }

    // helper: compute range YYYY-MM for last n months (inclusive)
    private Pair<String,String> calcLastNMonthsRange(int n) {
        LocalDate now = LocalDate.now();
        LocalDate end = now.withDayOfMonth(1).plusMonths(0); // month of now
        LocalDate start = end.minusMonths(n-1); // inclusive
        return new Pair<>(start.format(YM_FMT), end.format(YM_FMT));
    }

    // open modal month-range picker (FXML)
    private Pair<String,String> openMonthRangeDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(
                            "/main/resources/com/project/app/fxml/view/MonthRangePicker.fxml"
                    ))
            );

            Parent pane = loader.load(); // FIX: Parent instead of AnchorPane
            MonthRangePickerController ctrl = loader.getController();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Select Month Range");
            dialog.setScene(new Scene(pane));
            dialog.showAndWait();

            return ctrl.getSelectedRange(); // may be null
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private void loadDataForThisMonth() {
        String ym = LocalDate.now().format(YM_FMT);
        loadAllBetween(ym, ym);
    }

    // central loader: load all cards using startYearMonth and endYearMonth (format "YYYY-MM")
    private void loadAllBetween(String startYM, String endYM) {
        // Card 1
        int sp = dao.countSPBetween(startYM, endYM);
        int pending = dao.countTagihanPendingBetween(startYM, endYM);
        int lunas = dao.countTagihanLunasBetween(startYM, endYM);
        int batal = dao.countTagihanBatalBetween(startYM, endYM);

        int totalDocuments = sp + pending + lunas + batal;
        totalDokumen.setText(String.valueOf(totalDocuments));

        if (totalDocuments == 0) {
            setBarWidths(0,0,0,0);
            fieldSP.setText("0%");
            fieldTBL.setText("0%");
            fieldTL.setText("0%");
            fieldTB.setText("0%");
        } else {
            double pSP = (sp * 100.0) / totalDocuments;
            double pPending = (pending * 100.0) / totalDocuments;
            double pLunas = (lunas * 100.0) / totalDocuments;
            double pBatal = (batal * 100.0) / totalDocuments;

            fieldSP.setText(String.format("%.0f%%", pSP));
            fieldTBL.setText(String.format("%.0f%%", pPending));
            fieldTL.setText(String.format("%.0f%%", pLunas));
            fieldTB.setText(String.format("%.0f%%", pBatal));

            double wSP = (pSP / 100.0) * TOTAL_BAR_WIDTH;
            double wPending = (pPending / 100.0) * TOTAL_BAR_WIDTH;
            double wLunas = (pLunas / 100.0) * TOTAL_BAR_WIDTH;
            double wBatal = (pBatal / 100.0) * TOTAL_BAR_WIDTH;

            setBarWidths(wSP, wPending, wLunas, wBatal);
        }

        // Card 2
        List<UserSummary> users = dao.getStaffSummariesBetween(startYM, endYM);
        tableUsers.setItems(FXCollections.observableArrayList(users));

        // Card 3
        List<MonthlyDocumentDetail> details = dao.getMonthlyDocumentDetailsBetween(startYM, endYM);
        tableDetail.setItems(FXCollections.observableArrayList(details));

        // Card 4
        StatusSummary s = dao.getStatusSummaryBetween(startYM, endYM);
        fieldBelumBayar.setText(String.valueOf(s.getPending()));
        fieldJatuhTempo.setText(String.valueOf(s.getTelat()));
        fieldLunas.setText(String.valueOf(s.getLunas()));
        fieldBatal.setText(String.valueOf(s.getBatal()));
    }

    private void setBarWidths(double wSP, double wPending, double wLunas, double wBatal) {
        barSP.setPrefWidth(wSP);
        barTBL.setPrefWidth(wPending);
        barTL.setPrefWidth(wLunas);
        barTB.setPrefWidth(wBatal);
    }

    private void setupUserTable() {
        // map columns
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        colUsername.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNickname()));
        colSP.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getTotalSP()).asObject());
        colTBL.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getTotalPending()).asObject());
        colTL.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getTotalLunas()).asObject());
        colTB.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getTotalBatal()).asObject());
    }

    private void setupDetailTable() {
        colTransaksi.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        colTotal.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getTotal()).asObject());
    }

    private void filterUsers(String q) {
        if (q == null || q.isEmpty()) {
            // reload current selection
            String sel = comboMonth.getValue();
            if ("Last 6 Months".equals(sel)) {
                Pair<String,String> r = calcLastNMonthsRange(6);
                tableUsers.setItems(FXCollections.observableArrayList(dao.getStaffSummariesBetween(r.getKey(), r.getValue())));
            } else if ("Last 12 Months".equals(sel)) {
                Pair<String,String> r = calcLastNMonthsRange(12);
                tableUsers.setItems(FXCollections.observableArrayList(dao.getStaffSummariesBetween(r.getKey(), r.getValue())));
            } else if ("This Month".equals(sel) || sel == null) {
                String ym = LocalDate.now().format(YM_FMT);
                tableUsers.setItems(FXCollections.observableArrayList(dao.getStaffSummariesBetween(ym, ym)));
            } else {
                // keep current table content
            }
            return;
        }
        String key = q.toLowerCase();
        ObservableList<UserSummary> all = tableUsers.getItems();
        ObservableList<UserSummary> filtered = FXCollections.observableArrayList();
        for (UserSummary u : all) {
            if (u.getNickname() != null && u.getNickname().toLowerCase().contains(key)) filtered.add(u);
            else if (String.valueOf(u.getId()).contains(key)) filtered.add(u);
        }
        tableUsers.setItems(filtered);
    }
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
}
