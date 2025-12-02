package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.java.com.project.app.dao.PenawaranDAO;
import main.java.com.project.app.model.PenawaranModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PenawaranController implements Initializable, MainInjectable {

    private MainController mainController;
    @FXML private VBox vboxSuratContainer;
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

    }

    private void loadSP() throws SQLException {
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

        Label lblPembuat = new Label("Dibuat oleh: " + (sp.pembuat == null ? "-" : sp.pembuat));
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
//            mainController.setSPIdBuffer(sp.sp_id); // simpen dulu
//            mainController.loadPage("addnewpenawaran"); // baru pindah scene
        });



        // Set posisi tombol di kanan
        AnchorPane.setTopAnchor(btnBuka, 45.0);
        AnchorPane.setRightAnchor(btnBuka, 15.0);
        ap.getChildren().add(btnBuka);
        return ap;
    }




    private String formatRupiah(double value) {
        return String.format("%,.0f", value).replace(",", ".");
    }
}