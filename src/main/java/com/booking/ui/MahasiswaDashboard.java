package com.booking.ui;

import com.booking.dao.*;
import com.booking.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class MahasiswaDashboard extends JFrame {
    private Mahasiswa mhs;
    private JadwalDao jadwalDao = new JadwalDao();
    private BookingDao bookingDao = new BookingDao();
    private JTabbedPane tabs = new JTabbedPane();
    
    private JTable tblJadwal = new JTable();
    private DefaultTableModel modelJadwal;
    
    private JTable tblRiwayat = new JTable();
    private DefaultTableModel modelRiwayat;

    public MahasiswaDashboard(Mahasiswa mhs) {
        this.mhs = mhs;
        setTitle(mhs.getDashboardTitle() + " - " + mhs.getUsername());
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(new Color(40, 167, 69));
        JLabel lbl = new JLabel("[MAHASISWA] " + mhs.getDashboardTitle());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(238, 67, 67));
        btnLogout.setForeground(Color.WHITE);
        header.add(Box.createHorizontalGlue());
        header.add(btnLogout);

        // Tab 1: Jadwal Tersedia
        modelJadwal = new DefaultTableModel(
            new String[]{"ID","Dosen","Tanggal","Jam Mulai","Jam Selesai"}, 0);
        tblJadwal.setModel(modelJadwal);
        tblJadwal.setRowHeight(35);
        tblJadwal.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JPanel btnBookPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBook = new JButton("Booking Jadwal");
        btnBook.setBackground(new Color(40, 167, 69));
        btnBook.setForeground(Color.WHITE);
        btnBook.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBookPanel.add(btnBook);
        
        JPanel panelJadwal = new JPanel(new BorderLayout());
        panelJadwal.add(new JScrollPane(tblJadwal), BorderLayout.CENTER);
        panelJadwal.add(btnBookPanel, BorderLayout.SOUTH);
        
        tabs.addTab("Jadwal Tersedia", panelJadwal);

        // Tab 2: Riwayat Booking
        modelRiwayat = new DefaultTableModel(
            new String[]{"ID","Dosen","Tanggal","Jam","Topik","Status","Lokasi"}, 0);
        tblRiwayat.setModel(modelRiwayat);
        tblRiwayat.setRowHeight(35);
        tblRiwayat.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JPanel btnPanelRiwayat = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEdit = new JButton("Edit Topik");
        JButton btnRefresh = new JButton("Refresh");
        
        styleButton(btnEdit, new Color(255, 180, 0));
        styleButton(btnRefresh, new Color(108, 117, 125));
        
        btnPanelRiwayat.add(btnEdit);
        btnPanelRiwayat.add(btnRefresh);
        
        JPanel panelRiwayat = new JPanel(new BorderLayout());
        panelRiwayat.add(new JScrollPane(tblRiwayat), BorderLayout.CENTER);
        panelRiwayat.add(btnPanelRiwayat, BorderLayout.SOUTH);
        
        tabs.addTab("Riwayat Booking", panelRiwayat);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        // Actions
        btnLogout.addActionListener(e -> { dispose(); new LoginView().setVisible(true); });
        btnBook.addActionListener(e -> doBooking());
        btnEdit.addActionListener(e -> editBooking());
        btnRefresh.addActionListener(e -> { loadJadwal(); loadRiwayat(); });

        loadJadwal();
        loadRiwayat();
    }

    private void loadJadwal() {
        modelJadwal.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            // Gunakan method baru yang filter jadwal untuk mahasiswa ini
            for (JadwalKonsultasi j : jadwalDao.getJadwalTersediaForMahasiswa(mhs.getId())) {
                modelJadwal.addRow(new Object[]{
                    j.getIdJadwal(), 
                    j.getNamaDosen(), 
                    sdf.format(j.getTanggal()),
                    j.getJamMulai().toString().substring(0,5), 
                    j.getJamSelesai().toString().substring(0,5)
                });
            }
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, e.getMessage()); 
        }
    }

    private void loadRiwayat() {
        modelRiwayat.setRowCount(0);
        SimpleDateFormat sdfTanggal = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfWaktu = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        try {
            for (Booking b : bookingDao.getBookingByMahasiswaId(mhs.getId())) {
                String tglBooking = b.getCreatedAt() != null ? 
                    sdfWaktu.format(b.getCreatedAt()) : "-";
                
                String lokasi = b.getLokasi();
                String lokasiDisplay = "-";
                if (lokasi != null && !lokasi.trim().isEmpty() && !lokasi.equals("-")) {
                    lokasiDisplay = lokasi;
                }
                
                String tanggalKonsul = "-";
                String jamKonsul = "-";
                
                if (b.getJadwalId() > 0) {
                    try {
                        var jadwalList = jadwalDao.getAllJadwal();
                        for (var j : jadwalList) {
                            if (j.getIdJadwal() == b.getJadwalId()) {
                                tanggalKonsul = sdfTanggal.format(j.getTanggal());
                                jamKonsul = j.getJamMulai().toString().substring(0, 5);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        // Ignore
                    }
                }
                
                modelRiwayat.addRow(new Object[]{
                    b.getIdBooking(),
                    b.getNamaDosen() != null ? b.getNamaDosen() : "-",
                    tanggalKonsul,
                    jamKonsul,
                    b.getTopikSkripsi(),
                    b.getStatusBooking(),
                    lokasiDisplay
                });
            }
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); 
        }
    }

    private void doBooking() {
        int r = tblJadwal.getSelectedRow();
        if (r < 0) { 
            JOptionPane.showMessageDialog(this, "Pilih jadwal dulu!"); 
            return; 
        }
        int jadwalId = (int) tblJadwal.getValueAt(r, 0);
        
        String topik = JOptionPane.showInputDialog(this, "Masukkan Topik Skripsi/Konsultasi:");
        if (topik == null || topik.trim().isEmpty()) return;
        
        try {
            Booking b = new Booking();
            b.setJadwalId(jadwalId);
            b.setMahasiswaId(mhs.getId());
            b.setTopikSkripsi(topik);
            b.setStatusBooking("PENDING");
            bookingDao.buatBooking(b);
            
            JOptionPane.showMessageDialog(this, "✅ Booking berhasil dibuat!\nMenunggu persetujuan dosen.");
            loadJadwal();  // Refresh - jadwal yang sudah diboooking akan hilang
            loadRiwayat();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editBooking() {
        int r = tblRiwayat.getSelectedRow();
        if (r < 0) { 
            JOptionPane.showMessageDialog(this, "Pilih booking yang akan diedit!"); 
            return; 
        }
        
        int idBooking = (int) tblRiwayat.getValueAt(r, 0);
        String status = (String) tblRiwayat.getValueAt(r, 5);
        
        if (!status.equals("PENDING")) {
            JOptionPane.showMessageDialog(this, 
                "Hanya booking dengan status PENDING yang bisa diedit.", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String topikLama = (String) tblRiwayat.getValueAt(r, 4);
        String topikBaru = JOptionPane.showInputDialog(this, 
            "Edit Topik Konsultasi:", topikLama);
        
        if (topikBaru == null || topikBaru.trim().isEmpty()) return;
        
        try {
            bookingDao.updateTopikBooking(idBooking, topikBaru);
            JOptionPane.showMessageDialog(this, "✅ Topik berhasil diubah!");
            loadRiwayat();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
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