package com.booking.service;

import com.booking.dao.BookingDao;
import com.booking.dao.JadwalDao;
import com.booking.model.Booking;
import com.booking.model.StatusBooking;
import java.sql.SQLException;
import java.util.List;

/**
 * BookingService - Mengatur logika bisnis terkait booking konsultasi
 * Tugas: Citra Ananda Pratiwi
 */
public class BookingService {
    private BookingDao bookingDao;
    private JadwalDao jadwalDao;

    public BookingService() {
        this.bookingDao = new BookingDao();
        this.jadwalDao = new JadwalDao();
    }

    /**
     * Melakukan booking konsultasi
     */
    public boolean bookingKonsultasi(Booking booking) {
        try {
            bookingDao.buatBooking(booking);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menyetujui booking oleh dosen
     */
    public boolean setujuiBooking(int idBooking) {
        try {
            bookingDao.updateStatusBooking(idBooking, StatusBooking.DISETUJUI.name());
            
            // Update status jadwal jadi TERBOOKING
            Booking b = getBookingById(idBooking);
            if (b != null) {
                jadwalDao.updateStatus(b.getJadwalId(), "TERBOOKING");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menolak booking oleh dosen
     */
    public boolean tolakBooking(int idBooking) {
        try {
            bookingDao.updateStatusBooking(idBooking, StatusBooking.DITOLAK.name());
            
            // Kembalikan status jadwal jadi TERSEDIA
            Booking b = getBookingById(idBooking);
            if (b != null) {
                jadwalDao.updateStatus(b.getJadwalId(), "TERSEDIA");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menandai konsultasi sebagai selesai
     */
    public boolean selesaiKonsultasi(int idBooking) {
        try {
            bookingDao.updateStatusBooking(idBooking, StatusBooking.SELESAI.name());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mendapatkan booking berdasarkan ID
     */
    public Booking getBookingById(int idBooking) {
        try {
            List<Booking> allBookings = bookingDao.getAllBooking();
            return allBookings.stream()
                .filter(b -> b.getIdBooking() == idBooking)
                .findFirst()
                .orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Menampilkan semua booking
     */
    public List<Booking> tampilBooking() {
        try {
            return bookingDao.getAllBooking();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Mendapatkan booking berdasarkan dosen
     */
    public List<Booking> getBookingByDosen(int dosenId) {
        try {
            return bookingDao.getBookingByDosenId(dosenId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Mendapatkan booking berdasarkan mahasiswa
     */
    public List<Booking> getBookingByMahasiswa(int mhsId) {
        try {
            return bookingDao.getBookingByMahasiswaId(mhsId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update lokasi bimbingan
     */
    public boolean updateLokasi(int idBooking, String lokasi) {
        try {
            bookingDao.updateLokasi(idBooking, lokasi);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

 

    /**
     * Update topik booking (oleh mahasiswa)
     */
    public boolean updateTopikBooking(int idBooking, String topik) {
        try {
            bookingDao.updateTopikBooking(idBooking, topik);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Membatalkan booking (oleh mahasiswa)
     */
    public boolean batalkanBooking(int idBooking) {
        try {
            Booking b = getBookingById(idBooking);
            if (b != null && b.getStatusBooking().equals(StatusBooking.PENDING.name())) {
                bookingDao.cancelBooking(idBooking);
                jadwalDao.updateStatus(b.getJadwalId(), "TERSEDIA");
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}