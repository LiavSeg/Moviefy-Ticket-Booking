package moviefyPackge.moviefy.domain.dto.ReportsDto.BookingReports;

import moviefyPackge.moviefy.services.Util.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Contains booking details for inclusion in a generated report (e.g., XML export).
 * Fields:
 * - bookingId: Unique identifier of the booking.
 * - userId: Identifier of the user who made the booking.
 * - username: Display name of the user.
 * - movieTitle: Title of the booked movie.
 * - showtime: Start time of the show, formatted for XML using a custom adapter.
 * - theater: Name of the theater or hall.
 * - seats: List of booked seat labels (e.g., "A1", "B2").
 * - price: Total cost of the booking.
 * - createdAt: Timestamp when the booking was made, adapted for XML serialization.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)

public class BookingItemReport {

    private String bookingId;
    private String userId;
    private String username;
    private String movieTitle;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime showtime;

    private String theater;

    @XmlElementWrapper(name="seats")
    @XmlElement(name = "seat")
    private List<String> seats;

    @XmlElement(name = "price")
    private Double price;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime createdAt;
}
