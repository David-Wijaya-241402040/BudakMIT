package main.java.com.project.app.dao;

import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.model.PdfSPModel;
import main.java.com.project.app.model.PdfJobModel;
import main.java.com.project.app.model.PdfItemModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PdfSPDAO {
    private final Connection conn;

    public PdfSPDAO() throws Exception {
        this.conn = DBConnection.getConnection();
    }

    public PdfSPModel getPdfSPById(int spId) throws Exception {
        PdfSPModel pdf = new PdfSPModel();

        // ===== Header =====
        String sqlHeader = "SELECT sp.no_sp, sp.tanggal_surat_penawaran, sp.perihal, " +
                "c.nama_perusahaan, c.alamat_perusahaan " +
                "FROM surat_penawaran sp " +
                "LEFT JOIN perusahaan c ON sp.company_id = c.company_id " +
                "WHERE sp.sp_id = ?";
        PreparedStatement ps = conn.prepareStatement(sqlHeader);
        ps.setInt(1, spId);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            pdf.setNoSP(rs.getString("no_sp"));

            java.sql.Date tanggalSql = rs.getDate("tanggal_surat_penawaran");
            pdf.setTanggal(tanggalSql != null ? tanggalSql.toLocalDate() : LocalDate.now());

            pdf.setPerihal(rs.getString("perihal"));
            pdf.setNamaPerusahaan(rs.getString("nama_perusahaan"));
            pdf.setAlamatPerusahaan(rs.getString("alamat_perusahaan"));
        }

        // ===== Jobs =====
        String sqlJobs = "SELECT job_id, nama_pekerjaan FROM jobs WHERE sp_id = ?";
        ps = conn.prepareStatement(sqlJobs);
        ps.setInt(1, spId);
        rs = ps.executeQuery();

        List<PdfJobModel> jobs = new ArrayList<>();
        while(rs.next()) {
            PdfJobModel job = new PdfJobModel();
            job.setNamaPekerjaan(rs.getString("nama_pekerjaan"));

            // Ambil items, join ke component untuk nama_component
            String sqlItems = "SELECT dp.qty, dp.harga_aktual AS harga, c.nama_component AS nama_item " +
                    "FROM detail_pekerjaan dp " +
                    "LEFT JOIN komponen c ON dp.component_id = c.component_id " +
                    "WHERE dp.job_id = ?";
            PreparedStatement psItem = conn.prepareStatement(sqlItems);
            psItem.setLong(1, rs.getLong("job_id"));
            ResultSet rsItem = psItem.executeQuery();

            List<PdfItemModel> items = new ArrayList<>();
            while(rsItem.next()) {
                PdfItemModel item = new PdfItemModel();
                item.setNamaItem(rsItem.getString("nama_item"));
                item.setQty(rsItem.getInt("qty"));
                item.setHarga(rsItem.getDouble("harga"));
                items.add(item);
            }
            job.setItems(items);
            jobs.add(job);
        }

        pdf.setJobs(jobs);
        return pdf;
    }
}
