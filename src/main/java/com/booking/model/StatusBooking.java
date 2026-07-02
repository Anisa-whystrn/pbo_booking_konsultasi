package com.booking.model;

/**
 * Enum StatusBooking - Mengatur status proses konsultasi
 * Tugas: Salwa
 */
public enum StatusBooking {
    PENDING("Menunggu Persetujuan"),
    DISETUJUI("Disetujui Dosen"),
    DITOLAK("Ditolak Dosen"),
    SELESAI("Konsultasi Selesai");

    private final String deskripsi;

    StatusBooking(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    @Override
    public String toString() {
        return deskripsi;
    }

    /**
     * Method untuk mendapatkan enum dari string database
     */
    public static StatusBooking fromString(String status) {
        switch (status.toUpperCase()) {
            case "PENDING": return PENDING;
            case "DISETUJUI": return DISETUJUI;
            case "DITOLAK": return DITOLAK;
            case "SELESAI": return SELESAI;
            default: return PENDING;
        }
    }
}