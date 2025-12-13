package main.java.com.project.app.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jfx.incubator.scene.control.richtext.SelectionSegment;
import main.java.com.project.app.dao.PenawaranDAO;
import main.java.com.project.app.model.PenawaranModel;
import main.java.com.project.app.pdfwriter.PdfExporter;
import main.java.com.project.app.session.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PenawaranController implements Initializable, MainInjectable {

    private MainController mainController;
    @FXML private VBox vboxSuratContainer;
    @FXML private TextField searchField;
    @FXML private Button btnRefresh;
    @FXML private Button btnRefreshOwner;
    @FXML private Button btnAddPenawaran;
    private static Parent viewListPenawaran; // tampilan list SP awal
    private AddNewPenawaranController addNewPenawaranController;

    private final PenawaranDAO dao = new PenawaranDAO();

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadSP();
        } catch (Exception e) {
            System.err.println("❌ Error load SP: " + e.getMessage());
        }

        String role = Session.currentUser.getRoles();
        if(role.equals("owner")) {
            btnAddPenawaran.setDisable(true);
            btnAddPenawaran.setVisible(false);
            btnRefresh.setDisable(true);
            btnRefresh.setVisible(false);
            btnRefreshOwner.setVisible(true);
            btnRefreshOwner.setDisable(false);
        } else if (role.equals("staff")) {
            btnAddPenawaran.setDisable(false);
            btnAddPenawaran.setVisible(true);
            btnRefresh.setDisable(false);
            btnRefresh.setVisible(true);
            btnRefreshOwner.setVisible(false);
            btnRefreshOwner.setDisable(true);
        }
    }

    public void loadSP() throws SQLException {
        Map<String, PenawaranModel.SPItem> spMap = dao.loadSP();

        vboxSuratContainer.getChildren().clear();

        for (PenawaranModel.SPItem sp : spMap.values()) {
            vboxSuratContainer.getChildren().add(createSPPane(sp));
        }
    }

    private AnchorPane createSPPane(PenawaranModel.SPItem sp) {
        AnchorPane ap = new AnchorPane();
        ap.setPrefWidth(1000);
        ap.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #c8c8c8; -fx-border-width: 1;");
        ap.setPadding(new javafx.geometry.Insets(10));

        Label lblSP = new Label("No SP: " + sp.noSP);
        lblSP.setStyle("-fx-font-family: Georgia; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblPerusahaan = new Label("Perusahaan: " + (sp.perusahaan == null ? "-" : sp.perusahaan));
        lblPerusahaan.setStyle("-fx-font-family: Georgia; -fx-font-size: 14px;");

        String username = Session.currentUser.getNickname();

        Label lblPembuat = new Label("Dibuat oleh: " + username);
        lblPembuat.setStyle("-fx-font-family: Georgia; -fx-font-size: 14px;");

        VBox jobContainer = new VBox(6);
        jobContainer.setStyle("-fx-padding: 5 0 0 0;");

        double total = 0;
        int maxShow = 2;
        List<PenawaranModel.JobDetail> jobs = sp.jobs;

        if (jobs.isEmpty()) {
            Label emptyLabel = new Label("x. Belum ada pekerjaan");
            emptyLabel.setWrapText(true);
            emptyLabel.setMaxWidth(750);
            emptyLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 13px; -fx-font-style: italic; -fx-text-fill: #888;");
            jobContainer.getChildren().add(emptyLabel);
        }
        // ==========================

        // Jika ada job → tampil seperti biasa
        else {
            for (int i = 0; i < Math.min(jobs.size(), maxShow); i++) {
                PenawaranModel.JobDetail jd = jobs.get(i);
                Label jobLabel = new Label(
                        (i + 1) + ". " + jd.pekerjaan + " | "
                                + (jd.nama_mesin == null ? "-" : jd.nama_mesin) + " | "
                                + (jd.spesifikasi_mesin == null ? "-" : jd.spesifikasi_mesin)
                );
                jobLabel.setWrapText(true);
                jobLabel.setMaxWidth(750);
                jobLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 13px;");
                jobContainer.getChildren().add(jobLabel);
                total += jd.harga;
            }
        }

        if (jobs.size() > maxShow) {
            VBox moreList = new VBox(6);
            moreList.setVisible(false);
            moreList.setManaged(false);

            for (int i = maxShow; i < jobs.size(); i++) {
                PenawaranModel.JobDetail jd = jobs.get(i);
                Label jobLabel = new Label(
                        (i + 1) + ". " + jd.pekerjaan + " | "
                                + (jd.nama_mesin == null ? "-" : jd.nama_mesin) + " | "
                                + (jd.spesifikasi_mesin == null ? "-" : jd.spesifikasi_mesin)
                );
                jobLabel.setWrapText(true);
                jobLabel.setMaxWidth(750);
                jobLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 13px;");
                moreList.getChildren().add(jobLabel);
                total += jd.harga;
            }

            Button btnMore = new Button("Show More");
            btnMore.setStyle(
                    "-fx-font-family: Georgia; " +
                            "-fx-font-size: 13px; " +
                            "-fx-background-color: linear-gradient(to bottom, #0edbb8, #0fbda1); " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 8; " +
                            "-fx-padding: 6 18 6 18; " +
                            "-fx-border-color: #0aa98f; " +
                            "-fx-border-width: 1.2; " +
                            "-fx-border-radius: 8; " +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(15,189,161,0.4), 8, 0.3, 0, 2);"
            );

// efek hover & klik biar ada feedback warna
            btnMore.setOnMouseEntered(e -> btnMore.setStyle(btnMore.getStyle().replace("#0fbda1", "#0aa98f")));
            btnMore.setOnMousePressed(e -> btnMore.setStyle(btnMore.getStyle().replace("#0aa98f", "#08947d")));
            btnMore.setOnMouseExited(e -> btnMore.setStyle(btnMore.getStyle() + "-fx-background-color:#0fbda1;"));
            btnMore.setOnMouseReleased(e -> btnMore.setStyle(btnMore.getStyle() + "-fx-background-color:#0aa98f;"));


            btnMore.setOnAction(e -> {
                boolean show = moreList.isVisible();
                moreList.setVisible(!show);
                moreList.setManaged(!show);
                btnMore.setText(!show ? "Show Less" : "Show More");
            });

            jobContainer.getChildren().addAll(moreList, btnMore);
        }

        Label lblTotal = new Label("TOTAL: Rp " + formatRupiah(total));
        lblTotal.setStyle("-fx-font-family: Georgia; -fx-font-size: 15px; -fx-font-weight: bold;");

        AnchorPane.setTopAnchor(lblSP, 10.0);
        AnchorPane.setLeftAnchor(lblSP, 15.0);

        AnchorPane.setTopAnchor(lblPerusahaan, 45.0);
        AnchorPane.setLeftAnchor(lblPerusahaan, 15.0);

        AnchorPane.setTopAnchor(lblPembuat, 70.0);
        AnchorPane.setLeftAnchor(lblPembuat, 15.0);

        AnchorPane.setTopAnchor(jobContainer, 100.0);
        AnchorPane.setLeftAnchor(jobContainer, 15.0);

        AnchorPane.setTopAnchor(lblTotal, 10.0);
        AnchorPane.setRightAnchor(lblTotal, 15.0);

        ap.getChildren().addAll(lblSP, lblPerusahaan, lblPembuat, jobContainer, lblTotal);
        ap.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // Tombol untuk buka detail penawaran (pindah scene/halaman)
        Button btnBuka = new Button("Buka Penawaran");
        btnBuka.setStyle(
                "-fx-font-family: Georgia; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-color: linear-gradient(to bottom, #4a90e2, #357ABD); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 6 18 6 18; " +
                        "-fx-border-color: #2C5FA8; " +
                        "-fx-border-width: 1.2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(53,122,189,0.4), 8, 0.3, 0, 2);"
        );

        // Hover effect
        btnBuka.setOnMouseEntered(e -> btnBuka.setStyle(btnBuka.getStyle().replace("#357ABD", "#2C5FA8")));
        btnBuka.setOnMouseExited(e -> btnBuka.setStyle(btnBuka.getStyle().replace("#2C5FA8", "#357ABD")));


        // Action pindah ke scene detail
        btnBuka.setOnAction(e -> {
            System.out.println("SP ID yang diklik: " + sp.sp_id); // cek di console dulu
            mainController.setSPIdBuffer(sp.sp_id); // simpen dulu
            mainController.loadPage("addnewpenawaran"); // baru pindah scene
        });

        Button btnDelete = new Button("Delete Penawaran");
        btnDelete.setStyle(
                "-fx-font-family: Georgia; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-color: linear-gradient(to bottom, #e74c3c, #c0392b); " + // merah
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 6 18 6 18; " +
                        "-fx-border-color: #a93226; " +
                        "-fx-border-width: 1.2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(192,57,43,0.4), 8, 0.3, 0, 2);"
        );


        btnDelete.setOnMouseEntered(e -> btnDelete.setStyle(btnDelete.getStyle().replace("#c0392b", "#922b21")));
        btnDelete.setOnMouseExited(e -> btnDelete.setStyle(btnDelete.getStyle().replace("#922b21", "#c0392b")));


        btnDelete.setOnAction(e -> {
            try {
                boolean success = dao.deleteSP(sp.sp_id);
                if(success){
                    System.out.println("SP berhasil dihapus: " + sp.sp_id);
                    showAlert("INFORMATION", "Surat Penawaran berhasil Dihapus");
                    goRefresh(); // refresh list setelah delete
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.err.println("Gagal menghapus SP: " + sp.sp_id);
            }
        });


        Button btnExport = new Button("Export Penawaran");
        btnExport.setStyle(
                "-fx-font-family: Georgia; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-color: linear-gradient(to bottom, #2ecc71, #27ae60); " + // hijau
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 6 18 6 18; " +
                        "-fx-border-color: #1e8449; " +
                        "-fx-border-width: 1.2; " +
                        "-fx-border-radius: 8; " +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(39,174,96,0.4), 8, 0.3, 0, 2);"
        );


        btnExport.setOnMouseEntered(e -> btnExport.setStyle(btnExport.getStyle().replace("#27ae60", "#1e8449")));
        btnExport.setOnMouseExited(e -> btnExport.setStyle(btnExport.getStyle().replace("#1e8449", "#27ae60")));


        btnExport.setOnAction(e -> {
            System.out.println("Export SP ID: " + sp.sp_id);
            try {
                // Panggil PDF generator dengan SP ID yang diklik
                PdfExporter.generatePdf(sp.sp_id);
                showAlert("INFORMATION", "PDF berhasil di-export untuk SP ID: " + sp.sp_id);
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("ERROR", "Gagal export PDF SP ID: " + sp.sp_id);
            }
        });


        String role = Session.currentUser.getRoles();
        VBox buttonBox = new VBox(6, btnBuka, btnExport, btnDelete);
        buttonBox.setFillWidth(true); // otomatis button ikut lebar VBox
        btnBuka.setMaxWidth(Double.MAX_VALUE);
        btnDelete.setMaxWidth(Double.MAX_VALUE);
        btnExport.setMaxWidth(Double.MAX_VALUE);

        if(role.equals("owner")) {
            btnDelete.setVisible(false);
            btnDelete.setDisable(true);
        } else if (role.equals("staff")) {
            btnDelete.setVisible(true);
            btnDelete.setDisable(false);
        }

        AnchorPane.setTopAnchor(buttonBox, 45.0);
        AnchorPane.setRightAnchor(buttonBox, 15.0);

        ap.getChildren().add(buttonBox);
        return ap;
    }

    private String formatRupiah(double value) {
        return String.format("%,.0f", value).replace(",", ".");
    }

    @FXML
    public void goAdd() {
        mainController.handleAddPenawaran();
    }


    @FXML
    public void goSearch() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            try {
                loadSP();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        try {
            Map<String, PenawaranModel.SPItem> result = dao.searchSP(keyword);

            vboxSuratContainer.getChildren().clear();

            if (result.isEmpty()) {
                Label lbl = new Label("Tidak ada hasil untuk: " + keyword);
                lbl.setStyle("-fx-font-family: Georgia; -fx-font-size: 14px; -fx-text-fill: gray;");
                vboxSuratContainer.getChildren().add(lbl);
                return;
            }

            for (PenawaranModel.SPItem sp : result.values()) {
                vboxSuratContainer.getChildren().add(createSPPane(sp));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goRefresh() {
        try {
            loadSP();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void showAlert(String title, String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
            alert.setTitle(title);
            alert.showAndWait();
        });
    }
}