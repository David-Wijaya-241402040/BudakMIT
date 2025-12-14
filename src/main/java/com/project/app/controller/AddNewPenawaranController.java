package main.java.com.project.app.controller;

import javafx.fxml.FXML;
<<<<<<< HEAD
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
=======
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
    @FXML
    private TextField searchField;
    @FXML
    private Button btnSearch;

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

            btnSearch.setOnAction(e -> handleSearch());

        } catch (Exception e) {
            System.out.println("❌ Koneksi DB gagal di initialize!");
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
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
=======
=======
    private void handleSearch() {
        String keyword = searchField.getText().trim();

        if (spId == 0) {
            showError("Anda harus membuat surat penawaran terlebih dahulu.");
            return;
        }

        List<PenawaranModel.SPJobComponent> filtered = dao.searchDetailBySPAndNamaPekerjaan(spId, keyword);
        showDetailPenawaran(filtered);
    }

>>>>>>> c2c9aeaaabe1c6b3bac92ac3ab866f179bb78efd
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

    public void showDetailPenawaran(List<PenawaranModel.SPJobComponent> data) {
        vboxDetail.getChildren().clear(); // Hapus dulu supaya tidak double

        if (data.isEmpty()) {
            vboxDetail.getChildren().add(new Label("Tidak ada rincian pekerjaan."));
            return;
        }

        // Ambil spId dari data pertama (supaya tombol tambah bisa aktif)
        this.spId = data.get(0).spId;
        btnTambahRincian.setDisable(this.spId == 0);

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
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
