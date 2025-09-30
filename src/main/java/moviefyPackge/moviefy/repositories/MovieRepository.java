package moviefyPackge.moviefy.repositories;

import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing movie-related data.
 * Supports standard CRUD operations and dynamic query methods
 * for searching by title, genre, release date, and description.
 */
@Repository
public interface MovieRepository extends CrudRepository<MovieEntity, Integer> {
    Iterable<MovieEntity> findByTitle(String title);
    List<MovieEntity> findAll();
    List<MovieEntity> findByReleaseDateIsAfterAndGenre(LocalDate releaseDate, String genre);
    List<MovieEntity> findByReleaseDateIsBeforeAndGenre(LocalDate releaseDate, String genre);
    List<MovieEntity> findByReleaseDateIsBefore(LocalDate releaseDate);
    List<MovieEntity> findByReleaseDateIsAfter(LocalDate releaseDateAfter);
    List<MovieEntity> findByTitleContainingIgnoreCase(String title);
    List<MovieEntity> findByGenreContainingIgnoreCase(String genre);
    List<MovieEntity> findByDescriptionContainingIgnoreCase(String description);
    Optional<MovieEntity> getByTitle(String title);
}
