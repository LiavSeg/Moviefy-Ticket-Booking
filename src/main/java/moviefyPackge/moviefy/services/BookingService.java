package moviefyPackge.moviefy.services;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.BookingByMonthDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.IncomeRangeDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public interface BookingService {
    BookingEntity createBooking(BookingEntity bookingEntity);
    BookingEntity findBooking(UUID bookingId);
    List<BookingDto> getAllUserBooking(UUID user);
    List<String> getSeatsForBookingByMovieId(Integer movieId);
    List<BookingByMonthDto> getMonthBookingsStats(LocalDateTime start,LocalDateTime end) throws Exception;
    List<IncomeRangeDto> getIncomeRangeStats(LocalDateTime start, LocalDateTime end) throws Exception;
    Boolean isEligibleToReview(UUID userId,Integer movieId);


}
