package main.java.com.project.app.model;

import main.java.com.project.app.formatter.ActivityFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAktivitas {
    private int logId;
    private String namaTabel;
    private String aksi;
    private String dataLama;
    private String dataBaru;
    private int userId;
    private String waktu;

    public LogAktivitas(int logId, String namaTabel, String aksi, String dataLama, String dataBaru, int userId, String waktu) {
        this.logId = logId;
        this.namaTabel = namaTabel;
        this.aksi = aksi;
        this.dataLama = dataLama;
        this.dataBaru = dataBaru;
        this.userId = userId;
        this.waktu = waktu;
    }

    // getters
    public int getLogId() { return logId; }
    public String getNamaTabel() { return namaTabel; }
    public String getAksi() { return aksi; }
    public String getDataLama() { return dataLama; }
    public String getDataBaru() { return dataBaru; }
    public int getUserId() { return userId; }
    public String getWaktu() { return waktu; }

    // convenience: produce display text for "Aktivitas" column
    public String getAktivitasDisplay() {
        StringBuilder sb = new StringBuilder();
        if (namaTabel != null) sb.append(namaTabel);
        if (aksi != null) sb.append(" - ").append(aksi);
        if (dataBaru != null && !dataBaru.isEmpty()) sb.append(" (").append(dataBaru).append(")");
        return sb.toString();
    }

    public String getAktivitasRapi() {
        return ActivityFormatter.format(namaTabel, aksi, dataBaru);
    }
}
