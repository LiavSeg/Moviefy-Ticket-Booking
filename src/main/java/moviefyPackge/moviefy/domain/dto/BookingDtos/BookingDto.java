package moviefyPackge.moviefy.domain.dto.BookingDtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
/**
 * Represents a single booking record returned to the client.
 * Fields:
 * - userName: The name of the user who placed the booking.
 * - bookingId: The unique identifier for the booking.
 * - showtimeId: The ID of the showtime that was booked.
 * - movieTitle: Title of the booked movie.
 * - bookingDateTime: Full timestamp when the booking was placed.
 * - totalPrice: Final price calculated for the booking.
 * - seatList: List of seat labels that were reserved .
 * - timeOfOrder: Local time representation of the booking.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private String userName;
    private String bookingId;
    private Integer showtimeId;
    private String movieTitle;
    private String bookingDateTime;
    private Double totalPrice;
    private List<String> seatList;
    private String timeOfOrder;
}
