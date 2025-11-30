package main.java.com.project.app.model;

import java.util.ArrayList;
import java.util.List;

public class PenawaranModel {

    public static class SPItem {
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

}
