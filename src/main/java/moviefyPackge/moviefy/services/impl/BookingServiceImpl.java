package moviefyPackge.moviefy.services.impl;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.BookingByMonthDto;
import moviefyPackge.moviefy.domain.dto.StatsDtos.IncomeRangeDto;
import moviefyPackge.moviefy.mappers.BookingMapper;
import moviefyPackge.moviefy.repositories.BookingRepository;
import moviefyPackge.moviefy.services.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService  {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingEntity createBooking(BookingEntity bookingEntity) {
        try {
            List<String> takenSeats = getSeatsForBookingByMovieId(bookingEntity.getShowtime().getId());
            for (String st : bookingEntity.getSeatList())
                for (String taken : takenSeats)
                    if(st.equals(taken)) {
                        throw new IllegalStateException(String.format(
                                "The requested seat %s conflicts with an existing booking in the showtime. " +
                                        "Please choose a different seat or another showtime to avoid overlap.",st));
                    }
            return bookingRepository.save(bookingEntity);
        }
        catch(NoSuchElementException e) {
            throw new NoSuchElementException( String.format("The showtime with ID %d does not exist in the database." +
                    " Please verify the showtime ID and try booking again.",bookingEntity.getShowtime().getId()));

        }
    }
    @Override
    public BookingEntity findBooking(UUID bookingEntity) {
        return bookingRepository.findByBookingId(bookingEntity);
    }


    @Override
    public List<BookingDto> getAllUserBooking(UUID userId) {

        return bookingRepository.findByUser_UserId(userId).stream().map(bookingMapper::fromBookingEntity).toList();

    }

    @Override
    public Boolean isEligibleToReview(UUID userId,Integer movieId) {
            for (BookingEntity booking:bookingRepository.findByUser_UserId(userId))
                if (booking.getShowtime().getMovie().getId().equals(movieId))
                    return true;
        return false;
    }

    @Override
    public List<String> getSeatsForBookingByMovieId(Integer movieId) {
        Iterable<BookingEntity> bookingEntities = bookingRepository.findAllBookingsByShowtimeId(movieId);
        List<String> seats = new ArrayList<>();
        for (BookingEntity booking:bookingEntities)
            seats.addAll(booking.getSeatList());
        return seats;
    }

    @Override
    public  List<BookingByMonthDto> getMonthBookingsStats(LocalDateTime start,LocalDateTime end) throws Exception {
        if (start.isAfter(end))
            throw new Exception("Incorrect range, Start is after end");
        if (end.isBefore(start))
            throw new Exception("Incorrect range, end is after start");
        return bookingRepository.findAllByBookingTimeBetween(start,end);
    }

    @Override
    public List<IncomeRangeDto> getIncomeRangeStats(LocalDateTime start, LocalDateTime end) throws Exception {
        return bookingRepository.findIncomeInRange(start,end);
    }



}
