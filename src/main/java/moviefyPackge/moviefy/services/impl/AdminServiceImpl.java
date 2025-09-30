package moviefyPackge.moviefy.services.impl;

import moviefyPackge.moviefy.domain.dto.MoviteDtos.PopularMovieChartDto;
import moviefyPackge.moviefy.domain.dto.ReportsDto.DateRangeDto;
import moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports.PopularMovieItemDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.BookingByMonthDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.IncomeRangeDto;
import moviefyPackge.moviefy.repositories.SeatAllocationRepository;
import moviefyPackge.moviefy.services.AdminService;
import moviefyPackge.moviefy.services.BookingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final BookingService bookingService;
    private final SeatAllocationRepository seatAllocationRepository;
    /**
     * Creates a new AdminServiceImpl instance with the required services.
     * @param bookingService booking service used for statistics
     * @param seatAllocationRepository repository for querying seat allocations
     */
    public AdminServiceImpl(BookingService bookingService, SeatAllocationRepository seatAllocationRepository) {
        this.bookingService = bookingService;
        this.seatAllocationRepository = seatAllocationRepository;
    }

    /**
     * Returns monthly booking statistics between the given start and end dates.
     * @param start start date of the range
     * @param end end date of the range
     * @return list of BookingByMonthDto representing monthly bookings
     * @throws Exception in case of data retrieval failure
     */
    @Override
    public List<BookingByMonthDto> getBookingsPerMonth(LocalDateTime start, LocalDateTime end) throws Exception {
        return bookingService.getMonthBookingsStats(start,end);
    }

    /**
     * Returns income statistics grouped by range between the given start and end dates.
     * @param start start date of the range
     * @param end end date of the range
     * @return list of IncomeRangeDto representing income per range
     * @throws Exception in case of data retrieval failure
     */
    @Override
    public List<IncomeRangeDto> getIncomeByRange(LocalDateTime start, LocalDateTime end) throws Exception {
        return bookingService.getIncomeRangeStats(start,end);
    }


    /**
     * Returns a chart-friendly list of the top 5 movies with the highest income in the given range.
     * @param start start date of the range
     * @param end end date of the range
     * @return list of PopularMovieChartDto representing top income movies
     */
    @Override
    public List<?> getHighestIncomeMovieRange(LocalDateTime start, LocalDateTime end) {
        Object[] dataObjects = getDataForPopularMovie(start, end);
        @SuppressWarnings("unchecked")
        List<PopularMovieItemDto> top = (List<PopularMovieItemDto>) dataObjects[1];

        return top.stream()
                .map(m -> new PopularMovieChartDto(
                        m.getMovieTitle(),
                        m.getTotalIncome() != null ? m.getTotalIncome() : 0
                ))
                .toList();
    }


    /**
     * Helper method that builds a date range and fetches top 5 movies by income and seat occupancy.
     * @param from start date of the range
     * @param to end date of the range
     * @return an Object array with DateRangeDto and list of top movies
     */
    private Object[] getDataForPopularMovie(LocalDateTime from,LocalDateTime to){
        Object[] objects = new Object[2];
        objects[0] = DateRangeDto.builder()
                .from(from)
                .to(to)
                .build();
        Pageable top5 = PageRequest.of(0, 5);
        objects[1] = seatAllocationRepository.findTopMoviesByOccupiedSeatsAndBookings(from,to,top5);
        return objects;
    }
}
