package moviefyPackge.moviefy.services;

import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.dto.MoviteDtos.MovieDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface MovieService {
    MovieEntity createMovie(MovieEntity movie);
    MovieEntity getById(Integer id);
    List<MovieEntity> findAll();
    boolean deleteMovieById(Integer id);
    List<MovieEntity> searchByTerm(String term);
    Optional<MovieEntity> findById(Integer id);
    boolean existsInDb(MovieEntity movieEntity);
    List<MovieEntity> getSortedList(String tab,String genre);
    MovieDto patchMovie(String field, Integer movieId, String value);
    List<Integer> getIdsByTitles(List<String> titles);

}