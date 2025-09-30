package moviefyPackge.moviefy.repositories;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.BookingByMonthDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.IncomeRangeDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
/**
 * Repository interface for managing booking data in the database.
 * Provides methods for basic CRUD operations, user-based lookups,
 * showtime-based deletions, and custom reporting queries using native SQL.
 */
@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, BookingDto> {
    Iterable<BookingEntity> findAllBookingsByShowtimeId(Integer showtime_id);
    void deleteAllByShowtimeId(Integer showtimeId);
    List<BookingEntity> findByUser_UserId(UUID userId);

    /**
     * Retrieves the number of bookings per month within a given range.
     * @param start start date of the range
     * @param end end date of the range
     * @return monthly booking counts grouped by "Mon YYYY"
     */
    @Query(value = """
        SELECT
            TO_CHAR(b.booking_time, 'Mon YYYY') AS label,
            COUNT(*) AS count
        FROM bookings b
        WHERE b.booking_time BETWEEN :start AND :end
        GROUP BY TO_CHAR(b.booking_time, 'Mon YYYY')
        ORDER BY MIN(b.booking_time)
    """, nativeQuery = true)
    List<BookingByMonthDto> findAllByBookingTimeBetween(@Param("start") LocalDateTime start,
                                                        @Param("end") LocalDateTime end);
    /**
     * Calculates income per month for bookings in a date range.
     * @param start start date of the range
     * @param end end date of the range
     * @return monthly income grouped by "Mon YYYY"
     */
    @Query(value = """
        SELECT
            TO_CHAR(b.booking_time, 'Mon YYYY') AS month,
            sum(b.total_price) AS income
        FROM bookings b
        WHERE b.booking_time BETWEEN :start AND :end
        GROUP BY TO_CHAR(b.booking_time, 'Mon YYYY')
        ORDER BY MIN(b.booking_time)
    """, nativeQuery = true)
    List<IncomeRangeDto> findIncomeInRange(@Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);

    BookingEntity findByBookingId(UUID bookingId);
    List<BookingEntity> findBookingEntitiesByBookingTimeBetween(LocalDateTime from,LocalDateTime to);

}