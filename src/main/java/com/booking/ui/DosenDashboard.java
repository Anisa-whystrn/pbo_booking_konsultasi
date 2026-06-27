package com.booking.ui;

import com.booking.dao.*;
import com.booking.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class DosenDashboard extends JFrame {
    private Dosen dosen;
    private BookingDao bookingDao = new BookingDao();
    private JadwalDao jadwalDao = new JadwalDao();
    
    // Tabel Booking
    private JTable tblBooking = new JTable();
    private DefaultTableModel modelBooking;
    
    // Tabel Jadwal
    private JTable tblJadwal = new JTable();
    private DefaultTableModel modelJadwal;

    public DosenDashboard(Dosen dosen) {
        this.dosen = dosen;
        setTitle(dosen.getDashboardTitle() + " - " + dosen.getUsername());
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(new Color(255, 140, 0));
        JLabel lbl = new JLabel("[DOSEN] " + dosen.getDashboardTitle());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(238, 67, 67));
        btnLogout.setForeground(Color.WHITE);
        header.add(Box.createHorizontalGlue());
        header.add(btnLogout);

        // === TABBED PANE ===
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Tab 1: Booking Mahasiswa
        tabs.addTab("Booking Masuk", buildBookingPanel());
        
        // Tab 2: Jadwal Saya
        tabs.addTab("Jadwal Saya", buildJadwalSayaPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        // Actions
        btnLogout.addActionListener(e -> { 
            dispose(); 
            new LoginView().setVisible(true); 
        });

        loadBooking();
        loadJadwalSaya();
    }

    // ============ TAB 1: BOOKING PANEL ============
    private JPanel buildBookingPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Inisialisasi model booking
        modelBooking = new DefaultTableModel(
            new String[]{"ID","Mahasiswa","NIM","Topik","Status","Lokasi","Aksi"}, 0);
        tblBooking.setModel(modelBooking);
        tblBooking.setRowHeight(40);
        tblBooking.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSetujui = new JButton("Setujui");
        JButton btnTolak = new JButton("Tolak");
        JButton btnLokasi = new JButton("Set Lokasi");
        JButton btnRefresh = new JButton("Refresh");
        
        styleButton(btnSetujui, new Color(40, 167, 69));
        styleButton(btnTolak, new Color(238, 67, 67));
        styleButton(btnLokasi, new Color(23, 162, 184));
        styleButton(btnRefresh, new Color(108, 117, 125));
        
        btnPanel.add(btnSetujui);
        btnPanel.add(btnTolak);
        btnPanel.add(btnLokasi);
        btnPanel.add(btnRefresh);

        p.add(new JScrollPane(tblBooking), BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        btnSetujui.addActionListener(e -> updateStatus("DISETUJUI"));
        btnTolak.addActionListener(e -> updateStatus("DITOLAK"));
        btnLokasi.addActionListener(e -> setLokasi());
        btnRefresh.addActionListener(e -> loadBooking());

        return p;
    }

    private void loadBooking() {
        modelBooking.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        try {
            for (Booking b : bookingDao.getBookingByDosenId(dosen.getId())) {
                // Format tanggal
                String tglFormat = b.getCreatedAt() != null ? sdf.format(b.getCreatedAt()) : "-";
                
                // Cek lokasi dengan lebih detail
                String lokasi = b.getLokasi();
                String lokasiDisplay = "-";
                if (lokasi != null && !lokasi.trim().isEmpty() && !lokasi.equals("-")) {
                    lokasiDisplay = lokasi;
                }
                
                modelBooking.addRow(new Object[]{
                    b.getIdBooking(),
                    b.getNamaMahasiswa(),
                    b.getNimMahasiswa(),
                    b.getTopikSkripsi(),
                    b.getStatusBooking(),
                    lokasiDisplay,
                    "Detail"
                });
            }
            
            // Set renderer dan editor untuk kolom aksi
            if (tblBooking.getColumnCount() > 6) {
                tblBooking.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
                tblBooking.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
            }
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, "Error loading booking: " + e.getMessage()); 
        }
    }

    private void updateStatus(String status) {
        int r = tblBooking.getSelectedRow();
        if (r < 0) { 
            JOptionPane.showMessageDialog(this, "Pilih booking dulu!"); 
            return; 
        }
        int id = (int) tblBooking.getValueAt(r, 0);
        
        try {
            bookingDao.updateStatusBooking(id, status);
            
            // Update status jadwal berdasarkan approval
            if (status.equals("DITOLAK")) {
                Booking b = bookingDao.getAllBooking().stream()
                    .filter(booking -> booking.getIdBooking() == id)
                    .findFirst().orElse(null);
                if (b != null) {
                    jadwalDao.updateStatus(b.getJadwalId(), "TERSEDIA");
                }
            } else if (status.equals("DISETUJUI")) {
                Booking b = bookingDao.getAllBooking().stream()
                    .filter(booking -> booking.getIdBooking() == id)
                    .findFirst().orElse(null);
                if (b != null) {
                    jadwalDao.updateStatus(b.getJadwalId(), "TERBOOKING");
                }
            }
            
            JOptionPane.showMessageDialog(this, "Status berhasil diubah ke " + status);
            loadBooking();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void setLokasi() {
        int r = tblBooking.getSelectedRow();
        if (r < 0) { 
            JOptionPane.showMessageDialog(this, "Pilih booking dulu!"); 
            return; 
        }
        int id = (int) tblBooking.getValueAt(r, 0);
        
        String lokasi = JOptionPane.showInputDialog(this, 
            "Masukkan Lokasi Bimbingan:\n(Ruang, Link Zoom, dll)");
        if (lokasi == null || lokasi.trim().isEmpty()) return;
        
        try {
            bookingDao.updateLokasi(id, lokasi);
            JOptionPane.showMessageDialog(this, "Lokasi berhasil disimpan!");
            loadBooking();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ============ TAB 2: JADWAL SAYA PANEL ============
    private JPanel buildJadwalSayaPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Inisialisasi model jadwal
        modelJadwal = new DefaultTableModel(
            new String[]{"ID","Tanggal","Jam Mulai","Jam Selesai","Status"}, 0);
        tblJadwal.setModel(modelJadwal);
        tblJadwal.setRowHeight(35);
        tblJadwal.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTambah = new JButton("+ Tambah Jadwal");
        JButton btnHapus = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");
        
        styleButton(btnTambah, new Color(40, 167, 69));
        styleButton(btnHapus, new Color(238, 67, 67));
        styleButton(btnRefresh, new Color(108, 117, 125));
        
        btnPanel.add(btnTambah);
        btnPanel.add(btnHapus);
        btnPanel.add(btnRefresh);

        p.add(new JScrollPane(tblJadwal), BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        btnTambah.addActionListener(e -> formTambahJadwal());
        btnHapus.addActionListener(e -> {
            int r = tblJadwal.getSelectedRow();
            if (r < 0) { 
                JOptionPane.showMessageDialog(this, "Pilih jadwal dulu!"); 
                return; 
            }
            int id = (int) tblJadwal.getValueAt(r, 0);
            if (JOptionPane.showConfirmDialog(this, "Hapus jadwal ini?", "Konfirmasi", 
                    JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    jadwalDao.deleteJadwal(id);
                    loadJadwalSaya();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        btnRefresh.addActionListener(e -> loadJadwalSaya());

        return p;
    }

    private void loadJadwalSaya() {
        modelJadwal.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            for (JadwalKonsultasi j : jadwalDao.getJadwalByDosenId(dosen.getId())) {
                // Format tanggal: dd/MM/yyyy
                String tglFormat = sdf.format(j.getTanggal());
                
                // Format jam: HH:mm
                String jamMulai = j.getJamMulai().toString().substring(0, 5);
                String jamSelesai = j.getJamSelesai().toString().substring(0, 5);
                
                modelJadwal.addRow(new Object[]{
                    j.getIdJadwal(),
                    tglFormat,      // Format: 30/06/2026
                    jamMulai,       // Format: 08:00
                    jamSelesai,     // Format: 10:00
                    j.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading jadwal: " + e.getMessage());
        }
    }

    private void formTambahJadwal() {
        JTextField txtTgl = new JTextField("2026-06-30");
        JTextField txtMulai = new JTextField("08:00");
        JTextField txtSelesai = new JTextField("10:00");

        Object[] fields = {
            "Tanggal (YYYY-MM-DD):", txtTgl,
            "Jam Mulai (HH:MM):", txtMulai,
            "Jam Selesai (HH:MM):", txtSelesai
        };
        
        if (JOptionPane.showConfirmDialog(this, fields, "Tambah Jadwal",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                JadwalKonsultasi j = new JadwalKonsultasi();
                j.setDosenId(dosen.getId());
                j.setTanggal(Date.valueOf(txtTgl.getText()));
                j.setJamMulai(Time.valueOf(txtMulai.getText() + ":00"));
                j.setJamSelesai(Time.valueOf(txtSelesai.getText() + ":00"));
                j.setStatus("TERSEDIA");
                
                jadwalDao.tambahJadwal(j);
                JOptionPane.showMessageDialog(this, "Jadwal berhasil ditambahkan!");
                loadJadwalSaya();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ============ BUTTON RENDERER & EDITOR ============
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 11));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Detail");
            setBackground(new Color(67, 97, 238));
            setForeground(Color.WHITE);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.table = table;
            label = "Detail";
            button.setText(label);
            button.setBackground(new Color(67, 97, 238));
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed && table != null) {
                int row = table.getSelectedRow();
                if (row >= 0 && row < table.getRowCount()) {
                    int idBooking = (int) table.getValueAt(row, 0);
                    try {
                        Booking b = bookingDao.getAllBooking().stream()
                            .filter(booking -> booking.getIdBooking() == idBooking)
                            .findFirst().orElse(null);
                        if (b != null) {
                            showDetail(b);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        private void showDetail(Booking b) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            // Format tanggal booking
            String tglFormat = b.getCreatedAt() != null ? sdf.format(b.getCreatedAt()) : "-";
            
            // Cek lokasi dengan lebih detail
            String lokasi = b.getLokasi();
            String lokasiText = "Belum ditentukan";
            if (lokasi != null && !lokasi.trim().isEmpty() && !lokasi.equals("-")) {
                lokasiText = lokasi;
            }
            
            String msg = String.format(
                "Detail Booking:\n\n" +
                "ID: %d\n" +
                "Mahasiswa: %s\n" +
                "Topik: %s\n" +
                "Status: %s\n" +
                "Lokasi: %s\n" +
                "Tanggal Booking: %s",
                b.getIdBooking(),
                b.getNamaMahasiswa(),
                b.getTopikSkripsi(),
                b.getStatusBooking(),
                lokasiText,
                tglFormat
            );
            JOptionPane.showMessageDialog(button, msg, "Detail Booking", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}