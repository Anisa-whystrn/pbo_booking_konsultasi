package com.booking.service;

import com.booking.dao.BookingDao;
import com.booking.model.Booking;
import com.booking.model.StatusBooking;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * AntrianKonsultasi - Mengelola antrean mahasiswa yang menunggu konsultasi
 * Tugas: Citra Ananda Pratiwi
 */
public class AntrianKonsultasi {
    private BookingDao bookingDao;
    private List<Booking> antrian;

    public AntrianKonsultasi() {
        this.bookingDao = new BookingDao();
        this.antrian = new ArrayList<>();
    }

    /**
     * Memuat antrean untuk dosen tertentu
     */
    public void muatAntrean(int dosenId) {
        antrian.clear();
        try {
            List<Booking> bookings = bookingDao.getBookingByDosenId(dosenId);
            
            // Filter hanya yang PENDING atau DISETUJUI (belum selesai)
            for (Booking b : bookings) {
                if (b.getStatusBooking().equals(StatusBooking.PENDING.name()) || 
                    b.getStatusBooking().equals(StatusBooking.DISETUJUI.name())) {
                    antrian.add(b);
                }
            }
            
            // Urutkan berdasarkan waktu booking (yang paling lama di antrean pertama)
            Collections.sort(antrian, new Comparator<Booking>() {
                @Override
                public int compare(Booking b1, Booking b2) {
                    return b1.getCreatedAt().compareTo(b2.getCreatedAt());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mendapatkan daftar antrean
     */
    public List<Booking> getAntrian() {
        return antrian;
    }

    /**
     * Mendapatkan jumlah antrean
     */
    public int getJumlahAntrean() {
        return antrian.size();
    }

    /**
     * Mendapatkan posisi antrean mahasiswa tertentu
     */
    public int getPosisiAntrean(int mahasiswaId) {
        for (int i = 0; i < antrian.size(); i++) {
            if (antrian.get(i).getMahasiswaId() == mahasiswaId) {
                return i + 1; // Posisi dimulai dari 1
            }
        }
        return -1; // Tidak ada dalam antrean
    }

    /**
     * Menampilkan antrean
     */
    public void tampilkanAntrean() {
        if (antrian.isEmpty()) {
            System.out.println("Tidak ada antrean konsultasi.");
            return;
        }
        
        System.out.println("=== ANTREAN KONSULTASI ===");
        System.out.println("No. Antre | Mahasiswa | NIM | Topik | Status");
        System.out.println("------------------------------------------------");
        
        int nomor = 1;
        for (Booking b : antrian) {
            System.out.printf("%d | %s | %s | %s | %s\n",
                nomor++,
                b.getNamaMahasiswa(),
                b.getNimMahasiswa() != null ? b.getNimMahasiswa() : "-",
                b.getTopikSkripsi(),
                b.getStatusBooking()
            );
        }
    }

    /**
     * Mendapatkan booking pertama dalam antrean
     */
    public Booking getBookingPertama() {
        if (antrian.isEmpty()) {
            return null;
        }
        return antrian.get(0);
    }

    /**
     * Refresh antrean dari database
     */
    public void refresh(int dosenId) {
        muatAntrean(dosenId);
    }
}