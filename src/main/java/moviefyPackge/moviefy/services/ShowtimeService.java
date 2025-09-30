package moviefyPackge.moviefy.services;

import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.dto.ShowtimeDto.ShowtimeDtoEdit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ShowtimeService {
    ShowtimeEntity createShowtime(ShowtimeEntity showtimeEntity);
    ShowtimeEntity findById(Integer id);
    void deleteShowtime(Integer id);
    List<ShowtimeEntity> findByMovieId(Integer movieId);
    List<ShowtimeEntity> getAll();
     ShowtimeEntity updatedShowtime(Integer toUpdateId, ShowtimeDtoEdit given);

    }
