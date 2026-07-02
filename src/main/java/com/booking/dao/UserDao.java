package com.booking.dao;

import com.booking.model.*;
import com.booking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    
    // LOGIN - Cek username & password
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection c = DatabaseConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                int id = rs.getInt("id");
                String un = rs.getString("username");
                String em = rs.getString("email");
                String pw = rs.getString("password");
                String nim = rs.getString("nim_nip");
            
                User user;
            // Polymorphism: buat objek sesuai role
                switch (role) {
                    case "ADMIN": user = new Admin(id, un, em, pw, nim); break;
                    case "DOSEN": user = new Dosen(id, un, em, pw, nim); break;
                    default: user = new Mahasiswa(id, un, em, pw, nim);
                }
            
                // ✅ Gunakan method dari interface Loginable untuk verifikasi
                if (user.login(un, pw)) {
                    System.out.println("✅ Login berhasil: " + un + " sebagai " + user.getRole());
                    return user;
                }
            }
        }
        return null;
    }
    
    // CREATE - Tambah user baru
    public void tambahUser(User u) throws SQLException {
        String sql = "INSERT INTO users(username,email,password,role,nim_nip) VALUES(?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRole());
            ps.setString(5, u.getNimNip());
            ps.executeUpdate();
        }
    }
    
    // READ - Ambil semua user
    public List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id ASC";
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                String role = rs.getString("role");
                User u;
                switch (role) {
                    case "ADMIN": u = new Admin(); break;
                    case "DOSEN": u = new Dosen(); break;
                    default: u = new Mahasiswa();
                }
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(role);
                u.setNimNip(rs.getString("nim_nip"));
                list.add(u);
            }
        }
        return list;
    }
    
    // UPDATE - Edit user
    public void updateUser(User u) throws SQLException {
        String sql = "UPDATE users SET username=?,email=?,password=?,role=?,nim_nip=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRole());
            ps.setString(5, u.getNimNip());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        }
    }
    
    // DELETE - Hapus user
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}