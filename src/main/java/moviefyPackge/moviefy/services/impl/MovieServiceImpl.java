package moviefyPackge.moviefy.services.impl;

import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.dto.MoviteDtos.MovieDto;
import moviefyPackge.moviefy.mappers.impl.MovieMapperImpl;
import moviefyPackge.moviefy.repositories.MovieRepository;
import moviefyPackge.moviefy.repositories.ShowtimeRepository;
import moviefyPackge.moviefy.services.MovieService;
import moviefyPackge.moviefy.services.ShowtimeService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.hibernate.PersistentObjectException;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MovieServiceImpl implements MovieService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeService showtimeService;
    private final Validator validator;
    private final MovieMapperImpl movieMapperImpl;

    public MovieServiceImpl(MovieRepository movieRepository, Validator validator, ShowtimeRepository showtimeRepository, ShowtimeService showtimeService, MovieMapperImpl movieMapperImpl) {
        this.movieRepository = movieRepository;
        this.validator = validator;
        this.showtimeRepository =  showtimeRepository;
        this.showtimeService =  showtimeService;
        this.movieMapperImpl = movieMapperImpl;
    }
    @Override
    public MovieEntity createMovie(MovieEntity movieEntity){
        validateMovie(movieEntity);
        return movieRepository.save(movieEntity);
    }

    @Override
    public MovieEntity getById(Integer id) {
        return movieRepository.findById(id).orElseThrow(()-> new RuntimeException("There is not movie associated with id" + id));
    }

    @Override
    public boolean existsInDb(MovieEntity movieEntity)  {
        Iterable<MovieEntity> movies = movieRepository.findByTitle(movieEntity.getTitle());
        for (MovieEntity movie:movies ){
            if (movie.getTitle().equals(movieEntity.getTitle()))
               return true;
        }
        return false;
    }

    @Override
    public List<MovieEntity> findAll(){
        return StreamSupport.stream(movieRepository
                        .findAll().spliterator(), false
                ).collect(Collectors.toList());

    }

    @Override
    public boolean deleteMovieById(Integer id) {
        movieRepository.deleteById(id);
        if (movieRepository.findById(id).isPresent())
            throw new PersistentObjectException("Could not del");
        return true;
    }

    @Override
    public List<MovieEntity> searchByTerm(String term) {
        Set<MovieEntity> result = new LinkedHashSet<>();
        result.addAll(movieRepository.findByTitleContainingIgnoreCase(term));
        result.addAll(movieRepository.findByGenreContainingIgnoreCase(term));
        result.addAll(movieRepository.findByDescriptionContainingIgnoreCase(term));
        return new ArrayList<>(result);
    }

    @Override
    public Optional<MovieEntity> findById(Integer id) {
        return movieRepository.findById(id);
    }

    public void validateMovie(MovieEntity movieEntity) {
        Set<ConstraintViolation<MovieEntity>> violations = validator.validate(movieEntity);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Validation failed: " + errorMessages);
        }
    }
@Override
public List<MovieEntity> getSortedList(String tab, String genre){
        LocalDate today = LocalDate.now();

    switch (tab) {
        case "MOVIES IN THEATERS" -> {
            if (genre.equals("All")) {
                return movieRepository.findByReleaseDateIsBefore(today);
            } else
                return movieRepository.findByReleaseDateIsBeforeAndGenre(today, genre);
        }
        case "COMING SOON" -> {
            if (genre.equals("All")) {
                return movieRepository.findByReleaseDateIsAfter(today);
            } else
                return movieRepository.findByReleaseDateIsAfterAndGenre(today, genre);
        }
        case "ALL TIMES FANS FAVORITES" -> {
            return movieRepository.findByTitleContainingIgnoreCase("potter");
        }
    }
    return null;
    }

    @Transactional
    @Override
    public MovieDto patchMovie(String field, Integer movieId, String value) {
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));

        switch (field) {
            case "title" -> movie.setTitle(value);
            case "language" -> movie.setLanguage(value);
            case "trailerUrl" -> movie.setTrailerUrl(value);
            case "releaseDate" -> {
                LocalDate date = LocalDate.parse(value);
                movie.setReleaseDate(date);
            }
            default -> throw new IllegalArgumentException("Unsupported field: " + field);
        }

        MovieEntity saved = movieRepository.save(movie);
        return movieMapperImpl.mapTo(saved);
    }

    @Override
    public List<Integer> getIdsByTitles(List<String> titles) {
        List<Integer> ids = new ArrayList<>();
        for (String title : titles) {
            movieRepository.getByTitle(title)
                    .map(MovieEntity::getId)
                    .ifPresent(ids::add);
        }
        return ids;
    }

}
