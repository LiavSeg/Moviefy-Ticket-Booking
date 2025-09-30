package moviefyPackge.moviefy.domain.dto.BookingDtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;


/**
 *  * Represents a client request to create a new booking in the system.
 * Fields:
 * - userId: The UUID of the user placing the booking.
 * - showtimeId: The ID of the showtime for which the booking is made.
 * - movieTitle: The title of the movie being booked (used for reference only).
 * - theater: The name or code of the theater/hall.
 * - totalPrice: The total price for the entire booking (including all seats).
 * - seats: A list of seat labels selected by the user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    @NotNull(message = "User ID is required")
    private UUID userId;
    @NotNull(message = "Showtime ID is required")
    @Positive(message = "Showtime ID must be a positive number")
    private Integer showtimeId;
    @NotBlank(message = "Movie title must not be blank")
    private String movieTitle;

    @NotBlank(message = "Theater name must not be blank")
    private String theater;
    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be Positive")
    private Double totalPrice;
    @Size(min = 1, message = "At least one seat must be selected")
    private List<@NotBlank(message = "Seat label must not be blank") String> seats;
}