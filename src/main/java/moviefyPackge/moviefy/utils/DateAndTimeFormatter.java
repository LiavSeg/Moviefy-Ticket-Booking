package moviefyPackge.moviefy.utils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility class for formatting LocalDateTime objects into readable strings.
 * Supports parsing a single timestamp or a range, with formatted
 * date and time in English locale (e.g., "Aug 19, 2025 • 14:30").
 */
public class DateAndTimeFormatter {
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;
    /**
     * Initializes the formatter with default patterns:
     * - Date: "MMM d, yyyy"
     * - Time: "HH:mm"
     */
    public DateAndTimeFormatter(){
        this.dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }
    /**
     * Formats a single LocalDateTime into a string with both date and time.
     * @param time the LocalDateTime to format
     * @return formatted string (e.g., "Aug 19, 2025 • 14:30"),
     *         or "UNKOWN" if time is null
     */
    public String parseLocalDateTimeRange(LocalDateTime time){
        if (time==null)
            return "UNKOWN";
        String date = time.format(dateFormatter);
        String hour = time.format(timeFormatter);
        return date + " • " + hour;
    }
    /**
     * Formats a range between two LocalDateTime values.
     * @param start the starting time
     * @param end the ending time
     * @return formatted string combining start date and time,
     *         and end time (e.g., "Aug 19, 2025 • 14:30 - 16:00")
     */
    public String parseLocalDateTimeRange(LocalDateTime start,LocalDateTime end){
        return parseLocalDateTimeRange(start) + " - " + end.format(timeFormatter);
    }

}
