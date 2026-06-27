package com.booking.ui;

import com.booking.dao.*;
import com.booking.model.*;
import com.booking.report.LaporanBookingImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List; 

public class AdminDashboard extends JFrame {
    private Admin admin;
    private JTabbedPane tabs = new JTabbedPane();
    private UserDao userDao = new UserDao();
    private JadwalDao jadwalDao = new JadwalDao();
    private BookingDao bookingDao = new BookingDao();

    // Tabel Users
    private JTable tblUsers = new JTable();
    private DefaultTableModel modelUsers;
    // Tabel Statistik
    private JTable tblStatistik = new JTable();
    private DefaultTableModel modelStatistik;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        setTitle(admin.getDashboardTitle() + " - " + admin.getUsername());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Header ===
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(new Color(67, 97, 238));
        JLabel lblTitle = new JLabel("[ADMIN] " + admin.getDashboardTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle);
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(238, 67, 67));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.add(Box.createHorizontalGlue());
        header.add(btnLogout);

        // === TabbedPane ===
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("Kelola User", buildUserPanel());
        tabs.addTab("Laporan User", buildLaporanPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        loadUsers();
        loadStatistik();
    }

    // ============ TAB 1: KELOLA USER ============
    private JPanel buildUserPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        modelUsers = new DefaultTableModel(new String[]{"ID","Username","Email","Role","NIM/NIP"}, 0);
        tblUsers.setModel(modelUsers);
        tblUsers.setRowHeight(30);
        tblUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane sp = new JScrollPane(tblUsers);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("+ Tambah User");
        JButton btnEdit = new JButton("Edit");
        JButton btnDel = new JButton("Hapus");
        styleButton(btnAdd, new Color(67, 97, 238));
        styleButton(btnEdit, new Color(255, 180, 0));
        styleButton(btnDel, new Color(238, 67, 67));
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDel);

        p.add(sp, BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> formUser(null));
        btnEdit.addActionListener(e -> {
            int r = tblUsers.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Pilih data dulu!"); return; }
            formUser(getUserFromRow(r));
        });
        btnDel.addActionListener(e -> {
            int r = tblUsers.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Pilih data dulu!"); return; }
            int id = (int) tblUsers.getValueAt(r, 0);
            if (JOptionPane.showConfirmDialog(this, "Hapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == 0) {
                try { userDao.deleteUser(id); loadUsers(); } 
                catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
            }
        });
        return p;
    }

    private void formUser(User u) {
        boolean edit = u != null;
        JTextField txtUser = new JTextField(edit ? u.getUsername() : "");
        JTextField txtEmail = new JTextField(edit ? u.getEmail() : "");
        JPasswordField txtPass = new JPasswordField(edit ? u.getPassword() : "123456");
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"ADMIN","DOSEN","MAHASISWA"});
        JTextField txtNim = new JTextField(edit ? u.getNimNip() : "");
        if (edit) cbRole.setSelectedItem(u.getRole());

        Object[] fields = {"Username:", txtUser, "Email:", txtEmail, "Password:", txtPass,
                           "Role:", cbRole, "NIM/NIP:", txtNim};
        if (JOptionPane.showConfirmDialog(this, fields, edit ? "Edit User" : "Tambah User",
                JOptionPane.OK_CANCEL_OPTION) == 0) {
            try {
                User newU;
                String role = cbRole.getSelectedItem().toString();
                switch (role) {
                    case "ADMIN": newU = new Admin(); break;
                    case "DOSEN": newU = new Dosen(); break;
                    default: newU = new Mahasiswa();
                }
                newU.setUsername(txtUser.getText());
                newU.setEmail(txtEmail.getText());
                newU.setPassword(new String(txtPass.getPassword()));
                newU.setRole(role);
                newU.setNimNip(txtNim.getText());
                if (edit) { newU.setId(u.getId()); userDao.updateUser(newU); }
                else userDao.tambahUser(newU);
                loadUsers();
                loadStatistik(); // Refresh statistik
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        }
    }

    private User getUserFromRow(int r) {
        String role = (String) tblUsers.getValueAt(r, 3);
        User u;
        switch (role) {
            case "ADMIN": u = new Admin(); break;
            case "DOSEN": u = new Dosen(); break;
            default: u = new Mahasiswa();
        }
        u.setId((int) tblUsers.getValueAt(r, 0));
        u.setUsername((String) tblUsers.getValueAt(r, 1));
        u.setEmail((String) tblUsers.getValueAt(r, 2));
        u.setRole(role);
        u.setNimNip((String) tblUsers.getValueAt(r, 4));
        return u;
    }

    private void loadUsers() {
        modelUsers.setRowCount(0);
        try {
            for (User u : userDao.getAllUsers()) {
                modelUsers.addRow(new Object[]{u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getNimNip()});
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    // ============ TAB 2: LAPORAN USER ============
    private JPanel buildLaporanPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        modelStatistik = new DefaultTableModel(new String[]{"Role","Jumlah"}, 0);
        tblStatistik.setModel(modelStatistik);
        tblStatistik.setRowHeight(40);
        tblStatistik.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblStatistik.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JScrollPane sp = new JScrollPane(tblStatistik);
        p.add(sp, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnPDF = new JButton("Export PDF Laporan User");
        styleButton(btnRefresh, new Color(108, 117, 125));
        styleButton(btnPDF, new Color(23, 162, 184));
        btnPanel.add(btnRefresh);
        btnPanel.add(btnPDF);

        btnRefresh.addActionListener(e -> loadStatistik());
        btnPDF.addActionListener(e -> exportLaporanUserPDF());

        p.add(btnPanel, BorderLayout.SOUTH);
        return p;
    }

    private void loadStatistik() {
        modelStatistik.setRowCount(0);
        try {
            int adminCount = 0, dosenCount = 0, mhsCount = 0;
            
            for (User u : userDao.getAllUsers()) {
                switch (u.getRole()) {
                    case "ADMIN": adminCount++; break;
                    case "DOSEN": dosenCount++; break;
                    case "MAHASISWA": mhsCount++; break;
                }
            }
            
            modelStatistik.addRow(new Object[]{"Admin", adminCount});
            modelStatistik.addRow(new Object[]{"Dosen", dosenCount});
            modelStatistik.addRow(new Object[]{"Mahasiswa", mhsCount});
            modelStatistik.addRow(new Object[]{"TOTAL", adminCount + dosenCount + mhsCount});
            
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, e.getMessage()); 
        }
    }

    private void exportLaporanUserPDF() {
    try {
        // Ambil semua user
        List<User> allUsers = userDao.getAllUsers();
        
        // Hitung statistik
        int adminCount = 0, dosenCount = 0, mhsCount = 0;
        for (User u : allUsers) {
            switch (u.getRole()) {
                case "ADMIN": adminCount++; break;
                case "DOSEN": dosenCount++; break;
                case "MAHASISWA": mhsCount++; break;
            }
        }
        
        String path = "Laporan_User_" + System.currentTimeMillis() + ".pdf";
        new LaporanBookingImpl().generateLaporanUserPDF(path, allUsers, adminCount, dosenCount, mhsCount);
        JOptionPane.showMessageDialog(this, "PDF Laporan User berhasil dibuat:\n" + path);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Gagal export PDF: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}