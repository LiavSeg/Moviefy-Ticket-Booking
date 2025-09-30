package moviefyPackge.moviefy.services.Util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDateTime unmarshal(String s) throws Exception {
        return LocalDateTime.parse(s, FORMATTER);
    }

    @Override
    public String marshal(LocalDateTime localDateTime) throws Exception {
        return localDateTime != null ? localDateTime.format(FORMATTER) : null;
    }
}
