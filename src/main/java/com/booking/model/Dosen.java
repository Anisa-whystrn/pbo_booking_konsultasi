package com.booking.model;

public class Dosen extends User {
    public Dosen() { super(); }
    public Dosen(int id, String username, String email, String password, String nip) {
        super(id, username, email, password, "DOSEN", nip);
    }
    
    @Override
    public String getDashboardTitle() {
        return "Dashboard Dosen";
    }
}