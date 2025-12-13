package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.PenawaranDAO;
import main.java.com.project.app.dao.SPDetailDAO;
import main.java.com.project.app.model.PenawaranModel;
import main.java.com.project.app.session.Session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddNewPenawaranController {
    @FXML VBox vboxDetail;
    @FXML ScrollPane scrollRincian;
    @FXML Button btnTambahRincian;

    private SPDetailDAO dao;
    private int spId;
    private MainController mainController;

    public void setMainController(MainController mainController) {this.mainController = mainController;}

    public void setSPId(int spId) {
        this.spId = spId;
        System.out.println("SP ID berhasil diterima: " + spId);
    }

    @FXML
    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            this.dao = new SPDetailDAO(conn);

            String role = Session.currentUser.getRoles();
            if(role.equals("owner")) {
                btnTambahRincian.setDisable(true);
                btnTambahRincian.setVisible(false);
            } else if (role.equals("staff")) {
                btnTambahRincian.setDisable(false);
                btnTambahRincian.setVisible(true);
            }

            // Atur event handler untuk tombol Tambah Rincian
            btnTambahRincian.setOnAction(event -> handleTambahRincian());
            scrollRincian.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollRincian.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        } catch (Exception e) {
            System.out.println("❌ Koneksi DB gagal di initialize!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTambahRincian() {
        if (spId == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Anda harus membuat surat penawaran terlebih dahulu.");
            alert.showAndWait();
            return;
        }

        if (mainController != null) {
            mainController.setSPIdBuffer(this.spId);
            mainController.loadPage("adddetailpenawaran");
        }
    }

    public void showDetailPenawaran(int spId) {
        System.out.println("✅ SP ID masuk ke AddNewPenawaranController: " + spId);
        this.spId = spId;

        vboxDetail.getChildren().clear();

        List<PenawaranModel.SPJobComponent> data = dao.getDetailBySP(spId);
        System.out.println("Total data dari view: " + data.size());

        if (data.isEmpty()) {
            vboxDetail.getChildren().add(new Label("Tidak ada rincian pekerjaan."));
            return;
        }

        btnTambahRincian.setDisable(spId == 0);

        // 🔥 KUNCI BIAR FULL WIDTH
        vboxDetail.setFillWidth(true);
        scrollRincian.setFitToWidth(true);

        var first = data.get(0);
        VBox suratBox = new VBox(6);
        suratBox.setPadding(new Insets(10));

        Label h1 = new Label("Perihal: " + first.perihal);
        Label h2 = new Label("Tanggal: " + first.tanggal_surat_penawaran);
        Label h3 = new Label("Perusahaan: " + first.namaPerusahaan);

        String headerStyle = "-fx-font-size: 14px;";
        h1.setStyle(headerStyle);
        h2.setStyle(headerStyle);
        h3.setStyle(headerStyle);

        suratBox.getChildren().addAll(h1, h2, h3, new Separator());
        vboxDetail.getChildren().add(suratBox);

        Map<Integer, VBox> jobGroup = new LinkedHashMap<>();

        for (var d : data) {

            if (!jobGroup.containsKey(d.jobId)) {

                // ================= JOB CONTAINER =================
                VBox jobContainer = new VBox(12);
                jobContainer.setPadding(new Insets(20));
                jobContainer.setMaxWidth(Double.MAX_VALUE);
                jobContainer.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: #dddddd;" +
                                "-fx-border-radius: 12;" +
                                "-fx-background-radius: 12;" +
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 4);"
                );

                // ================= HEADER =================
                VBox jobInfo = new VBox(6);

                String deskripsi = (d.deskripsiPekerjaan == null) ? "---" : d.deskripsiPekerjaan;

                Label p1 = new Label("🛠 Pekerjaan : " + d.namaPekerjaan);
                Label p2 = new Label("🔧 Mesin     : " + d.namaMesin);
                Label p3 = new Label("⚙  Spesifikasi: " + d.spesifikasiMesin);
                Label p4 = new Label("📄 Deskripsi : " + deskripsi);

                p1.setStyle("-fx-font-size: 14px;");
                p2.setStyle("-fx-font-size: 14px;");
                p3.setStyle("-fx-font-size: 14px;");
                p4.setStyle("-fx-font-size: 14px;");

                jobInfo.getChildren().addAll(p1, p2, p3, p4);

                // ================= ACTION BUTTONS =================
                Button btnEdit = new Button("Edit");
                btnEdit.setStyle(
                        "-fx-background-color: #2196f3;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 6;"
                );

                Button btnDelete = new Button("Delete");
                btnDelete.setStyle(
                        "-fx-background-color: #f44336;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 6;"
                );

                btnEdit.setPrefWidth(80);
                btnDelete.setPrefWidth(80);

                btnEdit.setPrefHeight(32);
                btnDelete.setPrefHeight(32);

                final int currentJobId = d.jobId;

                btnDelete.setOnAction(e -> {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Konfirmasi Hapus");
                    confirm.setHeaderText("Hapus pekerjaan ini?");
                    confirm.setContentText(
                            "Semua rincian pekerjaan ini akan ikut terhapus.\n" +
                                    "Tindakan ini tidak bisa dibatalkan."
                    );

                    confirm.showAndWait().ifPresent(result -> {
                        if (result == ButtonType.OK) {
                            handleDeleteJob(currentJobId);
                        }
                    });
                });

                int jobId = d.jobId;
                int currentSpId = this.spId;

                btnEdit.setOnAction(e -> {
                    if (mainController != null) {
                        mainController.setEditJobContext(currentSpId, jobId);
                        mainController.loadPage("adddetailpenawaran");
                    }
                });

                VBox actionBox = new VBox(6, btnEdit, btnDelete);
                actionBox.setAlignment(Pos.TOP_RIGHT);

                // ================= HEADER LAYOUT =================
                HBox header = new HBox(10, jobInfo, actionBox);
                header.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(jobInfo, Priority.ALWAYS);

                jobContainer.getChildren().addAll(header, new Separator());

                jobGroup.put(d.jobId, jobContainer);
            }

            // ================= DETAIL ROW =================
            VBox jobContainer = jobGroup.get(d.jobId);

            HBox row = new HBox(10);
            row.setPadding(new Insets(4, 0, 4, 15));

            Label left = new Label("• " + d.namaComponent + "  (Qty: " + d.qty + ")");
            Label right = new Label("Rp " + String.format("%,.0f", d.hargaKomponen));

            HBox.setHgrow(left, Priority.ALWAYS);
            right.setAlignment(Pos.CENTER_RIGHT);

            row.getChildren().addAll(left, right);
            jobContainer.getChildren().add(row);
        }

        vboxDetail.getChildren().addAll(jobGroup.values());
    }

    private void handleDeleteJob(int jobId) {
        try {
            Connection conn = DBConnection.getConnection();
            SPDetailDAO dao = new SPDetailDAO(conn);

            boolean success = dao.deleteJobById(jobId);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Pekerjaan berhasil dihapus.");
                alert.showAndWait();

                // 🔥 REFRESH HALAMAN YANG SAMA
                showDetailPenawaran(this.spId);
            } else {
                showError("Gagal menghapus pekerjaan.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("Terjadi kesalahan saat menghapus pekerjaan.");
        }
    }


    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}