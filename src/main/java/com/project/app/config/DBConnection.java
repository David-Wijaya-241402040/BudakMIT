package main.java.com.project.app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/budakmit",
                        "root",
                        ""
                );
                System.out.println("Koneksi database berhasil dibuka ulang ✅");
            }
        } catch (SQLException e) {
            System.err.println("Gagal membuka koneksi database ❌");
            e.printStackTrace();
        }
        return connection;
    }

}