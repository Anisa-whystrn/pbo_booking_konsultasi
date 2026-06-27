package com.booking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Ganti port sesuai MariaDB kamu (3306 atau 3307)
    private static final String URL = "jdbc:mysql://localhost:3307/jadwalbooking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection conn;
    
    // Load driver secara manual dan eksplisit
    static {
        try {
            // Register MySQL Driver secara eksplisit
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            System.out.println("✅ MySQL Driver registered successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Failed to register MySQL Driver!");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                System.out.println("🔌 Connecting to: " + URL);
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            } catch (SQLException e) {
                System.err.println("❌ Connection failed!");
                System.err.println("Error: " + e.getMessage());
                throw e;
            }
        }
        return conn;
    }
    
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔌 Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}