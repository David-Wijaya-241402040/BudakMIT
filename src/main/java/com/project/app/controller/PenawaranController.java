package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import main.java.com.project.app.dao.PenawaranDAO;
import main.java.com.project.app.model.PenawaranModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PenawaranController implements Initializable {

    @FXML private VBox vboxSuratContainer;

    private final PenawaranDAO dao = new PenawaranDAO();

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
        ap.setPadding(new Insets(10));

        Label lblSP = new Label("No SP: " + sp.noSP);
        lblSP.setStyle("-fx-font-family: Georgia; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblPerusahaan = new Label("Perusahaan: " + (sp.perusahaan == null ? "-" : sp.perusahaan));
        lblPerusahaan.setStyle("-fx-font-family: Georgia; -fx-font-size: 14px;");

        Label lblPembuat = new Label("Dibuat oleh: " + (sp.pembuat == null ? "-" : sp.pembuat));
        lblPembuat.setStyle("-fx-font-family: Georgia; -fx-font-size: 14px;");

        VBox jobContainer = new VBox(6);

        double total = 0;
        List<PenawaranModel.JobDetail> jobs = sp.jobs;
        int maxShow = 3;

        // Show first 3 jobs
        for (int i = 0; i < Math.min(jobs.size(), maxShow); i++) {
            PenawaranModel.JobDetail jd = jobs.get(i);
            total += addJobLabel(jobContainer, jd, i);
        }

        // Show More / Show Less section
        if (jobs.size() > maxShow) {
            VBox moreList = new VBox(6);
            moreList.setVisible(false);
            moreList.setManaged(false);

            for (int i = maxShow; i < jobs.size(); i++) {
                PenawaranModel.JobDetail jd = jobs.get(i);
                total += addJobLabel(moreList, jd, i);
            }

            Button btnMore = new Button("Show More ▾");
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

    private double addJobLabel(VBox container, PenawaranModel.JobDetail jd, int index) {
        Label jobLabel = new Label(
                (index + 1) + ". " + jd.pekerjaan + " | "
                        + (jd.nama_mesin == null ? "-" : jd.nama_mesin) + " | "
                        + (jd.spesifikasi_mesin == null ? "-" : jd.spesifikasi_mesin)
        );

        jobLabel.setWrapText(true);
        jobLabel.setMaxWidth(750);
        jobLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 13px;");
        container.getChildren().add(jobLabel);

        return jd.harga;
    }

    private String formatRupiah(double value) {
        return String.format("%,.0f", value).replace(",", ".");
    }
}
