package com.booking.ui;

import com.booking.dao.UserDao;
import com.booking.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnLogin = new JButton("Login");
    private UserDao userDao = new UserDao();

    public LoginView() {
        setTitle("Login - Booking Konsultasi");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        // === Panel Header ===
        JPanel header = new JPanel();
        header.setBackground(new Color(67, 97, 238));
        header.setPreferredSize(new Dimension(0, 120));
        header.setLayout(new GridBagLayout());
        JLabel title = new JLabel("SIBOS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);

        // === Panel Form ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Selamat Datang!");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblTitle, gbc);

        JLabel lblSub = new JLabel("Silakan login untuk melanjutkan");
        lblSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        formPanel.add(lblSub, gbc);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(lblUser, gbc);

        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 3;
        formPanel.add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 4;
        formPanel.add(lblPass, gbc);

        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 5;
        formPanel.add(txtPassword, gbc);

        btnLogin.setBackground(new Color(67, 97, 238));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(0, 45));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6; gbc.insets = new Insets(25, 30, 10, 30);
        formPanel.add(btnLogin, gbc);

        add(header, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        // === Action Login ===
        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addActionListener(e -> doLogin()); // Enter di password = login
    }

    private void doLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username & Password harus diisi!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            User u = userDao.login(user, pass);
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", 
                    "Login Gagal", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + u.getRole());
                dispose();
                bukaDashboard(u);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Polymorphism: buka dashboard sesuai tipe user
    private void bukaDashboard(User u) {
        if (u.getRole().equals("ADMIN")) new AdminDashboard((com.booking.model.Admin) u).setVisible(true);
        else if (u.getRole().equals("DOSEN")) new DosenDashboard((com.booking.model.Dosen) u).setVisible(true);
        else new MahasiswaDashboard((com.booking.model.Mahasiswa) u).setVisible(true);
    }
}