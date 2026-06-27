package com.booking.model;

import java.sql.Date;
import java.sql.Time;

public class JadwalKonsultasi {
    private int idJadwal;
    private int dosenId;
    private String namaDosen;
    private Date tanggal;
    private Time jamMulai;
    private Time jamSelesai;
    private String status;

    // Getters & Setters
    public int getIdJadwal() { return idJadwal; }
    public void setIdJadwal(int idJadwal) { this.idJadwal = idJadwal; }
    public int getDosenId() { return dosenId; }
    public void setDosenId(int dosenId) { this.dosenId = dosenId; }
    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }
    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }
    public Time getJamMulai() { return jamMulai; }
    public void setJamMulai(Time jamMulai) { this.jamMulai = jamMulai; }
    public Time getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(Time jamSelesai) { this.jamSelesai = jamSelesai; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}