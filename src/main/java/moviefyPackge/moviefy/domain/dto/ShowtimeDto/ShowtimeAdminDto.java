package moviefyPackge.moviefy.domain.dto.ShowtimeDto;

import moviefyPackge.moviefy.domain.dto.MoviteDtos.MovieDto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data transfer object representing a showtime in the admin context.
 * Fields:
 * - id: Unique identifier of the showtime.
 * - price: Ticket price for this showtime.
 * - movie: The full movie data associated with the showtime.
 * - theater: Name or identifier of the theater hall where the showtime is scheduled.
 * - startTime: Start time of the showtime, represented as an ISO-8601 formatted string.
 * - endTime: End time of the showtime, represented as an ISO-8601 formatted string.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeAdminDto {

    private Integer id;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private Double price;

    @NotNull(message = "Movie is required")
    private MovieDto movie;

    @NotBlank(message = "Theater is required")
    private String theater;

    @NotBlank(message = "Start time is required")
    private String startTime;

    @NotBlank(message = "End time is required")
    private String endTime;
}
