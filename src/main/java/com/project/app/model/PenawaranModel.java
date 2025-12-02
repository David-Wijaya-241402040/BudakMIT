package main.java.com.project.app.model;

import java.util.ArrayList;
import java.util.List;

public class PenawaranModel {

    public static class SPItem {
        public  int sp_id;
        public String noSP, perusahaan, pembuat;
        public List<JobDetail> jobs = new ArrayList<>();

        public SPItem(String noSP, String perusahaan, String pembuat) {
            this.noSP = noSP;
            this.perusahaan = perusahaan;
            this.pembuat = pembuat;
        }
    }

    public static class JobDetail {
        public int jobId;
        public String pekerjaan;
        public String nama_mesin;
        public String spesifikasi_mesin;
        public double harga;

        public JobDetail(int jobId, String pekerjaan, String namaMesin, String spesifikasiMesin, double harga) {
            this.jobId = jobId;
            this.pekerjaan = pekerjaan;
            this.nama_mesin = namaMesin;
            this.spesifikasi_mesin = spesifikasiMesin;
            this.harga = harga;
        }
    }

    public static class SPJobComponent {
        public int spId;
        public String perihal;
        public String tanggal;
        public String namaPerusahaan;
        public int jobId;
        public String namaPekerjaan;
        public String namaMesin;
        public String spesifikasiMesin;
        public int componentId;
        public String namaComponent;
        public double hargaAcuan;

        public SPJobComponent(int spId, String perihal, String tanggal, String namaPerusahaan,
                              int jobId, String namaPekerjaan, String namaMesin, String spesifikasiMesin,
                              int componentId, String namaComponent, double hargaAcuan) {
            this.spId = spId;
            this.perihal = perihal;
            this.tanggal = tanggal;
            this.namaPerusahaan = namaPerusahaan;
            this.jobId = jobId;
            this.namaPekerjaan = namaPekerjaan;
            this.namaMesin = namaMesin;
            this.spesifikasiMesin = spesifikasiMesin;
            this.componentId = componentId;
            this.namaComponent = namaComponent;
            this.hargaAcuan = hargaAcuan;
        }
    }

}