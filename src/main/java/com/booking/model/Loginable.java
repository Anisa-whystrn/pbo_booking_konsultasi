package com.booking.model;

public interface Loginable {
    boolean login(String username, String password);
    void logout();
}