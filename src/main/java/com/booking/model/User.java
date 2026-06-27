package com.booking.model;

public abstract class User {
    // Encapsulation: atribut private
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String nimNip;

    public User() {}

    public User(int id, String username, String email, String password, String role, String nimNip) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nimNip = nimNip;
    }

    // Polymorphism: method abstract yang wajib di-override
    public abstract String getDashboardTitle();

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getNimNip() { return nimNip; }
    public void setNimNip(String nimNip) { this.nimNip = nimNip; }
}