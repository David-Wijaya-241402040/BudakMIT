package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.SPDetailDAO;
import main.java.com.project.app.model.PenawaranModel;

import java.net.URL;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddNewPenawaranController implements Initializable, MainInjectable {

    @FXML private VBox vboxDetail;
    @FXML private Label lblEmpty;
    private MainController mainController;
    private SPDetailDAO dao;
    private String noSP;


    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadSP();

            if (noSP != null && !noSP.isEmpty()) {
                System.out.println("🚀 NO SP aman, load detail...");
                showDetailPenawaran(noSP);
            } else {
                System.out.println("⚠ NO SP masih null saat initialize!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error load SP: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void loadSP() {
        try {
            Connection conn = DBConnection.getConnection();
            this.dao = new SPDetailDAO(conn);
            System.out.println("✅ DAO berhasil di-initialize!");

            // 🔥 cek nilai NoSP
            System.out.println("📌 NoSP masuk ke controller = [" + noSP + "]");

            if (noSP != null && !noSP.isBlank()) {
                System.out.println("🚀 NoSP aman, langsung load tanpa parsing");
                showDetailPenawaran(noSP);
            } else {
                System.out.println("⚠ NoSP masih kosong/null, gak load data!");
            }

        } catch (Exception e) {
            System.out.println("❌ DAO gagal di initialize!");
            e.printStackTrace();
        }
    }

    public void showDetailPenawaran(String nosurat) {
        this.noSP = nosurat;
        vboxDetail.getChildren().clear();

        System.out.println("🔎 showDetailPenawaran pakai NO SP = " + noSP);

        List<PenawaranModel.SPJobComponent> data = dao.getDetailBySP(noSP);

        // HANDLE empty (belum ada jobs)
        if (data == null || data.isEmpty()) {
            lblEmpty.setMaxWidth(Double.MAX_VALUE);
            lblEmpty.setStyle("""
                -fx-background-color:white;
                -fx-border-color:#c8c8c8;
                -fx-border-width:1;
                -fx-padding:20;
                -fx-background-radius:12;
                -fx-border-radius:12;
            """);
            vboxDetail.getChildren().add(lblEmpty);
            lblEmpty.setVisible(true);
            lblEmpty.setManaged(true);
            return;
        }

        // HILANGKAN placeholder
        lblEmpty.setVisible(false);
        lblEmpty.setManaged(false);

        var first = data.get(0);

        // BOX INFORMASI SURAT (Full width)
        VBox suratBox = new VBox(8);
        suratBox.setPadding(new Insets(12));
        suratBox.setMaxWidth(Double.MAX_VALUE);
        suratBox.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
        """);

        suratBox.getChildren().addAll(
                new Label("📄 Perihal: " + first.perihal),
                new Label("📅 Tanggal: " + first.tanggal),
                new Label("🏢 Perusahaan: " + first.namaPerusahaan),
                new Separator()
        );

        vboxDetail.getChildren().add(suratBox);

        // GROUPING JOBS
        Map<Integer, VBox> jobGroup = new LinkedHashMap<>();

        for (var d : data) {
            jobGroup.putIfAbsent(d.jobId, new VBox(6));
            VBox jobBox = jobGroup.get(d.jobId);

            if (jobBox.getChildren().isEmpty()) {
                jobBox.setPadding(new Insets(12));
                jobBox.setMaxWidth(Double.MAX_VALUE);
                jobBox.setStyle("-fx-background-radius:12; -fx-border-radius:12; -fx-border-color:#e0e0e0; -fx-border-width:1;");

                jobBox.getChildren().addAll(
                        new Label("🛠 Pekerjaan: " + (d.namaPekerjaan == null ? "Belum ada pekerjaan" : d.namaPekerjaan)),
                        new Label("🔧 Mesin: " + (d.namaMesin == null ? "Belum ada mesin" : d.namaMesin)),
                        new Label("⚙ Spesifikasi: " + (d.spesifikasiMesin == null ? "Belum ada spesifikasi" : d.spesifikasiMesin)),
                        new Label("📦 Komponen:"),
                        new Separator()
                );
            }

            // ROW KOMPONEN + BUTTON EDIT DELETE
            HBox row = new HBox(12);
            row.setMaxWidth(Double.MAX_VALUE);
            row.setPadding(new Insets(6, 10, 6, 20));
            row.setStyle("-fx-background-radius:8; -fx-border-radius:8; -fx-border-color:#ddd; -fx-border-width:1;");

            Label lblKomponen = new Label("• " + (d.namaComponent == null ? "Belum ada komponen" : d.namaComponent));
            Label lblHarga = new Label(d.hargaAcuan == 0 ? "-" : "Rp " + String.format("%,.0f", d.hargaAcuan));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button btnEdit = new Button("✏");
            btnEdit.setStyle("-fx-background-color:transparent;");
            btnEdit.setCursor(Cursor.HAND);
            btnEdit.setOnAction(e -> handleEditJob(d));


            Button btnDelete = new Button("🗑");
            btnDelete.setStyle("-fx-background-color:transparent;");
            btnDelete.setCursor(Cursor.HAND);
            btnDelete.setOnAction(e -> handleDelete(row, d));

            row.getChildren().addAll(lblKomponen, spacer, lblHarga, btnEdit, btnDelete);
            jobBox.getChildren().add(row);
        }

        // RENDER hasil grouping ke UI
        vboxDetail.setSpacing(16);
        jobGroup.values().forEach(v -> vboxDetail.getChildren().add(v));
    }

    private void handleEdit(PenawaranModel.SPJobComponent d) {
        TextInputDialog dialog = new TextInputDialog(d.namaComponent);
        dialog.setTitle("Edit Komponen");
        dialog.setHeaderText("Ubah nama komponen");
        dialog.setContentText("Nama baru:");

        dialog.showAndWait().ifPresent(namaBaru -> {
            dao.updateComponent(d.componentId, namaBaru);
            showDetailPenawaran(noSP); // refresh tampilan
        });
    }

    private void handleDelete(HBox row, PenawaranModel.SPJobComponent d) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hapus Komponen");
        alert.setHeaderText("Yakin mau hapus?");
        alert.setContentText(d.namaComponent);

        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                dao.deleteComponent(d.componentId);

                // Delete row dari VBox parent
                Node parent = row.getParent();
                if (parent instanceof VBox vb) {
                    vb.getChildren().remove(row);
                }

                System.out.println("🗑 Berhasil hapus komponen: " + d.namaComponent);
            }
        });
    }

    private void handleEditJob(PenawaranModel.SPJobComponent d) {
        System.out.println("📝 Edit pekerjaan yang diklik, jobId = " + d.jobId);

        // Simpan jobId ke buffer supaya bisa diambil di controller halaman edit


        // pindah ke halaman edit pekerjaan
        mainController.loadPage("editpekerjaan");
    }

    public void setNoSP(String nosurat) {
        this.noSP = nosurat;
        System.out.println("📌 NO SP diterima di AddNewPenawaranController: " + noSP);
    }

}
