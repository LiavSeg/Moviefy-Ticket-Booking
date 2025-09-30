package moviefyPackge.moviefy.domain.dto.ShowtimeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data transfer object representing a single showtime.
 * Fields:
 * - id: Unique identifier for the showtime.
 * - price: Ticket price for the showtime.
 * - movieId: ID of the movie being shown.
 * - theater: Name or code of the theater where the showtime is scheduled.
 * - startTime: Start time of the showtime, as an ISO-8601 formatted string.
 * - endTime: End time of the showtime, as an ISO-8601 formatted string.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeDto {
    private Integer id;
    private Double price;
    private Integer movieId;
    private String theater;
    private String startTime;
    private String endTime;
}
