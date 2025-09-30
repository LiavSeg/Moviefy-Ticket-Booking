package moviefyPackge.moviefy.domain.dto.SeatAllocationDto;

import moviefyPackge.moviefy.enums.SeatState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the availability status of a single seat in a showtime.
 * Fields:
 * - seatLabel: Unique label identifying the seat (e.g., "B4", "C7"). Required.
 * - state: The current state of the seat (AVAILABLE, RESERVED, SOLD). Required.
 * - expiresAt: The expiration timestamp for the reservation, if the seat is RESERVED. Null otherwise.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatAvailabilityDto {
    private String seatLabel;
    private SeatState state;
    private LocalDateTime expiresAt;
}
