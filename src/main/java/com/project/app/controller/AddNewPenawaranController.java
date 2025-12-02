package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.SPDetailDAO;
import main.java.com.project.app.model.PenawaranModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddNewPenawaranController {
    @FXML VBox vboxDetail;

    private SPDetailDAO dao;
    private int spId;
    private MainController mainController;
    @FXML
    Button btnTambahRincian;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setSPId(int spId) {
        this.spId = spId;
        System.out.println("SP ID berhasil diterima: " + spId);
    }

    @FXML
    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            this.dao = new SPDetailDAO(conn);

            // Atur event handler untuk tombol Tambah Rincian
            btnTambahRincian.setOnAction(event -> handleTambahRincian());

        } catch (Exception e) {
            System.out.println("❌ Koneksi DB gagal di initialize!");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleTambahRincian() {
        if (spId == 0) {
            // Tampilkan pesan bahwa harus membuat surat penawaran dulu
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
        this.spId = spId; // simpen ke variable lokal

        vboxDetail.getChildren().clear();

        List<PenawaranModel.SPJobComponent> data = dao.getDetailBySP(spId);
        System.out.println("Total data dari view: " + data.size());

        if (data.isEmpty()) {
            vboxDetail.getChildren().add(new Label("Tidak ada rincian pekerjaan."));
            return;
        }

        if (spId == 0) {
            btnTambahRincian.setDisable(true);
            btnTambahRincian.setTooltip(new Tooltip("Buat surat penawaran terlebih dahulu"));
        } else {
            btnTambahRincian.setDisable(false);
        }

        var first = data.get(0);
        VBox suratBox = new VBox(6);
        suratBox.setPadding(new Insets(10));

        suratBox.getChildren().addAll(
                new Label("Perihal: " + first.perihal),
                new Label("Tanggal: " + first.tanggal_surat_penawaran),
                new Label("Perusahaan: " + first.namaPerusahaan),
                new Separator()
        );

        vboxDetail.getChildren().add(suratBox);

        Map<Integer, VBox> jobGroup = new LinkedHashMap<>();
        for (var d : data) {
            jobGroup.putIfAbsent(d.jobId, new VBox(5));
            VBox jobBox = jobGroup.get(d.jobId);

            if (jobBox.getChildren().isEmpty()) {
                jobBox.setPadding(new Insets(8));
                jobBox.getChildren().addAll(
                        new Label("🛠 Pekerjaan: " + d.namaPekerjaan),
                        new Label("🔧 Mesin: " + d.namaMesin),
                        new Label("⚙ Spek: " + d.spesifikasiMesin),
                        new Separator()
                );
            }

            HBox row = new HBox(10);
            row.setPadding(new Insets(4, 0, 4, 15));
            row.getChildren().addAll(
                    new Label("• " + d.namaComponent),
                    new Label("Rp " + String.format("%,.0f", d.hargaKomponen))
            );

            jobBox.getChildren().add(row);
        }

        for (VBox jobBox : jobGroup.values()) {
            vboxDetail.getChildren().add(jobBox);
        }
    }
}

