//package main.java.com.project.app.controller;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.stage.Stage;
//import javafx.event.ActionEvent;
//import main.java.com.project.app.config.DBConnection;
//
//
//import java.sql.Connection;
//
//public class TambahRincianPopupController {
//
//    @FXML private TextField namaPekerjaanField;
//    @FXML private TextField namaMesinField;
//    @FXML private TextField spesifikasiField;
//    @FXML private TextArea deskripsiArea;
//    @FXML private ComboBox<String> komponenComboBox;
//    @FXML private TextField qtyField;
//    @FXML private TextField hargaModalField;
//    @FXML private TextField hargaAktualField;
//    @FXML private Button btnTambahKomponen;
//    @FXML private TableView<KomponenItem> tabelKomponen;
//    @FXML private TableColumn<KomponenItem, String> colNama;
//    @FXML private TableColumn<KomponenItem, Integer> colQty;
//    @FXML private TableColumn<KomponenItem, Double> colHarga;
//    @FXML private TableColumn<KomponenItem, Double> colTotal;
//    @FXML private Label labelTotalHarga;
//    @FXML private Button btnSimpan;
//    @FXML private Button btnBatal;
//
//    private int spId;
//    private AddNewPenawaranController parentController;
//    private JobDAO jobDAO;
//    private JobComponentDAO komponenDAO;
//
//    public void setSpId(int spId) {
//        this.spId = spId;
//    }
//
//    public void setAddNewPenawaranController(AddNewPenawaranController controller) {
//        this.parentController = controller;
//    }
//
//    @FXML
//    public void initialize() {
//        try {
//            Connection conn = DBConnection.getConnection();
//            this.jobDAO = new JobDAO(conn);
//            this.komponenDAO = new JobComponentDAO(conn);
//
//            setupUI();
//            setupEventHandlers();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setupUI() {
//        // Setup combo box dengan daftar komponen
//        komponenComboBox.getItems().addAll(
//                "Bearing 6205", "Belt Conveyor", "Motor 3 Phase", "PLC Siemens",
//                "Sensor Proximity", "Valve Pneumatic", "Gearbox 1:10", "Coupling Flexible"
//        );
//
//        // Setup table columns
//        colNama.setCellValueFactory(cellData -> cellData.getValue().namaProperty());
//        colQty.setCellValueFactory(cellData -> cellData.getValue().qtyProperty().asObject());
//        colHarga.setCellValueFactory(cellData -> cellData.getValue().hargaProperty().asObject());
//        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());
//    }
//
//    private void setupEventHandlers() {
//        btnTambahKomponen.setOnAction(this::handleTambahKomponen);
//        btnSimpan.setOnAction(this::handleSimpan);
//        btnBatal.setOnAction(this::handleBatal);
//
//        // Auto-calculate harga aktual
//        hargaModalField.textProperty().addListener((obs, oldVal, newVal) -> {
//            calculateHargaAktual();
//        });
//    }
//
//    @FXML
//    private void handleTambahKomponen(ActionEvent event) {
//        try {
//            String nama = komponenComboBox.getValue();
//            String qtyText = qtyField.getText();
//            String hargaText = hargaAktualField.getText();
//
//            if (nama == null || nama.isEmpty()) {
//                showAlert("Error", "Pilih nama komponen terlebih dahulu!");
//                return;
//            }
//
//            if (qtyText.isEmpty() || hargaText.isEmpty()) {
//                showAlert("Error", "Qty dan harga harus diisi!");
//                return;
//            }
//
//            int qty = Integer.parseInt(qtyText);
//            double harga = Double.parseDouble(hargaText);
//
//            if (qty <= 0 || harga <= 0) {
//                showAlert("Error", "Qty dan harga harus lebih dari 0!");
//                return;
//            }
//
//            KomponenItem item = new KomponenItem(nama, qty, harga);
//            tabelKomponen.getItems().add(item);
//
//            updateTotalHarga();
//
//            // Clear fields
//            komponenComboBox.getSelectionModel().clearSelection();
//            qtyField.clear();
//            hargaModalField.clear();
//            hargaAktualField.clear();
//
//        } catch (NumberFormatException e) {
//            showAlert("Error", "Format angka tidak valid!");
//        }
//    }
//
//    private void calculateHargaAktual() {
//        try {
//            String modalText = hargaModalField.getText();
//            if (!modalText.isEmpty()) {
//                double modal = Double.parseDouble(modalText);
//                double aktual = modal * 1.20; // 20% markup
//                hargaAktualField.setText(String.valueOf((int) aktual));
//            }
//        } catch (NumberFormatException e) {
//            // Ignore
//        }
//    }
//
//    private void updateTotalHarga() {
//        double total = tabelKomponen.getItems().stream()
//                .mapToDouble(KomponenItem::getTotal)
//                .sum();
//
//        labelTotalHarga.setText("Rp " + String.format("%,.0f", total));
//    }
//
//    @FXML
//    private void handleSimpan(ActionEvent event) {
//        try {
//            // Validasi
//            if (!validateForm()) {
//                return;
//            }
//
//            // 1. Simpan job
//            int jobId = jobDAO.insertJob(spId,
//                    namaPekerjaanField.getText(),
//                    namaMesinField.getText(),
//                    spesifikasiField.getText(),
//                    deskripsiArea.getText()
//            );
//
//            // 2. Simpan komponen-komponen
//            for (KomponenItem item : tabelKomponen.getItems()) {
//                komponenDAO.insertKomponen(jobId,
//                        item.getNama(),
//                        item.getQty(),
//                        item.getHarga()
//                );
//            }
//
//            showAlert("Success", "Rincian pekerjaan berhasil disimpan!");
//
//            // Tutup popup
//            Stage stage = (Stage) btnSimpan.getScene().getWindow();
//            stage.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert("Error", "Gagal menyimpan: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    private void handleBatal(ActionEvent event) {
//        Stage stage = (Stage) btnBatal.getScene().getWindow();
//        stage.close();
//    }
//
//    private boolean validateForm() {
//        if (namaPekerjaanField.getText().isEmpty()) {
//            showAlert("Error", "Nama pekerjaan harus diisi!");
//            return false;
//        }
//
//        if (namaMesinField.getText().isEmpty()) {
//            showAlert("Error", "Nama mesin harus diisi!");
//            return false;
//        }
//
//        if (tabelKomponen.getItems().isEmpty()) {
//            showAlert("Error", "Minimal tambahkan 1 komponen!");
//            return false;
//        }
//
//        return true;
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Model class untuk tabel komponen
//    public static class KomponenItem {
//        private final String nama;
//        private final int qty;
//        private final double harga;
//
//        public KomponenItem(String nama, int qty, double harga) {
//            this.nama = nama;
//            this.qty = qty;
//            this.harga = harga;
//        }
//
//        public String getNama() { return nama; }
//        public int getQty() { return qty; }
//        public double getHarga() { return harga; }
//        public double getTotal() { return qty * harga; }
//
//        // Properties untuk binding tabel
//        public javafx.beans.property.StringProperty namaProperty() {
//            return new javafx.beans.property.SimpleStringProperty(nama);
//        }
//
//        public javafx.beans.property.IntegerProperty qtyProperty() {
//            return new javafx.beans.property.SimpleIntegerProperty(qty);
//        }
//
//        public javafx.beans.property.DoubleProperty hargaProperty() {
//            return new javafx.beans.property.SimpleDoubleProperty(harga);
//        }
//
//        public javafx.beans.property.DoubleProperty totalProperty() {
//            return new javafx.beans.property.SimpleDoubleProperty(getTotal());
//        }
//    }
//}