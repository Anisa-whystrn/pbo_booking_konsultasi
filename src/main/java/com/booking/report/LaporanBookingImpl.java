package com.booking.report;

import com.booking.model.User;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;  // ✅ Ganti dari BaseColor ke Color
import java.io.FileOutputStream;
import java.util.List;

public class LaporanBookingImpl implements LaporanService {
    
    @Override
    public void generatePDF(String outputPath) throws Exception {
        // Untuk booking report (jika diperlukan)
    }
    
    // Method untuk export laporan user lengkap
    public void generateLaporanUserPDF(String outputPath, List<User> users, 
            int adminCount, int dosenCount, int mhsCount) throws Exception {
        
        Document document = new Document(PageSize.A4.rotate()); // Landscape
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        
        document.open();
        
        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("LAPORAN DATA USER", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);
        
        // Subtitle
        Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph subtitle = new Paragraph("Sistem Booking Konsultasi", subFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);
        
        // Statistik Summary
        PdfPTable summaryTable = new PdfPTable(3);
        summaryTable.setWidthPercentage(60);
        summaryTable.setSpacingBefore(10);
        summaryTable.setSpacingAfter(20);
        
        // ✅ Ganti BaseColor dengan Color
        PdfPCell cell = new PdfPCell(new Phrase("Statistik User", 
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new Color(67, 97, 238));  // ✅ Pakai Color
        cell.setPadding(10);
        summaryTable.addCell(cell);
        
        summaryTable.addCell("Admin");
        summaryTable.addCell("Dosen");
        summaryTable.addCell("Mahasiswa");
        
        summaryTable.addCell(String.valueOf(adminCount));
        summaryTable.addCell(String.valueOf(dosenCount));
        summaryTable.addCell(String.valueOf(mhsCount));
        
        document.add(summaryTable);
        
        // Detail User Table
        PdfPTable table = new PdfPTable(5); // ID, Username, Email, Role, NIM/NIP
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setSpacingAfter(20);
        
        // Set column widths
        float[] columnWidths = {10f, 20f, 30f, 15f, 25f};
        table.setWidths(columnWidths);
        
        // Header
        String[] headers = {"ID", "Username", "Email", "Role", "NIM/NIP"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            // ✅ Ganti BaseColor dengan Color
            headerCell.setBackgroundColor(new Color(240, 240, 240));  // ✅ Pakai Color
            headerCell.setPadding(8);
            table.addCell(headerCell);
        }
        
        // Data rows
        for (User u : users) {
            table.addCell(String.valueOf(u.getId()));
            table.addCell(u.getUsername());
            table.addCell(u.getEmail());
            table.addCell(u.getRole());
            table.addCell(u.getNimNip() != null ? u.getNimNip() : "-");
        }
        
        document.add(table);
        
        // Footer
        Paragraph footer = new Paragraph("\n\nLaporan ini dibuat secara otomatis oleh sistem pada " + 
            new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()), 
            FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
        
        document.close();
    }
}