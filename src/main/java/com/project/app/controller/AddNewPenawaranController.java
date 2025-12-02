package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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

    public void setSPId(int spId) {
        this.spId = spId;
        System.out.println("SP ID berhasil diterima: " + spId);
    }

    public void initialize() {
        try {
            Connection conn = DBConnection.getConnection();
            this.dao = new SPDetailDAO(conn);
        } catch (Exception e) {
            System.out.println("❌ Koneksi DB gagal di initialize!");
            e.printStackTrace();
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

        var first = data.get(0);
        VBox suratBox = new VBox(6);
        suratBox.setPadding(new Insets(10));

        suratBox.getChildren().addAll(
                new Label("Perihal: " + first.perihal),
                new Label("Tanggal: " + first.tanggal),
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
                    new Label("Rp " + String.format("%,.0f", d.hargaAcuan))
            );

            jobBox.getChildren().add(row);
        }

        for (VBox jobBox : jobGroup.values()) {
            vboxDetail.getChildren().add(jobBox);
        }
    }
}