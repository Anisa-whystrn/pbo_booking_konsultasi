package com.booking.model;

public class Mahasiswa extends User {
    public Mahasiswa() { super(); }
    public Mahasiswa(int id, String username, String email, String password, String nim) {
        super(id, username, email, password, "MAHASISWA", nim);
    }
    
    @Override
    public String getDashboardTitle() {
        return "Dashboard Mahasiswa";
    }
}