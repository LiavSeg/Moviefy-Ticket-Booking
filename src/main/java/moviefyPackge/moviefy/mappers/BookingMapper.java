package moviefyPackge.moviefy.mappers;

 import moviefyPackge.moviefy.domain.Entities.BookingEntity;
 import moviefyPackge.moviefy.domain.Entities.MovieEntity;
 import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
 import moviefyPackge.moviefy.domain.Entities.UserEntity;
 import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingDto;
 import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingRequestDto;
 import moviefyPackge.moviefy.domain.dto.ReportsDto.BookingReports.BookingItemReport;
 import moviefyPackge.moviefy.repositories.UserRepository;
 import moviefyPackge.moviefy.services.impl.MovieServiceImpl;
 import moviefyPackge.moviefy.services.impl.ShowtimeServiceImpl;
 import moviefyPackge.moviefy.utils.DateAndTimeFormatter;
 import org.jetbrains.annotations.NotNull;
 import org.modelmapper.ModelMapper;
 import org.springframework.stereotype.Component;
 import java.time.LocalDateTime;

@Component
public class BookingMapper {

    private final ShowtimeServiceImpl showtimeService;
    private final UserRepository userRepository;
    private final DateAndTimeFormatter formatter;
    public BookingMapper(
            ModelMapper modelMapper,
            MovieServiceImpl movieService,
            ShowtimeServiceImpl showtimeService,
            UserRepository userRepository) {
        this.showtimeService = showtimeService;
        this.userRepository = userRepository;
        this.formatter = new DateAndTimeFormatter();
    }

    public BookingEntity fromBookingRequestDto(BookingRequestDto bookingRequestDto) {

        return BookingEntity.builder()
                .user(userRepository.findByUserId(bookingRequestDto.getUserId()).orElseThrow())
                .showtime(showtimeService.findById(bookingRequestDto.getShowtimeId()))
                .seatList(bookingRequestDto.getSeats())
                .totalPrice(bookingRequestDto.getTotalPrice())
                .bookingTime(LocalDateTime.now())
                .build();
    }

    public BookingDto fromBookingEntity(@NotNull BookingEntity bookingEntity) {
       ShowtimeEntity showtime = bookingEntity.getShowtime();
        MovieEntity movie = showtime.getMovie();
       UserEntity user = bookingEntity.getUser();
       String bookingDateTime = formatter.parseLocalDateTimeRange(showtime.getStartTime(),showtime.getEndTime());
       String orderTime = formatter.parseLocalDateTimeRange(bookingEntity.getBookingTime());
        return BookingDto.builder()
                .userName(user.getUserName())
                .bookingId(bookingEntity.getBookingId().toString())
                .showtimeId(bookingEntity.getShowtime().getId())
                .movieTitle(movie.getTitle())
                .bookingDateTime(bookingDateTime)
                .seatList(bookingEntity.getSeatList())
                .totalPrice(bookingEntity.getTotalPrice())
                .timeOfOrder(orderTime)
                .build();
    }

    public BookingItemReport fromBookingEntityToItem(@NotNull BookingEntity bookingEntity){
           return BookingItemReport.builder()
                   .bookingId(bookingEntity.getBookingId().toString())
                   .userId(bookingEntity.getUser().getUserId().toString())
                   .username(bookingEntity.getUser().getUserName())
                   .movieTitle(bookingEntity.getShowtime().getMovie().getTitle())
                   .showtime(bookingEntity.getShowtime().getStartTime())
                   .theater(bookingEntity.getShowtime().getTheater())
                   .seats(bookingEntity.getSeatList())
                   .price(bookingEntity.getTotalPrice())
                   .createdAt(LocalDateTime.now())
                   .build();
    }



}
