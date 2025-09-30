package moviefyPackge.moviefy.domain.dto.SeatAllocationDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Represents the seat availability response for a specific showtime.
 * Fields:
 * - showtimeId: The unique identifier of the showtime for which seat availability is being requested.
 * - seats: A list of seat availability information, each represented by a SeatAvailabilityDto.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatAvailabilityResponseDto {

    @NotNull(message = "Showtime ID is required")
    private Integer showtimeId;
    private List<SeatAvailabilityDto> seats;
}
