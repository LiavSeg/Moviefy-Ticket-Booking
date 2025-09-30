package moviefyPackge.moviefy.domain.Entities;

import moviefyPackge.moviefy.enums.SeatState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "seat_allocation",
        uniqueConstraints = @UniqueConstraint(columnNames = {"showtime_id", "seat_label"})
)
public class SeatAllocation {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private ShowtimeEntity showtime;

    private String seatLabel;

    @Enumerated(EnumType.STRING)
    private SeatState state;

    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;

}
