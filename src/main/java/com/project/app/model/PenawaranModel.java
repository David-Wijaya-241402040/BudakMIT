package main.java.com.project.app.model;

<<<<<<< HEAD
=======
import main.java.com.project.app.session.Session;

>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
import java.util.ArrayList;
import java.util.List;

public class PenawaranModel {

    public static class SPItem {
        public  int sp_id;
        public String noSP, perusahaan, pembuat;
        public List<JobDetail> jobs = new ArrayList<>();

<<<<<<< HEAD
        public SPItem(String noSP, String perusahaan, String pembuat) {
            this.noSP = noSP;
            this.perusahaan = perusahaan;
            this.pembuat = pembuat;
=======
        public SPItem(String noSP, String perusahaan) {
            this.noSP = noSP;
            this.perusahaan = perusahaan;
            this.pembuat = Session.currentUser.getNickname();
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
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
<<<<<<< HEAD
        public String noSP;
        public String perihal;
        public String tanggal;
=======
        public String no_sp;
        public String perihal;
        public String user_id;
        public String tanggal_surat_penawaran;
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
        public String namaPerusahaan;
        public int jobId;
        public String namaPekerjaan;
        public String namaMesin;
        public String spesifikasiMesin;
<<<<<<< HEAD
        public int componentId;
        public String namaComponent;
        public double hargaAcuan;

        public SPJobComponent(int spId,String noSP , String perihal, String tanggal, String namaPerusahaan,
                              int jobId, String namaPekerjaan, String namaMesin, String spesifikasiMesin,
                              int componentId, String namaComponent, double hargaAcuan) {
            this.spId = spId;
            this.noSP = noSP;
            this.perihal = perihal;
            this.tanggal = tanggal;
=======
        public String deskripsiPekerjaan;
        public int componentId;
        public String namaComponent;
        public int qty;
        public double hargaAcuan;
        public double hargaAktual;
        public double hargaKomponen;

        // Constructor yang sesuai dengan query DAO
        public SPJobComponent(int spId, String no_sp, String perihal, String user_id,
                              String tanggal_surat_penawaran, String namaPerusahaan,
                              int jobId, String namaPekerjaan, String namaMesin,
                              String spesifikasiMesin, String deskripsiPekerjaan, int componentId,
                              String namaComponent, int qty, double hargaAcuan, double hargaAktual, double hargaKomponen) {
            this.spId = spId;
            this.no_sp = no_sp;
            this.perihal = perihal;
            this.user_id = user_id;
            this.tanggal_surat_penawaran = tanggal_surat_penawaran;
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
            this.namaPerusahaan = namaPerusahaan;
            this.jobId = jobId;
            this.namaPekerjaan = namaPekerjaan;
            this.namaMesin = namaMesin;
            this.spesifikasiMesin = spesifikasiMesin;
<<<<<<< HEAD
            this.componentId = componentId;
            this.namaComponent = namaComponent;
            this.hargaAcuan = hargaAcuan;
        }
    }

}
=======
            this.deskripsiPekerjaan = deskripsiPekerjaan;
            this.componentId = componentId;
            this.namaComponent = namaComponent;
            this.qty = qty;
            this.hargaAcuan = hargaAcuan;
            this.hargaAktual = hargaAktual;
            this.hargaKomponen = hargaKomponen;
        }

        public int getSpId() { return spId; }
        public void setSpId(int spId) { this.spId = spId; }

        public String getNo_sp() { return no_sp; }
        public void setNo_sp(String no_sp) { this.no_sp = no_sp; }

        public String getPerihal() { return perihal; }
        public void setPerihal(String perihal) { this.perihal = perihal; }

        public String getUser_id() { return user_id; }
        public void setUser_id(String user_id) { this.user_id = user_id; }

        // PERHATIKAN: GETTER INI
        public String getTanggal_surat_penawaran() { return tanggal_surat_penawaran; }
        public void setTanggal_surat_penawaran(String tanggal_surat_penawaran) {
            this.tanggal_surat_penawaran = tanggal_surat_penawaran;
        }

        public String getNamaPerusahaan() { return namaPerusahaan; }
        public void setNamaPerusahaan(String namaPerusahaan) { this.namaPerusahaan = namaPerusahaan; }

        public int getJobId() { return jobId; }
        public void setJobId(int jobId) { this.jobId = jobId; }

        public String getNamaPekerjaan() { return namaPekerjaan; }
        public void setNamaPekerjaan(String namaPekerjaan) { this.namaPekerjaan = namaPekerjaan; }

        public String getNamaMesin() { return namaMesin; }
        public void setNamaMesin(String namaMesin) { this.namaMesin = namaMesin; }

        public String getSpesifikasiMesin() { return spesifikasiMesin; }
        public void setSpesifikasiMesin(String spesifikasiMesin) { this.spesifikasiMesin = spesifikasiMesin; }

        public String getDeskripsiPekerjaan() { return deskripsiPekerjaan; }
        public void setDeskripsiPekerjaan(String deskripsiPekerjaan) { this.deskripsiPekerjaan = deskripsiPekerjaan; }

        public int getComponentId() { return componentId; }
        public void setComponentId(int componentId) { this.componentId = componentId; }

        public String getNamaComponent() { return namaComponent; }
        public void setNamaComponent(String namaComponent) { this.namaComponent = namaComponent; }

        public double getHargaKomponen() { return hargaKomponen; }
        public void setHargaKomponen(double hargaKomponen) { this.hargaKomponen = hargaKomponen; }
    }
}
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
