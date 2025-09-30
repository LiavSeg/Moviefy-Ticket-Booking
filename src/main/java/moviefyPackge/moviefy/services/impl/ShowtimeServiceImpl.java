package moviefyPackge.moviefy.services.impl;
import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.dto.ShowtimeDto.ShowtimeAdminDto;
import moviefyPackge.moviefy.domain.dto.ShowtimeDto.ShowtimeDtoEdit;
import moviefyPackge.moviefy.repositories.BookingRepository;
import moviefyPackge.moviefy.repositories.ShowtimeRepository;
import moviefyPackge.moviefy.services.MovieService;
import moviefyPackge.moviefy.services.ShowtimeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final BookingRepository bookingRepository;
    private final MovieService movieService;

    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository, BookingRepository bookingRepository, @Lazy MovieService movieService) {
        this.showtimeRepository = showtimeRepository;
        this.bookingRepository = bookingRepository;
        this.movieService = movieService;
    }

    @Override
    @Transactional
    public ShowtimeEntity createShowtime(ShowtimeEntity showtimeEntity) {
        String overlappingShowtimeIds = isOverlap(
                showtimeEntity.getStartTime(),
                showtimeEntity.getEndTime(),
                showtimeEntity.getTheater()
        );
        if (overlappingShowtimeIds.isEmpty())
            return showtimeRepository.save(showtimeEntity);
        else
            throw new IllegalStateException("Can't create this showtime due to overlapping screening times with the following " +
                "showtimes(Ids):\n\t" + overlappingShowtimeIds);
    }


   @Override
    public ShowtimeEntity findById(Integer id) throws EntityNotFoundException{
       return showtimeRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("Showtime not found with ID: " + id));
    }
    @Override
    public List<ShowtimeEntity> findByMovieId(Integer movieId){
        MovieEntity movie = movieService.getById(movieId);
        List<ShowtimeEntity> showtimes = showtimeRepository.findByMovieIdAndStartTimeGreaterThan(movieId,LocalDateTime.now());
        return showtimes;
    }

    @Override
    public List<ShowtimeEntity> getAll() {
        return showtimeRepository.findAll();
    }


    @Override
    @Transactional
    public void deleteShowtime(Integer id) {
        bookingRepository.deleteAllByShowtimeId(id);
        showtimeRepository.deleteById(id);
    }

    @Override
    public ShowtimeEntity updatedShowtime(Integer toUpdateId, ShowtimeDtoEdit given){
        ShowtimeEntity toUpdate = showtimeRepository.findById(toUpdateId).orElseThrow(EntityNotFoundException::new);
        toUpdate.setStartTime(given.getStartTime());
        toUpdate.setEndTime(given.getEndTime());
        toUpdate.setTheater(given.getTheater());
        toUpdate.setPrice(given.getPrice());
        return showtimeRepository.save(toUpdate);

    }
    private String isOverlap(LocalDateTime startTime,LocalDateTime endTime,String theater) {
        List<ShowtimeEntity> overlapped = showtimeRepository
                .findByTheaterAndStartTimeLessThanAndEndTimeGreaterThan(theater, endTime, startTime);
        if (!overlapped.isEmpty()){
            return overlapped.stream()
                    .map(showtimeEntity -> showtimeEntity.getId().toString())
                    .collect(Collectors.joining(","));
        }
        return "";
    }
}
