package com.booking.model;

public class Admin extends User {
    public Admin() { super(); }
    public Admin(int id, String username, String email, String password, String nimNip) {
        super(id, username, email, password, "ADMIN", nimNip);
    }
    
    @Override
    public String getDashboardTitle() {
        return "Dashboard Admin";
    }
}