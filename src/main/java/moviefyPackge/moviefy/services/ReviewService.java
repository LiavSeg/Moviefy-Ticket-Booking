package moviefyPackge.moviefy.services;

import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.Entities.ReviewEntity;
import moviefyPackge.moviefy.domain.Entities.UserEntity;
import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewRequestDto;
import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewResponseDto;
import org.springframework.stereotype.Component;

import javax.naming.CannotProceedException;
import java.util.List;
import java.util.Optional;

@Component
public interface ReviewService {


    List<ReviewResponseDto> findByMovieId(Integer movie); // Highest/Lowest/Rated movies
    List<ReviewResponseDto> findByUser(UserEntity userId); // Will be used in user's profile
    ReviewEntity addReview(ReviewRequestDto review,Integer movieId) throws IllegalArgumentException, CannotProceedException;
}
