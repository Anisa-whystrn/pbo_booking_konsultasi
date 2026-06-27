package com.booking.dao;

import com.booking.model.JadwalKonsultasi;
import com.booking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JadwalDao {
    
    public void tambahJadwal(JadwalKonsultasi j) throws SQLException {
        String sql = "INSERT INTO jadwal_konsultasi(dosen_id,tanggal,jam_mulai,jam_selesai,status) VALUES(?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, j.getDosenId());
            ps.setDate(2, j.getTanggal());
            ps.setTime(3, j.getJamMulai());
            ps.setTime(4, j.getJamSelesai());
            ps.setString(5, j.getStatus() != null ? j.getStatus() : "TERSEDIA");
            ps.executeUpdate();
        }
    }
    
    public List<JadwalKonsultasi> getAllJadwal() throws SQLException {
        List<JadwalKonsultasi> list = new ArrayList<>();
        String sql = "SELECT j.*, u.username as nama_dosen FROM jadwal_konsultasi j " +
                     "JOIN users u ON j.dosen_id = u.id ORDER BY j.tanggal DESC, j.jam_mulai";
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                JadwalKonsultasi j = new JadwalKonsultasi();
                j.setIdJadwal(rs.getInt("id_jadwal"));
                j.setDosenId(rs.getInt("dosen_id"));
                j.setNamaDosen(rs.getString("nama_dosen"));
                j.setTanggal(rs.getDate("tanggal"));
                j.setJamMulai(rs.getTime("jam_mulai"));
                j.setJamSelesai(rs.getTime("jam_selesai"));
                j.setStatus(rs.getString("status"));
                list.add(j);
            }
        }
        return list;
    }
    
    // ✅ METHOD BARU - Ambil jadwal berdasarkan dosen_id
    public List<JadwalKonsultasi> getJadwalByDosenId(int dosenId) throws SQLException {
        List<JadwalKonsultasi> list = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_konsultasi WHERE dosen_id = ? ORDER BY tanggal, jam_mulai";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, dosenId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JadwalKonsultasi j = new JadwalKonsultasi();
                j.setIdJadwal(rs.getInt("id_jadwal"));
                j.setDosenId(rs.getInt("dosen_id"));
                j.setTanggal(rs.getDate("tanggal"));
                j.setJamMulai(rs.getTime("jam_mulai"));
                j.setJamSelesai(rs.getTime("jam_selesai"));
                j.setStatus(rs.getString("status"));
                list.add(j);
            }
        }
        return list;
    }
    
    public List<JadwalKonsultasi> getJadwalTersedia() throws SQLException {
        List<JadwalKonsultasi> list = new ArrayList<>();
        String sql = "SELECT j.*, u.username as nama_dosen FROM jadwal_konsultasi j " +
                     "JOIN users u ON j.dosen_id = u.id WHERE j.status='TERSEDIA' " +
                     "ORDER BY j.tanggal, j.jam_mulai";
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                JadwalKonsultasi j = new JadwalKonsultasi();
                j.setIdJadwal(rs.getInt("id_jadwal"));
                j.setDosenId(rs.getInt("dosen_id"));
                j.setNamaDosen(rs.getString("nama_dosen"));
                j.setTanggal(rs.getDate("tanggal"));
                j.setJamMulai(rs.getTime("jam_mulai"));
                j.setJamSelesai(rs.getTime("jam_selesai"));
                j.setStatus(rs.getString("status"));
                list.add(j);
            }
        }
        return list;
    }
    // Tambah method ini di JadwalDao.java
public List<JadwalKonsultasi> getJadwalTersediaForMahasiswa(int mahasiswaId) throws SQLException {
    List<JadwalKonsultasi> list = new ArrayList<>();
    String sql = "SELECT j.*, u.username as nama_dosen FROM jadwal_konsultasi j " +
                 "JOIN users u ON j.dosen_id = u.id " +
                 "WHERE j.status='TERSEDIA' " +
                 "AND j.id_jadwal NOT IN (" +
                 "   SELECT b.jadwal_id FROM booking b " +
                 "   WHERE b.mahasiswa_id = ? AND b.status_booking IN ('PENDING','DISETUJUI')" +
                 ") " +
                 "ORDER BY j.tanggal, j.jam_mulai";
    try (Connection c = DatabaseConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, mahasiswaId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            JadwalKonsultasi j = new JadwalKonsultasi();
            j.setIdJadwal(rs.getInt("id_jadwal"));
            j.setDosenId(rs.getInt("dosen_id"));
            j.setNamaDosen(rs.getString("nama_dosen"));
            j.setTanggal(rs.getDate("tanggal"));
            j.setJamMulai(rs.getTime("jam_mulai"));
            j.setJamSelesai(rs.getTime("jam_selesai"));
            j.setStatus(rs.getString("status"));
            list.add(j);
        }
    }
    return list;
}
    
    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE jadwal_konsultasi SET status=? WHERE id_jadwal=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
    
    public void updateJadwal(JadwalKonsultasi j) throws SQLException {
        String sql = "UPDATE jadwal_konsultasi SET dosen_id=?,tanggal=?,jam_mulai=?,jam_selesai=? WHERE id_jadwal=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, j.getDosenId());
            ps.setDate(2, j.getTanggal());
            ps.setTime(3, j.getJamMulai());
            ps.setTime(4, j.getJamSelesai());
            ps.setInt(5, j.getIdJadwal());
            ps.executeUpdate();
        }
    }
    
    public void deleteJadwal(int id) throws SQLException {
        String sql = "DELETE FROM jadwal_konsultasi WHERE id_jadwal=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    // Tambah method ini di BookingDao.java
public void updateTopikBooking(int idBooking, String topik) throws SQLException {
    String sql = "UPDATE booking SET topik_skripsi=? WHERE id_booking=?";
    try (Connection c = DatabaseConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, topik);
        ps.setInt(2, idBooking);
        ps.executeUpdate();
    }
}

public void cancelBooking(int idBooking) throws SQLException {
    String sql = "DELETE FROM booking WHERE id_booking=?";
    try (Connection c = DatabaseConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, idBooking);
        ps.executeUpdate();
    }
}
}