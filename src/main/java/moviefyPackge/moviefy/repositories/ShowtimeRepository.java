package moviefyPackge.moviefy.repositories;

import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Repository interface for managing showtime entities in the system.
 * Provides standard CRUD operations along with query methods for
 * retrieving showtimes by theater, movie, or specific time constraints.
 */
@Repository
public interface ShowtimeRepository extends CrudRepository<ShowtimeEntity,Integer>{
    @NotNull List<ShowtimeEntity> findAll();
   // List<ShowtimeEntity> findAllByStartTimeGreaterThanEqual(LocalDateTime now);
    @NotNull List<ShowtimeEntity> findByMovieIdAndStartTimeGreaterThan(Integer movieId, LocalDateTime now);

   // @NotNull Iterable<ShowtimeEntity> findByTheater(String theater);
    @NotNull List<ShowtimeEntity> findByMovieId(Integer movieId);
    @NotNull List<ShowtimeEntity> findByTheaterAndStartTimeLessThanAndEndTimeGreaterThan(
        String theater,
        LocalDateTime startTime,
        LocalDateTime endTime
    );
   //List<ShowtimeEntity> findByMovie(MovieEntity movieEntity);
}
