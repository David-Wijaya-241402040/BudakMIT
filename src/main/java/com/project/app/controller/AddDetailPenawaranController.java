package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.DetailPekerjaanDAO;
import main.java.com.project.app.dao.JobDAO;
import main.java.com.project.app.dao.SPDetailDAO;
import main.java.com.project.app.dao.KomponenDAO;
import main.java.com.project.app.model.DetailPekerjaan;
import main.java.com.project.app.model.ItemPenawaran;
import main.java.com.project.app.model.Job;
import main.java.com.project.app.model.Komponen;
import main.java.com.project.app.model.PenawaranModel.SPJobComponent;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddDetailPenawaranController implements Initializable {

    @FXML private Text titleText;
    @FXML private TextField namaPekerjaanField;
    @FXML private TextField namaMesinField;
    @FXML private TextField spesifikasiMesinField;
    @FXML private TextArea deskripsiArea;
    @FXML private ComboBox<String> namaItemComboBox;
    @FXML private TextField qtyField;
    @FXML private TextField hargaModalField;
    @FXML private TextField hargaAktualField;
    @FXML private TableView<ItemPenawaran> itemTableView;
    @FXML private TableColumn<ItemPenawaran, String> itemColumn;
    @FXML private TableColumn<ItemPenawaran, Integer> qtyColumn;
    @FXML private TableColumn<ItemPenawaran, Double> hargaColumn;
    @FXML private TextField adminIdField;
    @FXML private TextField noSuratField;
    @FXML private TextField namaPerusahaanField;
    @FXML private TextField tanggalField;
    @FXML private TextField totalHargaField;
    @FXML private Button saveButton;
    @FXML private Button addItemButton;

    private MainController mainController;
    private SPDetailDAO spDetailDAO;
    private KomponenDAO komponenDAO;
    private JobDAO jobDAO;
    private DetailPekerjaanDAO detailPekerjaanDAO;
    private int spId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Setup koneksi database
            Connection conn = DBConnection.getConnection();
            this.spDetailDAO = new SPDetailDAO(conn);
            this.komponenDAO = new KomponenDAO(conn);
            this.jobDAO = new JobDAO(conn);
            this.detailPekerjaanDAO = new DetailPekerjaanDAO(conn);

            // Setup UI
            setupTable();
            setupComboBox();
            setupEventHandlers();
            setupNumberFormatters();

            System.out.println("✅ AddDetailPenawaranController initialized");

        } catch (Exception e) {
            System.out.println("❌ Error initializing AddDetailPenawaranController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void showDetailPenawaran(int spId) {
        this.spId = spId;
        System.out.println("=== DEBUG ===");
        System.out.println("Received spId: " + spId);
        System.out.println("MainController: " + (mainController != null ? "Set" : "NULL"));
        System.out.println("=== END DEBUG ===");

        System.out.println("🎯 AddDetailPenawaranController menerima spId: " + spId);

        if (spId > 0) {
            loadDataFromDatabase(spId);
            titleText.setText("TAMBAH RINCIAN PEKERJAAN - SP ID: " + spId);
        } else {
            clearForm();
            titleText.setText("TAMBAH RINCIAN PEKERJAAN BARU");
        }
    }

    private void loadDataFromDatabase(int spId) {
        try {
            System.out.println("📊 Loading data untuk SP ID: " + spId);

            List<SPJobComponent> data = spDetailDAO.getDetailBySP(spId);

            if (data != null && !data.isEmpty()) {
                System.out.println("✅ Data ditemukan, jumlah baris: " + data.size());

                SPJobComponent firstData = data.get(0);

                adminIdField.setText(firstData.user_id);
                noSuratField.setText(firstData.no_sp);
                namaPerusahaanField.setText(firstData.namaPerusahaan);

                if (firstData.tanggal_surat_penawaran != null && !firstData.tanggal_surat_penawaran.isEmpty()) {
                    tanggalField.setText(formatTanggal(firstData.tanggal_surat_penawaran));
                } else {
                    tanggalField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }

            } else {
                System.out.println("⚠️ Data tidak ditemukan untuk SP ID: " + spId);
                adminIdField.setText("ADMIN001");
                noSuratField.setText("SP-" + spId);
                namaPerusahaanField.setText("Perusahaan dari SP ID " + spId);
                tanggalField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }

            namaPekerjaanField.requestFocus();

        } catch (Exception e) {
            System.out.println("❌ Error loading data dari database: " + e.getMessage());
            e.printStackTrace();

            adminIdField.setText("ADMIN001");
            noSuratField.setText("SP-" + spId);
            namaPerusahaanField.setText("Perusahaan dari SP ID " + spId);
            tanggalField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    private String formatTanggal(String tanggalDb) {
        try {
            if (tanggalDb.contains(" ")) {
                tanggalDb = tanggalDb.split(" ")[0];
            }

            if (tanggalDb.contains("-")) {
                LocalDate date = LocalDate.parse(tanggalDb);
                return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else if (tanggalDb.contains("/")) {
                String[] parts = tanggalDb.split("/");
                if (parts.length == 3) {
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);
                    if (year < 100) {
                        year += 2000;
                    }
                    LocalDate date = LocalDate.of(year, month, day);
                    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Gagal parsing tanggal: " + tanggalDb);
        }
        return tanggalDb;
    }

    private void setupTable() {
        // Setup table columns dengan lambda expressions
        itemColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNamaItem()));

        qtyColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQty()).asObject());

        hargaColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getHarga()).asObject());

        // Format currency untuk harga column
        hargaColumn.setCellFactory(tc -> new TableCell<ItemPenawaran, Double>() {
            @Override
            protected void updateItem(Double harga, boolean empty) {
                super.updateItem(harga, empty);
                if (empty || harga == null) {
                    setText(null);
                } else {
                    setText(formatCurrency(harga));
                }
            }
        });
    }

    private void setupComboBox() {
        try {
            List<String> daftarKomponen = komponenDAO.getAllNamaKomponen();
            namaItemComboBox.getItems().addAll(daftarKomponen);
        } catch (Exception e) {
            e.printStackTrace();
            namaItemComboBox.getItems().addAll(
                    "Bearing 6205",
                    "Belt Conveyor",
                    "Motor 3 Phase",
                    "PLC Siemens",
                    "Sensor Proximity",
                    "Valve Pneumatic",
                    "Gearbox 1:10",
                    "Coupling Flexible"
            );
        }
    }

    private void setupEventHandlers() {
        if (addItemButton != null) {
            addItemButton.setOnAction(this::handleAddItem);
        }

        if (saveButton != null) {
            saveButton.setOnAction(this::handleSave);
        }

        hargaModalField.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateHargaAktual();
        });
    }

    private void setupNumberFormatters() {
        setupNumericField(qtyField);
        setupNumericField(hargaModalField);
        setupNumericField(hargaAktualField);
    }

    private void setupNumericField(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                field.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
    }

    @FXML
    private void handleAddItem(ActionEvent event) {
        try {
            String namaItem = namaItemComboBox.getValue();
            String qtyText = qtyField.getText();
            String hargaAktualText = hargaAktualField.getText();

            if (namaItem == null || namaItem.isEmpty()) {
                showAlert("Error", "Silakan pilih Nama Item terlebih dahulu!");
                return;
            }

            if (qtyText.isEmpty() || hargaAktualText.isEmpty()) {
                showAlert("Error", "Qty dan Harga Aktual harus diisi!");
                return;
            }

            int qty = Integer.parseInt(qtyText);
            double harga = Double.parseDouble(hargaAktualText);

            if (qty <= 0 || harga <= 0) {
                showAlert("Error", "Qty dan Harga harus lebih dari 0!");
                return;
            }

            ItemPenawaran item = new ItemPenawaran(namaItem, qty, harga);
            itemTableView.getItems().add(item);

            updateTotalHarga();

            namaItemComboBox.getSelectionModel().clearSelection();
            qtyField.clear();
            hargaModalField.clear();
            hargaAktualField.clear();

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Mohon isi Qty dan Harga dengan angka yang valid!");
        }
    }

    private void calculateHargaAktual() {
        try {
            String hargaModalText = hargaModalField.getText();
            if (!hargaModalText.isEmpty()) {
                double hargaModal = Double.parseDouble(hargaModalText);
                double hargaAktual = hargaModal * 1.20;
                hargaAktualField.setText(String.format("%.0f", hargaAktual));
            }
        } catch (NumberFormatException e) {
            // Ignore
        }
    }

    private void updateTotalHarga() {
        double total = 0.0;

        for (ItemPenawaran item : itemTableView.getItems()) {
            total += item.getHarga() * item.getQty();
        }

        totalHargaField.setText("Rp " + formatCurrency(total));
    }

    @FXML
    private void handleSave(ActionEvent event) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("💾 Menyimpan data untuk SP ID: " + spId);

            if (!validateForm()) {
                if (conn != null) conn.rollback();
                return;
            }

            if (!validateItemsBeforeSave()) {
                if (conn != null) conn.rollback();
                return;
            }

            // Collect data dari form
            String namaPekerjaan = namaPekerjaanField.getText().trim();
            String namaMesin = namaMesinField.getText().trim();
            String spesifikasi = spesifikasiMesinField.getText().trim();
            String deskripsi = deskripsiArea.getText().trim();

            // Gabungkan spesifikasi dan deskripsi
            String spesifikasiFull = spesifikasi;
            if (!deskripsi.isEmpty()) {
                spesifikasiFull += "\n\nDeskripsi:\n" + deskripsi;
            }

            // 1. Insert ke tabel jobs - GUNAKAN INSTANCE, BUKAN STATIC
            Job job = new Job((long) spId, namaPekerjaan, namaMesin, spesifikasiFull);
            Long jobId = jobDAO.insertJob(job); // PERBAIKAN DI SINI

            System.out.println("✅ Job berhasil disimpan dengan ID: " + jobId);

            // 2. Insert detail pekerjaan (komponen-komponen)
            List<DetailPekerjaan> detailList = new ArrayList<>();

            for (ItemPenawaran item : itemTableView.getItems()) {
                Long componentId = komponenDAO.getComponentIdByName(item.getNamaItem());

                if (componentId == null) {
                    conn.rollback();
                    showAlert("Error", "Komponen '" + item.getNamaItem() + "' tidak ditemukan di database!");
                    return;
                }

                Komponen komponen = komponenDAO.getKomponenById(componentId);
                if (komponen == null) {
                    conn.rollback();
                    showAlert("Error", "Data komponen '" + item.getNamaItem() + "' tidak valid!");
                    return;
                }

                // VALIDASI HARGA
                BigDecimal hargaAcuan = komponen.getHargaAcuan();
                BigDecimal hargaAktual = BigDecimal.valueOf(item.getHarga());

                if (hargaAktual.compareTo(hargaAcuan) < 0) {
                    conn.rollback();
                    String message = String.format(
                            "Harga aktual untuk '%s' (Rp %s) lebih rendah dari harga acuan (Rp %s)!\n\n" +
                                    "Silakan perbaiki harga sebelum menyimpan.",
                            item.getNamaItem(),
                            formatCurrency(hargaAktual.doubleValue()),
                            formatCurrency(hargaAcuan.doubleValue())
                    );
                    showAlert("Validasi Harga Gagal", message);
                    return;
                }

                // Cek apakah komponen sudah ada di job ini
                if (detailPekerjaanDAO.isComponentInJob(jobId, componentId)) {
                    conn.rollback();
                    showAlert("Error", "Komponen '" + item.getNamaItem() + "' sudah ada dalam pekerjaan ini!");
                    return;
                }

                DetailPekerjaan detail = new DetailPekerjaan(
                        jobId,
                        componentId,
                        item.getQty(),
                        item.getHarga()
                );
                detailList.add(detail);
            }

            // Insert batch detail pekerjaan - GUNAKAN INSTANCE
            if (!detailList.isEmpty()) {
                detailPekerjaanDAO.insertDetailPekerjaanBatch(detailList);
                System.out.println("✅ " + detailList.size() + " komponen berhasil disimpan");
            }

            conn.commit();

            double totalHarga = calculateTotalHarga();
            showSummary(namaPekerjaan, detailList.size(), totalHarga);

            showAlert("Berhasil", "Data pekerjaan dan komponen berhasil disimpan!");

            clearForm();
            handleBack();

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            showAlert("Database Error", "Gagal menyimpan data: " + e.getMessage());

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // METHOD BARU: Validasi items sebelum save
    private boolean validateItemsBeforeSave() {
        if (itemTableView.getItems().isEmpty()) {
            showAlert("Validasi Gagal", "Minimal tambahkan 1 item komponen!");
            return false;
        }

        // Validasi setiap item
        for (int i = 0; i < itemTableView.getItems().size(); i++) {
            ItemPenawaran item = itemTableView.getItems().get(i);

            try {
                Long componentId = komponenDAO.getComponentIdByName(item.getNamaItem());
                if (componentId == null) {
                    showAlert("Komponen Tidak Ditemukan",
                            String.format("Komponen '%s' (baris %d) tidak ditemukan di database!",
                                    item.getNamaItem(), i + 1));
                    return false;
                }

                Komponen komponen = komponenDAO.getKomponenById(componentId);
                if (komponen != null) {
                    BigDecimal hargaAcuan = komponen.getHargaAcuan();
                    BigDecimal hargaAktual = BigDecimal.valueOf(item.getHarga());

                    if (hargaAktual.compareTo(hargaAcuan) < 0) {
                        String message = String.format(
                                "VALIDASI HARGA GAGAL!\n\n" +
                                        "Komponen: %s\n" +
                                        "Harga Aktual: Rp %s\n" +
                                        "Harga Acuan: Rp %s\n\n" +
                                        "Harga aktual tidak boleh lebih rendah dari harga acuan!",
                                item.getNamaItem(),
                                formatCurrency(hargaAktual.doubleValue()),
                                formatCurrency(hargaAcuan.doubleValue())
                        );
                        showAlert("Harga Tidak Valid", message);
                        return false;
                    }
                }
            } catch (SQLException e) {
                showAlert("Database Error", "Gagal validasi komponen: " + e.getMessage());
                return false;
            }
        }

        return true;
    }

    // METHOD BARU: Hitung total harga
    private double calculateTotalHarga() {
        double total = 0.0;
        for (ItemPenawaran item : itemTableView.getItems()) {
            total += item.getHarga() * item.getQty();
        }
        return total;
    }

    // METHOD BARU: Tampilkan summary
    private void showSummary(String namaPekerjaan, int jumlahItem, double totalHarga) {
        System.out.println("\n📋 SUMMARY PENAWARAN");
        System.out.println("======================");
        System.out.println("Pekerjaan: " + namaPekerjaan);
        System.out.println("SP ID: " + spId);
        System.out.println("Jumlah Item: " + jumlahItem);
        System.out.println("Total Harga: Rp " + String.format("%,.0f", totalHarga));
        System.out.println("======================\n");
    }

    @FXML
    private void handleBack() {
        if (mainController != null) {
            mainController.loadPage("addnewpenawaran");
        }
    }

    private boolean validateForm() {
        if (namaPekerjaanField.getText().isEmpty()) {
            showAlert("Error", "Nama Pekerjaan harus diisi!");
            namaPekerjaanField.requestFocus();
            return false;
        }

        if (namaMesinField.getText().isEmpty()) {
            showAlert("Error", "Nama Mesin harus diisi!");
            namaMesinField.requestFocus();
            return false;
        }

        if (itemTableView.getItems().isEmpty()) {
            showAlert("Error", "Minimal tambahkan 1 item komponen!");
            return false;
        }

        return true;
    }

    private void clearForm() {
        namaPekerjaanField.clear();
        namaMesinField.clear();
        spesifikasiMesinField.clear();
        deskripsiArea.clear();

        namaItemComboBox.getSelectionModel().clearSelection();
        qtyField.clear();
        hargaModalField.clear();
        hargaAktualField.clear();

        itemTableView.getItems().clear();

        adminIdField.clear();
        noSuratField.clear();
        namaPerusahaanField.clear();
        tanggalField.clear();
        totalHargaField.clear();
    }

    private String formatCurrency(double amount) {
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("###,###,###");
        return formatter.format(amount);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}