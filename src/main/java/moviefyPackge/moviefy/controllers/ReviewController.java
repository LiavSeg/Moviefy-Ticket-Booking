package moviefyPackge.moviefy.controllers;

import moviefyPackge.moviefy.domain.Entities.ReviewEntity;
import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewRequestDto;
import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewResponseDto;
import moviefyPackge.moviefy.errors.ErrorHandler;
import moviefyPackge.moviefy.mappers.impl.ReviewRequestMapperImpl;
import moviefyPackge.moviefy.mappers.impl.ReviewResponseMapperImpl;
import moviefyPackge.moviefy.services.impl.BookingServiceImpl;
import moviefyPackge.moviefy.services.impl.ReviewServiceImpl;
import moviefyPackge.moviefy.utils.LoggerWrapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.CannotProceedException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Exposes review endpoints for creating reviews and retrieving
 * all reviews associated with a specific movie.
 * Accepts and returns DTOs, using mappers to convert between
 * ReviewEntity and its DTO representations.
 */
@RequestMapping("/reviews")
@RestController
public class ReviewController {
    private final ReviewServiceImpl reviewService;
    private final ErrorHandler errorHandler;
    private final BookingServiceImpl bookingService;
    private final ReviewResponseMapperImpl reviewResponseMapper;
    private final LoggerWrapper<ReviewController> logger ;


    /**
     * Initializes the controller with required services and mappers.
     * @param reviewService service for managing review persistence and logic
     * @param reviewResponseMapper mapper from entity to response DTO
     */
    public ReviewController(ReviewServiceImpl reviewService, BookingServiceImpl bookingService,
                            ReviewResponseMapperImpl reviewResponseMapper) {
        this.reviewService = reviewService;
        this.bookingService = bookingService;
        this.reviewResponseMapper = reviewResponseMapper;
        this.errorHandler = new ErrorHandler(ReviewController.class);
        logger = new LoggerWrapper<>(ReviewController.class,"Reviews");
        logger.startLog();

    }

    /**
     * Creates a new review for a specific movie.
     * @param movieId the ID of the movie being reviewed
     * @param reviewDto the review content and user info
     * @return HTTP 201 with the created review on success,
     *         HTTP 404 if the movie does not exist,
     *         or HTTP 409 for validation or persistence errors
     */
    @PostMapping("/movie/{movieId}")
    public ResponseEntity<?> createReview(@PathVariable Integer movieId, @RequestBody @Valid ReviewRequestDto reviewDto){
        try{
            if (!bookingService.isEligibleToReview(reviewDto.getUserId(), movieId))
                return new ResponseEntity<>("User can not post a review ",HttpStatus.FORBIDDEN);
            logger.LogInfo("User "+reviewDto.getUserId()+" posts a new review");
            ReviewEntity newReview = reviewService.addReview(reviewDto,movieId);
            logger.costume("Review was added");
            ReviewResponseDto revDto = reviewResponseMapper.mapTo(newReview);
            logger.fromEntity();
            logger.success();
            return new ResponseEntity<>(revDto,HttpStatus.CREATED);
        } catch (NoSuchElementException e){
            return errorHandler.notFound(movieId,e,e.toString());
        }
        catch (CannotProceedException e){
            return errorHandler.conflict("",e,e.toString());
        }
        catch (IllegalStateException e){
            return errorHandler.notFound("",e,e.toString());
        }
        catch (Exception e){
            return errorHandler.badRequest("",e,e.toString());
        }
    }

    /**
     * Retrieves all reviews associated with a given movie.
     * @param movieId the ID of the movie
     * @return HTTP 200 with a list of ReviewResponseDto,
     *         or HTTP 409 if retrieval fails
     */
    @GetMapping("/by-movie/{movieId}")
    public ResponseEntity<?> getReviewsByMovie(@PathVariable Integer movieId){
        try{
            logger.LogInfo("Fetching reviews for movie "+movieId);
            logger.fromEntity();
            List<ReviewResponseDto> byMovie = reviewService.findByMovieId(movieId);
            logger.success();
            return new ResponseEntity<>(byMovie,HttpStatus.OK);
        }
        catch (Exception e){
            return errorHandler.conflict("",e,e.toString());
        }
    }
}
