package main.java.com.project.app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            // Kalau belum ada atau sudah tertutup → buka ulang koneksi
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/budakmit",
                        "root",
                        ""
                );
                System.out.println("✅ DB Connected / Reconnected");
            }
        } catch (SQLException e) {
            System.out.println("❌ DB Connection Failed");
            e.printStackTrace();
        }
        return connection;
    }

    // ⚠ HAPUS atau jangan gunakan method close seperti ini di mana pun
    // public static void close() { connection.close(); }  ← Jangan ada ini dipanggil!
}

