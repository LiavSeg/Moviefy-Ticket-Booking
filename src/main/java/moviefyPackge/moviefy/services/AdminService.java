package moviefyPackge.moviefy.services;

import moviefyPackge.moviefy.domain.dto.StatsDtos.BookingByMonthDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.IncomeRangeDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface AdminService {
    List<BookingByMonthDto> getBookingsPerMonth(LocalDateTime start,LocalDateTime end) throws Exception;
    List<IncomeRangeDto> getIncomeByRange(LocalDateTime start, LocalDateTime end) throws Exception;
    List<?> getHighestIncomeMovieRange(LocalDateTime start, LocalDateTime end);
}
