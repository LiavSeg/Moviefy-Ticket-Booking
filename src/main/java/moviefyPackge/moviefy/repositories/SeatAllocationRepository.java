package moviefyPackge.moviefy.repositories;

import moviefyPackge.moviefy.domain.Entities.SeatAllocation;
import moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports.PopularMovieItemDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
/**
 * Repository interface for managing seat allocation entities.
 * Includes methods for pessimistic locking during seat reservation,
 * releasing expired reservations, and generating aggregated statistics
 * for popular movies by bookings and sold/reserved seats.
 */
@Repository
public interface SeatAllocationRepository extends JpaRepository<SeatAllocation, UUID> {

    /**
     * Locks selected seats for a showtime to prevent concurrent modification.
     * @param showtimeId the ID of the showtime
     * @param seatLabels list of seat labels to lock
     * @return list of locked SeatAllocation entities
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
      SELECT s FROM SeatAllocation s
      WHERE s.showtime.id = :showtimeId AND s.seatLabel IN :seatLabels
    """)
    List<SeatAllocation> lockByShowtimeAndLabels(
            @Param("showtimeId") Integer showtimeId,
            @Param("seatLabels") List<String> seatLabels
    );
    List<SeatAllocation> findByShowtime_Id(Integer showtimeId);

    /**
     * Releases all expired seat reservations by updating their state and expiration.
     * @return number of records updated
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE SeatAllocation s
        SET s.state =  moviefyPackge.moviefy.enums.SeatState.AVAILABLE, s.expiresAt = NULL
        WHERE s.state = moviefyPackge.moviefy.enums.SeatState.RESERVED AND s.expiresAt < CURRENT_TIMESTAMP
    """)
    int releaseExpiredHolds();


    /**
     * Returns a list of popular movies based on seat occupancy and booking data.
     * The results are sorted by total income generated from sold/reserved seats.
     *
     * @param from start date for filtering showtimes
     * @param to end date for filtering showtimes
     * @param pageable pagination settings (limit, offset)
     * @return list of aggregated data about popular movies
     */

    @Query("""
        SELECT new moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports.PopularMovieItemDto(
            m.id,
            m.title,
            COUNT(DISTINCT (b.bookingId)),
            COALESCE(SUM(CASE
                WHEN sa.state = moviefyPackge.moviefy.enums.SeatState.SOLD
                  OR (sa.state = moviefyPackge.moviefy.enums.SeatState.RESERVED AND sa.expiresAt > CURRENT_TIMESTAMP)
                THEN 1 ELSE 0
            END), 0),
            CAST(COALESCE(SUM(CASE
                WHEN sa.state = moviefyPackge.moviefy.enums.SeatState.SOLD
                  OR (sa.state = moviefyPackge.moviefy.enums.SeatState.RESERVED AND sa.expiresAt > CURRENT_TIMESTAMP)
                THEN st.price ELSE 0
            END), 0) AS integer)
        )
        FROM SeatAllocation sa
        JOIN sa.showtime st
        JOIN st.movie m
        LEFT JOIN sa.booking as b
        WHERE st.startTime BETWEEN :from AND :to
        GROUP BY m.id, m.title
        ORDER BY CAST(COALESCE(SUM(CASE
            WHEN sa.state = moviefyPackge.moviefy.enums.SeatState.SOLD
              OR (sa.state = moviefyPackge.moviefy.enums.SeatState.RESERVED AND sa.expiresAt > CURRENT_TIMESTAMP)
            THEN st.price ELSE 0
        END), 0) AS integer) DESC
    """)
    List<PopularMovieItemDto> findTopMoviesByOccupiedSeatsAndBookings(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );
}