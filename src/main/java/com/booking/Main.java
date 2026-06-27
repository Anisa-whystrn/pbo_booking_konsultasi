package com.booking;

import com.booking.ui.LoginView;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Setup tampilan modern dengan FlatLaf
        FlatLightLaf.setup();
        
        // Konfigurasi tampilan global
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("ScrollBar.thumbArc", 999);
        
        // Jalankan UI di Event Dispatch Thread (wajib di Swing)
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}