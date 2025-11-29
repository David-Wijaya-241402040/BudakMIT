package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.java.com.project.app.config.DBConnection;
import javafx.scene.layout.Region;


import java.net.URL;
import java.sql.*;
import java.util.*;

public class PenawaranController implements Initializable {

    @FXML private VBox vboxSuratContainer; // container list card SP

    public void loadSP() throws SQLException {
        String sql = "SELECT * FROM view_surat_penawaran_detail ORDER BY sp_id ASC, job_id ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            Map<String, SPItem> spMap = new LinkedHashMap<>();

            while (rs.next()) {
                String noSP = rs.getString("no_sp");
                String perusahaan = rs.getString("nama_perusahaan");
                String pembuat = rs.getString("nama_pembuat");
                int jobId = rs.getInt("job_id");
                String pekerjaan = rs.getString("nama_pekerjaan");
                String namaMesin = rs.getString("nama_mesin");
                String spesifikasi = rs.getString("spesifikasi_mesin");
                double harga = rs.getDouble("harga_aktual");

                spMap.putIfAbsent(noSP, new SPItem(noSP, perusahaan, pembuat));

                if (jobId > 0 && pekerjaan != null) {
                    spMap.get(noSP).jobs.add(
                            new JobDetail(jobId, pekerjaan, namaMesin, spesifikasi, harga)
                    );
                }
            }

            vboxSuratContainer.getChildren().clear();

            for (SPItem sp : spMap.values()) {
                vboxSuratContainer.getChildren().add(createSPPane(sp));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error SQL loadSP(): " + e.getMessage());
        }
    }

    private AnchorPane createSPPane(SPItem sp) {
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
        int maxShow = 3;
        List<JobDetail> jobs = sp.jobs;

        for (int i = 0; i < Math.min(jobs.size(), maxShow); i++) {
            JobDetail jd = jobs.get(i);
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

        if (jobs.size() > maxShow) {
            VBox moreList = new VBox(6);
            moreList.setVisible(false);
            moreList.setManaged(false);

            for (int i = maxShow; i < jobs.size(); i++) {
                JobDetail jd = jobs.get(i);
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

            Button btnMore = new Button("Show More ▾");
            btnMore.setStyle("-fx-font-family: Georgia; -fx-font-size: 13px; -fx-background-radius: 6; -fx-border-radius: 6;");

            btnMore.setOnAction(e -> {
                boolean show = moreList.isVisible();
                moreList.setVisible(!show);
                moreList.setManaged(!show);
                btnMore.setText(!show ? "Show Less ▴" : "Show More ▾");
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

        return ap;
    }

    class SPItem {
        String noSP, perusahaan, pembuat;
        List<JobDetail> jobs = new ArrayList<>();
        SPItem(String noSP, String perusahaan, String pembuat) {
            this.noSP = noSP;
            this.perusahaan = perusahaan;
            this.pembuat = pembuat;
        }
    }

    class JobDetail {
        int jobId;
        String pekerjaan;
        String nama_mesin;
        String spesifikasi_mesin;
        double harga;

        JobDetail(int jobId, String pekerjaan, String namaMesin, String spesifikasiMesin, double harga) {
            this.jobId = jobId;
            this.pekerjaan = pekerjaan;
            this.nama_mesin = namaMesin;
            this.spesifikasi_mesin = spesifikasiMesin;
            this.harga = harga;
        }
    }

    private String formatRupiah(double value) {
        return String.format("%,.0f", value).replace(",", ".");
    }

    @Override
    public void initialize(URL loc, ResourceBundle res) {
        try {
            loadSP();
        } catch (Exception e) {
            System.err.println("❌ Gagal load SP di initialize(): " + e.getMessage());
        }
    }
}
