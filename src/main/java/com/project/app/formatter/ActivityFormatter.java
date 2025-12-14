package main.java.com.project.app.formatter;

import java.util.HashMap;
import java.util.Map;

public class ActivityFormatter {

    /**
     * Entry utama untuk generate kalimat rapi.
     */
    public static String format(String table, String action, String raw) {
        if (table == null || action == null) return "Aktivitas tidak diketahui";

        table = table.trim().toLowerCase();
        action = action.trim().toLowerCase();

        Map<String, String> data = parse(raw);

        switch (table) {

            case "surat_penawaran":
                return formatSuratPenawaran(action, data);

            case "tagihan":
                return formatTagihan(action, data);

            case "company":
            case "perusahaan":
                return formatCompany(action, data);

            case "jobs":
            case "job":
                return formatJob(action, data);

            case "detail_pekerjaan":
                return formatDetailPekerjaan(action, data);

            default:
                return action + " " + table + " (" + raw + ")";
        }
    }

    /**
     * PARSER MANUAL super simple (tanpa org.json)
     * Format yang didukung: {"key":"value", "key2":"value2"}
     */
    private static Map<String, String> parse(String raw) {
        Map<String, String> map = new HashMap<>();
        if (raw == null) return map;

        raw = raw.trim();

        // CASE 1: JSON-like simple format
        if (raw.startsWith("{") && raw.endsWith("}")) {
            raw = raw.substring(1, raw.length() - 1); // remove { }

            String[] pairs = raw.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (String p : pairs) {
                String[] kv = p.split(":", 2);
                if (kv.length == 2) {
                    String key = kv[0].trim().replace("\"", "");
                    String value = kv[1].trim().replace("\"", "");
                    map.put(key, value);
                }
            }
            return map;
        }

        // CASE 2: Format "Job ID: 23 | No.SP: SP-0005"
        if (raw.contains("|")) {
            String[] sections = raw.split("\\|");
            for (String s : sections) {
                if (s.contains(":")) {
                    String[] kv = s.split(":", 2);
                    map.put(kv[0].trim(), kv[1].trim());
                }
            }
            return map;
        }

        // If nothing matched, return empty
        return map;
    }

    // ============================
    //  FORMATTERS PER TABLE
    // ============================

    private static String formatSuratPenawaran(String action, Map<String, String> d) {
        String noSP = d.getOrDefault("No_SP", "?");

        if (action.contains("insert"))
            return "Membuat Surat Penawaran Baru (" + noSP + ")";

        if (action.contains("update"))
            return "Mengupdate Surat Penawaran (" + noSP + ")";

        if (action.contains("delete"))
            return "Menghapus Surat Penawaran (" + noSP + ")";

        return action + " Surat Penawaran (" + noSP + ")";
    }

    private static String formatTagihan(String action, Map<String, String> d) {
        String noTag = d.getOrDefault("Tagihan_ID", d.getOrDefault("no_tag", "?"));

        if (action.contains("insert"))
            return "Membuat Tagihan Baru (" + noTag + ")";

        if (action.contains("update"))
            return "Mengupdate Tagihan (" + noTag + ")";

        if (action.contains("delete"))
            return "Menghapus Tagihan (" + noTag + ")";

        return action + " Tagihan (" + noTag + ")";
    }

    private static String formatCompany(String action, Map<String, String> d) {
        String nama = d.getOrDefault("Nama_Perusahaan", d.getOrDefault("nama_perusahaan", "?"));

        if (action.contains("insert"))
            return "Menambahkan Perusahaan Baru (" + nama + ")";

        if (action.contains("update"))
            return "Mengupdate Perusahaan (" + nama + ")";

        if (action.contains("delete"))
            return "Menghapus Perusahaan (" + nama + ")";

        return action + " Perusahaan (" + nama + ")";
    }

    private static String formatJob(String action, Map<String, String> d) {
        String noSP = d.getOrDefault("No_SP", "?");
        String nama = d.getOrDefault("Nama_Pekerjaan", d.getOrDefault("nama_pekerjaan", "?"));

        if (action.contains("insert"))
            return "Menambahkan Job Baru (" + nama + " - " + noSP + ")";

        if (action.contains("update"))
            return "Mengupdate Job (" + nama + " - " + noSP + ")";

        if (action.contains("delete"))
            return "Menghapus Job (" + nama + " - " + noSP + ")";

        return action + " Job (" + nama + " - " + noSP + ")";
    }

    private static String formatDetailPekerjaan(String action, Map<String, String> d) {
        String noSP = d.getOrDefault("No_SP", "?");
        String komponen = d.getOrDefault("Komponen", "?");

        if (action.contains("insert"))
            return "Menambahkan Detail Pekerjaan (Komponen " + komponen + ", " + noSP + ")";

        if (action.contains("update"))
            return "Mengupdate Detail Pekerjaan (Komponen " + komponen + ", " + noSP + ")";

        if (action.contains("delete"))
            return "Menghapus Detail Pekerjaan (Komponen " + komponen + ", " + noSP + ")";

        return action + " Detail Pekerjaan (Komponen " + komponen + ", " + noSP + ")";
    }
}
