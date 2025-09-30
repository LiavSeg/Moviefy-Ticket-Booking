package moviefyPackge.moviefy.services.Util;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import io.micrometer.common.lang.Nullable;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailSenderHandler {

   private final org.springframework.mail.javamail.JavaMailSender mailSender;

    public EmailSenderHandler(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmationEmail(String to, BookingEntity booking, byte[] pdfBytes) throws Exception {
        var tpl = EmailTemplateLoader.load("XML/Email/ConfirmationTemplate.xml");

        MimeMessage mime = mailSender.createMimeMessage();

        String logoCid = null;
        MimeMessageHelper helper;
        try {
            new ClassPathResource("logo.png").getInputStream().close();
            helper = new MimeMessageHelper(mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            logoCid = "moviefy-logo";
        } catch (Exception notFound) {
            helper = new MimeMessageHelper(mime, true, "UTF-8");
        }

        helper.setFrom(new InternetAddress(tpl.from, tpl.fromName));
        helper.setTo(to);
        helper.setSubject(tpl.subject);
        helper.setValidateAddresses(true);

        var values = EmailValuesFactory.fromBooking(booking, logoCid);

        String html = replacePlaceholders(tpl.html, values);

        helper.setText(html, true);

        if (logoCid != null) {
            try {
                helper.addInline(logoCid, new ClassPathResource("logo.png"), "image/png");
            } catch (Exception ignore) {}
        }

        helper.addAttachment("ticket.pdf", new ByteArrayResource(pdfBytes));
        mailSender.send(mime);
    }

    private static String replacePlaceholders(String template, Map<String, String> values) {
        String out = template;
        for (var e : values.entrySet()) {
            out = out.replace("{{" + e.getKey() + "}}", e.getValue());
        }
        return out;
    }

    static final class EmailValuesFactory {
        static Map<String, String> fromBooking(BookingEntity booking, @Nullable String logoCid) {
            var m = new LinkedHashMap<String, String>();

            var dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            var timeFmt = DateTimeFormatter.ofPattern("HH:mm");

            var start = booking.getShowtime().getStartTime();
            var date  = start.format(dateFmt);
            var time  = start.format(timeFmt);

            Integer durationMin = booking.getShowtime().getMovie().getDuration();
            String endTime = (durationMin != null) ? start.plusMinutes(durationMin).format(timeFmt) : null;

            NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("he", "IL"));
            currency.setRoundingMode(RoundingMode.HALF_UP);
            String price = currency.format(booking.getTotalPrice());

            String movie   = escape(booking.getShowtime().getMovie().getTitle());
            String theater = escape(booking.getShowtime().getTheater());
            String seats   = escape(String.join(", ", booking.getSeatList()));
            String user    = escape(booking.getUser().getUserName());

            String endsRow = (endTime != null)
              ? "<tr><td><strong>Ends</strong></td><td>" + escape(endTime) + "</td></tr>"
              : "";

            String logoBlock = (logoCid != null)
              ? "<img alt=\"Moviefy\" src=\"cid:" + logoCid + "\" style=\"width:80px;height:auto;display:block;margin:0 auto;\"/>"
              : "";

            m.put("USER_NAME", user);
            m.put("MOVIE", movie);
            m.put("DATE", date);
            m.put("TIME", time);
            m.put("ENDS_ROW", endsRow);
            m.put("THEATER", theater);
            m.put("SEATS", seats);
            m.put("TOTAL", escape(price));
            m.put("LOGO_BLOCK", logoBlock);
            return m;
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");
    }
}

