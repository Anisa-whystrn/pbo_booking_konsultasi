package com.booking.dao;

import com.booking.model.Booking;
import com.booking.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDao {
    
    public void buatBooking(Booking b) throws SQLException {
        String sql = "INSERT INTO booking(jadwal_id,mahasiswa_id,topik_skripsi,status_booking) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, b.getJadwalId());
            ps.setInt(2, b.getMahasiswaId());
            ps.setString(3, b.getTopikSkripsi());
            ps.setString(4, b.getStatusBooking() != null ? b.getStatusBooking() : "PENDING");
            ps.executeUpdate();
        }
    }
    
    public List<Booking> getAllBooking() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, m.username as nama_mhs, m.nim_nip as nim_mhs, " +
                     "d.username as nama_dosen, j.tanggal, j.jam_mulai, j.jam_selesai " +
                     "FROM booking b " +
                     "JOIN jadwal_konsultasi j ON b.jadwal_id = j.id_jadwal " +
                     "JOIN users m ON b.mahasiswa_id = m.id " +
                     "JOIN users d ON j.dosen_id = d.id " +
                     "ORDER BY b.created_at DESC";
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Booking b = new Booking();
                b.setIdBooking(rs.getInt("id_booking"));
                b.setJadwalId(rs.getInt("jadwal_id"));
                b.setMahasiswaId(rs.getInt("mahasiswa_id"));
                b.setNamaMahasiswa(rs.getString("nama_mhs"));
                b.setNimMahasiswa(rs.getString("nim_mhs"));
                b.setNamaDosen(rs.getString("nama_dosen"));
                b.setTopikSkripsi(rs.getString("topik_skripsi"));
                b.setLokasi(rs.getString("lokasi"));
                b.setStatusBooking(rs.getString("status_booking"));
                b.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(b);
            }
        }
        return list;
    }
    
    public List<Booking> getBookingByMahasiswaId(int mhsId) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, j.tanggal, j.jam_mulai, j.jam_selesai, " +
                     "d.username as nama_dosen, d.nim_nip as nip_dosen " +
                     "FROM booking b " +
                     "JOIN jadwal_konsultasi j ON b.jadwal_id = j.id_jadwal " +
                     "JOIN users d ON j.dosen_id = d.id " +
                     "WHERE b.mahasiswa_id = ? " +
                     "ORDER BY b.created_at DESC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, mhsId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking b = new Booking();
                b.setIdBooking(rs.getInt("id_booking"));
                b.setJadwalId(rs.getInt("jadwal_id"));
                b.setMahasiswaId(rs.getInt("mahasiswa_id"));
                b.setTopikSkripsi(rs.getString("topik_skripsi"));
                b.setLokasi(rs.getString("lokasi"));
                b.setStatusBooking(rs.getString("status_booking"));
                b.setCreatedAt(rs.getTimestamp("created_at"));
                b.setNamaDosen(rs.getString("nama_dosen"));
                list.add(b);
            }
        }
        return list;
    }
    
    public List<Booking> getBookingByDosenId(int dosenId) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, m.username as nama_mhs, m.nim_nip as nim_mhs, " +
                     "j.tanggal, j.jam_mulai, j.jam_selesai " +
                     "FROM booking b " +
                     "JOIN jadwal_konsultasi j ON b.jadwal_id = j.id_jadwal " +
                     "JOIN users m ON b.mahasiswa_id = m.id " +
                     "WHERE j.dosen_id = ? " +
                     "ORDER BY b.created_at DESC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, dosenId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking b = new Booking();
                b.setIdBooking(rs.getInt("id_booking"));
                b.setJadwalId(rs.getInt("jadwal_id"));
                b.setMahasiswaId(rs.getInt("mahasiswa_id"));
                b.setNamaMahasiswa(rs.getString("nama_mhs"));
                b.setNimMahasiswa(rs.getString("nim_mhs"));
                b.setTopikSkripsi(rs.getString("topik_skripsi"));
                b.setLokasi(rs.getString("lokasi"));
                b.setStatusBooking(rs.getString("status_booking"));
                b.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(b);
            }
        }
        return list;
    }
    
    public void updateStatusBooking(int id, String status) throws SQLException {
        String sql = "UPDATE booking SET status_booking=? WHERE id_booking=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
    
    public void updateLokasi(int idBooking, String lokasi) throws SQLException {
        String sql = "UPDATE booking SET lokasi=? WHERE id_booking=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, lokasi);
            ps.setInt(2, idBooking);
            ps.executeUpdate();
        }
    }
    
    // ✅ METHOD BARU - Update topik booking
    public void updateTopikBooking(int idBooking, String topik) throws SQLException {
        String sql = "UPDATE booking SET topik_skripsi=? WHERE id_booking=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, topik);
            ps.setInt(2, idBooking);
            ps.executeUpdate();
        }
    }
    
    // ✅ METHOD BARU - Cancel/Hapus booking
    public void cancelBooking(int idBooking) throws SQLException {
        String sql = "DELETE FROM booking WHERE id_booking=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idBooking);
            ps.executeUpdate();
        }
    }
    
    public void deleteBooking(int id) throws SQLException {
        String sql = "DELETE FROM booking WHERE id_booking=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}