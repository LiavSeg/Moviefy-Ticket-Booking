package moviefyPackge.moviefy.services;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.dto.SeatAllocationDto.SeatAvailabilityResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SeatAllocationService {
     void holdSeats(Integer showtimeId, List<String> seatLabels);
     SeatAvailabilityResponseDto getAvailability(Integer showtimeId);
     void confirmSeats(Integer showtimeId, List<String> seatLabels, BookingEntity booking) ;
}
