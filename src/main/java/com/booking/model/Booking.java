package com.booking.model;

import java.sql.Timestamp;

public class Booking {
    private int idBooking;
    private int jadwalId;
    private int mahasiswaId;
    private String namaMahasiswa;
    private String nimMahasiswa;  // ✅ Tambah field ini
    private String namaDosen;
    private String topikSkripsi;
    private String lokasi;
    private String statusBooking;
    private Timestamp createdAt;

    // Getters & Setters
    public int getIdBooking() { return idBooking; }
    public void setIdBooking(int idBooking) { this.idBooking = idBooking; }
    public int getJadwalId() { return jadwalId; }
    public void setJadwalId(int jadwalId) { this.jadwalId = jadwalId; }
    public int getMahasiswaId() { return mahasiswaId; }
    public void setMahasiswaId(int mahasiswaId) { this.mahasiswaId = mahasiswaId; }
    public String getNamaMahasiswa() { return namaMahasiswa; }
    public void setNamaMahasiswa(String namaMahasiswa) { this.namaMahasiswa = namaMahasiswa; }
    
    // ✅ Getter & Setter untuk NIM
    public String getNimMahasiswa() { return nimMahasiswa; }
    public void setNimMahasiswa(String nimMahasiswa) { this.nimMahasiswa = nimMahasiswa; }
    
    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }
    public String getTopikSkripsi() { return topikSkripsi; }
    public void setTopikSkripsi(String topikSkripsi) { this.topikSkripsi = topikSkripsi; }
    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
    public String getStatusBooking() { return statusBooking; }
    public void setStatusBooking(String statusBooking) { this.statusBooking = statusBooking; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}