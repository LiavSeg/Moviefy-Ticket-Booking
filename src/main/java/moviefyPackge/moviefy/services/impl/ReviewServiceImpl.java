package moviefyPackge.moviefy.services.impl;

import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.Entities.ReviewEntity;
import moviefyPackge.moviefy.domain.Entities.UserEntity;

import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewRequestDto;
import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewResponseDto;
import moviefyPackge.moviefy.mappers.impl.ReviewRequestMapperImpl;
import moviefyPackge.moviefy.mappers.impl.ReviewResponseMapperImpl;
import moviefyPackge.moviefy.repositories.BookingRepository;
import moviefyPackge.moviefy.repositories.MovieRepository;
import moviefyPackge.moviefy.repositories.ReviewRepository;
import moviefyPackge.moviefy.repositories.UserRepository;
import moviefyPackge.moviefy.services.BookingService;
import moviefyPackge.moviefy.services.ReviewService;
import org.springframework.stereotype.Service;

import javax.naming.CannotProceedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewResponseMapperImpl reviewMapper;

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    public ReviewServiceImpl(ReviewResponseMapperImpl reviewMapper, ReviewRepository reviewRepository, MovieRepository movieRepository, UserRepository userRepository, ReviewRequestMapperImpl reviewRequestMapperImpl, BookingService bookingService, BookingRepository bookingRepository){
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;

    }
    @Override
    public List<ReviewResponseDto> findByMovieId(Integer movieId) {

        List<ReviewEntity> reviews = reviewRepository.findAllByMovieId(movieId);

        return  reviewRepository.findAllByMovieId(movieId).stream().map(reviewMapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDto> findByUser(UserEntity user) {
        return reviewRepository.findAllByUser(user).stream().map(reviewMapper::mapTo).collect(Collectors.toList());
    }



    @Override
    public ReviewEntity addReview(ReviewRequestDto review,Integer movieId) throws IllegalArgumentException, CannotProceedException {

        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new IllegalArgumentException("Movie Not Found"));
        UserEntity user = userRepository.findByUserId(review.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
        if (reviewRepository.existsByUser_userIdAndMovie_Id(review.getUserId(),movieId))
            throw new CannotProceedException("User already posted A review for movie "+movieId);
        var currReview = ReviewEntity.builder()
                        .user(user)
                        .movie(movie)
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .build();
                return reviewRepository.save(currReview);
    }
}
