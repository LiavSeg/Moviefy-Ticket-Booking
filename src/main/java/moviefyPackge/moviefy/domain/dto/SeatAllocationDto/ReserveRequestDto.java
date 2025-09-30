package moviefyPackge.moviefy.domain.dto.SeatAllocationDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


/**
 * Represents a request to reserve one or more seats for a specific showtime.
 * Fields:
 * - showtimeId: The ID of the showtime for which the reservation is requested. Must not be null.
 * - seatLabels: A list of seat labels to be reserved. Must not be empty.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRequestDto {
    @NotNull(message = "Showtime ID is required")
    private Integer showtimeId;
    @NotEmpty(message = "Seat labels list must not be empty")
    private List<String> seatLabels;
}
