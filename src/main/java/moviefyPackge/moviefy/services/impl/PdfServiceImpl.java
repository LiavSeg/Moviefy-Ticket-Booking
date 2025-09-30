package moviefyPackge.moviefy.services.impl;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.services.PdfService;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.List;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PdfServiceImpl implements PdfService {

    @Override
    public byte[] generateTicketPdf(BookingEntity booking) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Rectangle page = PageSize.A6.rotate();
        Document document = new Document(page, 18f, 18f, 18f, 18f);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.addTitle("Moviefy E-Ticket");
        document.addAuthor("Moviefy");
        document.addCreationDate();

        document.open();

        final BaseColor PRIMARY = new BaseColor(17, 24, 39);
        final BaseColor ACCENT  = new BaseColor(79, 70, 229);
        final BaseColor TEXT    = new BaseColor(55, 65, 81);
        final BaseColor MUTED   = new BaseColor(107, 114, 128);
        final BaseColor BORDER  = new BaseColor(229, 231, 235);
        final BaseColor CARD_BG = new BaseColor(255, 255, 255);
        final BaseColor PAGE_BG = new BaseColor(245, 247, 250);
        final BaseColor SHADOW  = new BaseColor(0, 0, 0, 40);

        Font h1      = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, PRIMARY);
        Font h2      = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, ACCENT);
        Font labelF  = new Font(Font.FontFamily.HELVETICA, 9.5f, Font.BOLD, ACCENT);
        Font valueF  = new Font(Font.FontFamily.HELVETICA, 9.5f, Font.NORMAL, TEXT);
        Font small   = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, MUTED);

        PdfContentByte under = writer.getDirectContentUnder();
        fillRect(under, PAGE_BG, document.left() - 10, document.bottom() - 10,
                document.right() - document.left() + 20, document.top() - document.bottom() + 20);

        float cardX = document.left();
        float cardY = document.bottom();
        float cardW = document.right() - document.left();
        float cardH = document.top() - document.bottom();
        float radius = 12f;

        drawRoundedRect(under, SHADOW, cardX + 3f, cardY - 2f, cardW, cardH, radius);

        drawRoundedRect(under, CARD_BG, cardX, cardY, cardW, cardH, radius);

        drawRoundedTopBand(under, ACCENT, cardX, cardY + cardH - 28f, cardW, radius);

        PdfPTable header = new PdfPTable(2);
        header.setTotalWidth(cardW - 20f);
        header.setLockedWidth(true);
        header.setWidths(new float[]{12f, 88f});

        PdfPCell logoCell = noBorderCell();
        try {
            java.net.URL res = getClass().getClassLoader().getResource("logo.png");
            if (res != null) {
                Image logo = Image.getInstance(res);
                logo.scaleToFit(20, 20);
                logoCell = noBorderCell(logo);
            }
        } catch (Exception ignore) {}
        logoCell.setPadding(4f);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        Font titleWhite = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, BaseColor.WHITE);
        PdfPCell titleCell = noBorderCell(new Phrase("MOVIEFY TICKET", titleWhite));
        titleCell.setPadding(6f);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        header.addCell(logoCell);
        header.addCell(titleCell);

        header.writeSelectedRows(0, -1, cardX + 10f, cardY + cardH - 6f, writer.getDirectContent());

        document.add(new Paragraph(" "));

        MovieEntity movie = booking.getShowtime().getMovie();
        ShowtimeEntity showtime = booking.getShowtime();
        LocalDateTime start = showtime.getStartTime();
        LocalDateTime end   = start.plusMinutes(movie.getDuration());

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        Map<String, List<String>> seatsByRow = new TreeMap<>();
        for (String seat : booking.getSeatList()) {
            String[] parts = seat.split("-");
            if (parts.length == 2) seatsByRow.computeIfAbsent(parts[0], k -> new ArrayList<>()).add(parts[1]);
        }
        StringBuilder seatBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> e : seatsByRow.entrySet()) {
            if (!seatBuilder.isEmpty()) seatBuilder.append("\n");
            seatBuilder.append("Row ").append(e.getKey()).append(": ").append(String.join(", ", e.getValue()));
        }

        PdfPTable grid = new PdfPTable(2);
        grid.setWidthPercentage(100);
        grid.setWidths(new float[]{62f, 38f});
        grid.setSpacingBefore(6f);

        PdfPTable details = new PdfPTable(2);
        details.setWidthPercentage(100);
        details.setWidths(new float[]{28f, 72f});

        details.addCell(kvCell("Movie",   movie.getTitle(), labelF, valueF, BORDER));
        details.addCell(kvCell("Date",    start.format(dateFmt), labelF, valueF, BORDER));
        details.addCell(kvCell("Time",    start.format(timeFmt) + " – " + end.format(timeFmt), labelF, valueF, BORDER));
        details.addCell(kvCell("Theater", showtime.getTheater(), labelF, valueF, BORDER));
        details.addCell(kvCell("Seats",   formatSeats(booking.getSeatList()), labelF, valueF, BORDER));
        details.addCell(kvCell("Total",   String.format("₪ %.2f", booking.getTotalPrice()), labelF, valueF, BORDER));

        PdfPCell detailsWrap = new PdfPCell(details);
        detailsWrap.setBorder(Rectangle.NO_BORDER);
        detailsWrap.setPadding(8f);
        grid.addCell(detailsWrap);

        PdfPTable qrPanel = new PdfPTable(1);
        qrPanel.setWidthPercentage(100);

        PdfPCell qrTitle = noBorderCell(new Phrase("SCAN AT ENTRANCE", h2));
        qrTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        qrTitle.setPaddingBottom(4f);
        qrPanel.addCell(qrTitle);

        String qrPayload = "Booking: " + booking.getBookingId() + "\n" +
                "Movie: " + movie.getTitle() + "\n" +
                "Showtime: " + start.format(dateFmt) + " " + start.format(timeFmt) + "\n" +
                "Theater: " + showtime.getTheater() + "\n" +
                "Seats: " +  formatSeats(booking.getSeatList());

        com.itextpdf.text.pdf.BarcodeQRCode qr = new com.itextpdf.text.pdf.BarcodeQRCode(qrPayload, 120, 120, null);
        Image qrImg = qr.getImage();
        qrImg.scaleToFit(120, 120);

        PdfPCell qrImgCell = noBorderCell(qrImg);
        qrImgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        qrImgCell.setPadding(4f);
        qrPanel.addCell(qrImgCell);

        PdfPCell qrNote = noBorderCell(new Phrase("Keep this ticket ready on your device.", small));
        qrNote.setHorizontalAlignment(Element.ALIGN_CENTER);
        qrPanel.addCell(qrNote);

        PdfPCell qrWrap = new PdfPCell(qrPanel);
        qrWrap.setBorder(Rectangle.NO_BORDER);
        qrWrap.setPadding(8f);
        grid.addCell(qrWrap);

        document.add(grid);

        document.add(divider(BORDER));

        Paragraph footer = new Paragraph(
                String.format("Booking ID: %s    •    Issued: %s",
                        booking.getBookingId(),
                        java.time.ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm 'UTC'X"))
                ),
                small
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(4f);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }


    private void drawRoundedRect(PdfContentByte cb, BaseColor fill, float x, float y, float w, float h, float r) {
        cb.saveState();
        cb.setColorFill(fill);
        cb.roundRectangle(x, y, w, h, r);
        cb.fill();
        cb.restoreState();
    }

    private void drawRoundedTopBand(PdfContentByte cb, BaseColor fill, float x, float y, float w, float r) {
        cb.saveState();
        cb.setColorFill(fill);
        cb.moveTo(x, y);
        cb.lineTo(x, y + (float) 28.0 - r);
        cb.curveTo(x, y + (float) 28.0, x, y + (float) 28.0, x + r, y + (float) 28.0);
        cb.lineTo(x + w - r, y + (float) 28.0);
        cb.curveTo(x + w, y + (float) 28.0, x + w, y + (float) 28.0, x + w, y + (float) 28.0 - r);
        cb.lineTo(x + w, y);
        cb.lineTo(x, y);
        cb.fill();
        cb.restoreState();
    }

    private void fillRect(PdfContentByte cb, BaseColor fill, float x, float y, float w, float h) {
        cb.saveState();
        cb.setColorFill(fill);
        cb.rectangle(x, y, w, h);
        cb.fill();
        cb.restoreState();
    }

    private PdfPTable divider(BaseColor color) throws DocumentException {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        PdfPCell line = new PdfPCell(new Phrase(" "));
        line.setMinimumHeight(1f);
        line.setBorder(Rectangle.BOTTOM);
        line.setBorderColor(color);
        line.setPadding(0);
        t.addCell(line);
        return t;
    }

    private PdfPCell noBorderCell() {
        PdfPCell c = new PdfPCell(new Phrase(""));
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private PdfPCell noBorderCell(Image img) {
        PdfPCell c = new PdfPCell(img, false);
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private PdfPCell noBorderCell(Phrase phrase) {
        PdfPCell c = new PdfPCell(phrase);
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private PdfPCell kvCell(String label, String value, Font labelFont, Font valueFont, BaseColor border) {
        PdfPTable inner = new PdfPTable(1);
        inner.setWidthPercentage(100);

        PdfPCell l = new PdfPCell(new Phrase(label.toUpperCase(), labelFont));
        l.setBorder(Rectangle.NO_BORDER);
        l.setPaddingBottom(1.5f);

        PdfPCell v = new PdfPCell(new Phrase(value, valueFont));
        v.setBorder(Rectangle.NO_BORDER);
        v.setPaddingBottom(5f);

        inner.addCell(l);
        inner.addCell(v);

        PdfPCell wrap = new PdfPCell(inner);
        wrap.setPadding(6f);
        wrap.setBorder(Rectangle.BOX);
        wrap.setBorderColor(border);
        wrap.setBackgroundColor(BaseColor.WHITE);
        return wrap;
    }
    private String formatSeats(List<String> seatList) {
        List<String> formatted = new ArrayList<>();
        for (String seat : seatList) {
            String[] parts = seat.split("-");
            if (parts.length == 2) {
                String row = parts[0];
                String num = parts[1];
                formatted.add(row + num);
            }
        }
        return String.join(", ", formatted);
    }
}
