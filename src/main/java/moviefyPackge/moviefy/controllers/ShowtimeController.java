package moviefyPackge.moviefy.controllers;

import jakarta.validation.Valid;
import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.dto.ShowtimeDto.ShowtimeAdminDto;
import moviefyPackge.moviefy.domain.dto.ShowtimeDto.ShowtimeDto;
import moviefyPackge.moviefy.domain.dto.ShowtimeDto.ShowtimeDtoEdit;
import moviefyPackge.moviefy.errors.ErrorHandler;
import moviefyPackge.moviefy.mappers.Mapper;
import moviefyPackge.moviefy.services.MovieService;
import moviefyPackge.moviefy.services.ShowtimeService;
import moviefyPackge.moviefy.services.impl.SeatAllocationServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Exposes endpoints for managing showtimes, including:
 * creation, retrieval, update, and deletion.
 * Each showtime is associated with a movie and includes
 * automatic seat allocation upon creation.
 */
@RestController
public class ShowtimeController {

    private final MovieService movieService;
    private final ShowtimeService showtimeService;
    private final Mapper<ShowtimeEntity, ShowtimeDto> showtimeMapper;
    private final Mapper<ShowtimeEntity, ShowtimeAdminDto> showtimeAdminDtoMapper;
    private final SeatAllocationServiceImpl seatAllocationService;
    private static final Logger logger = LoggerFactory.getLogger(ShowtimeController.class);
    private final ErrorHandler errorHandler;


    /**
     * Initializes the controller with required services and mappers.
     * @param showtimeService service for managing showtime logic
     * @param showtimeMapper mapper for public-facing showtime DTOs
     * @param movieService service for movie lookup
     * @param showtimeAdminDtoMapper mapper for admin-facing showtime DTOs
     * @param seatAllocationService seat creation and reservation logic
     */
    public ShowtimeController(ShowtimeService showtimeService, Mapper<ShowtimeEntity, ShowtimeDto> showtimeMapper, MovieService movieService, Mapper<ShowtimeEntity, ShowtimeAdminDto> showtimeAdminDtoMapper, SeatAllocationServiceImpl seatAllocationService) {
        this.showtimeAdminDtoMapper = showtimeAdminDtoMapper;
        this.seatAllocationService = seatAllocationService;
        this.errorHandler = new ErrorHandler(ShowtimeController.class);
        this.showtimeService = showtimeService;
        this.showtimeMapper = showtimeMapper;
        this.movieService = movieService;
        logger.info("Showtime Endpoint is set and ready to operate.");

    }


    /**
     * Creates a new showtime for a given movie and initializes its seats.
     * @param showtimeDto the showtime details including the movie ID
     * @return HTTP 200 with the created showtime DTO on success,
     *         or an error response if movie not found or creation fails
     */
    @PostMapping(path = "/showtimes")
    public ResponseEntity<?> createShowtime(@RequestBody ShowtimeDto showtimeDto) {
        try {
            Integer id = showtimeDto.getMovieId();
            Optional<MovieEntity> movieEntity = movieService.findById(id);

            if (movieEntity.isEmpty()) {
                Exception e = new Exception(String.format("Failed to locate a movie with ID %d for the requested showtime. " +
                        "\nPlease ensure that the movie ID exists and try again.", id));
                return errorHandler.notFound("",e,"showtimes");
            }

            ShowtimeEntity showtimeEntity = showtimeMapper.mapFrom(showtimeDto);
            showtimeEntity.setMovie(movieEntity.get());
            ShowtimeEntity savedShowtimeEntity = showtimeService.createShowtime(showtimeEntity);
            seatAllocationService.createDefaultSeats(savedShowtimeEntity.getId());

            return new ResponseEntity<>(showtimeMapper.mapTo(savedShowtimeEntity),HttpStatus.OK);
        }
        catch (IllegalStateException e){
            return errorHandler.conflict("",e,"showtimes");
        }
        catch (Exception e) {
            return errorHandler.badRequest("",e,"showtimes");
        }
    }


    /**
     * Retrieves a single showtime by its ID.
     * @param showtimeId identifier of the showtime
     * @return HTTP 200 with the showtime DTO on success,
     *         or HTTP 404 if not found
     */
    @GetMapping(path = "/showtimes/{showtimeId}")
    public ResponseEntity<?> getShowtimeById(@PathVariable Integer showtimeId){
        try {
            ShowtimeEntity foundShowtime = showtimeService.findById(showtimeId);
            logger.info("Showtime with ID {} retrieved successfully", showtimeId);
            return new ResponseEntity<>(showtimeMapper.mapTo(foundShowtime),HttpStatus.OK);
        }

        catch (NoSuchElementException | EntityNotFoundException e) {
                return errorHandler.notFound(showtimeId,e,"showtimes");
            }
        catch (Exception e) {
            return errorHandler.badRequest("",e,"showtimes");
        }
    }

    /**
     * Returns all showtimes associated with a specific movie.
     * @param movieId ID of the movie
     * @return HTTP 200 with a list of showtime DTOs,
     *         or an error response if failed to fetch
     */
    @GetMapping(path = "/showtimes/by-movie/{movieId}")
    public ResponseEntity<?> getShowtimeByMovieId(@PathVariable Integer movieId){
        try {
            List<ShowtimeEntity> foundShowtime = showtimeService.findByMovieId(movieId);
            List<ShowtimeDto> showtimeDtos = new ArrayList<>();
            logger.info("{} Showtimes for Movie ID {} were found and retrieved successfully", foundShowtime.size(),movieId);
            for (ShowtimeEntity shows: foundShowtime)
                showtimeDtos.add(showtimeMapper.mapTo(shows));
            return new ResponseEntity<>(showtimeDtos,HttpStatus.OK);
        }

        catch (NoSuchElementException | EntityNotFoundException e) {
            return errorHandler.notFound("",e,"showtimes/by-movie/{"+movieId+"}");
        }
        catch (Exception e) {
            return errorHandler.badRequest("",e,"showtimes/by-movie/{"+movieId+"}");
        }
    }


    /**
     * Returns all showtimes in the system for administrative access.
     * @return HTTP 200 with a list of showtime admin DTOs,
     *         or error response on failure
     */
    @GetMapping(path = "/showtimes/all")
    public ResponseEntity<?> getAllShowtimes(){
        try {
            List<ShowtimeEntity> foundShowtime = showtimeService.getAll();
            List<ShowtimeAdminDto> showtimeAdminDtos = new ArrayList<>();
            logger.info("{} Showtimes found in total - exporting to admin",foundShowtime.size());
            for (ShowtimeEntity shows: foundShowtime)
                showtimeAdminDtos.add(showtimeAdminDtoMapper.mapTo(shows));
            return new ResponseEntity<>(showtimeAdminDtos,HttpStatus.OK);
        }


        catch (Exception e) {
            logger.info("err msg....");

            return errorHandler.badRequest("",e,"showtimes/all");
        }
    }

    /**
     * Updates an existing showtime by ID using provided fields.
     * @param edit new showtime data
     * @param showtimeId ID of the showtime to update
     * @return HTTP 200 on success, or error response on failure
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path ="showtimes/update/{showtimeId}")
    public ResponseEntity<?>  updateShowtime(@PathVariable Integer showtimeId, @RequestBody @Valid ShowtimeDtoEdit edit){
        try {

            ShowtimeEntity updated = showtimeService.updatedShowtime(showtimeId,edit);

            return new ResponseEntity<>(showtimeAdminDtoMapper.mapTo(updated),HttpStatus.OK);
        }
        catch (NoSuchElementException | EntityNotFoundException e) {
            return errorHandler.notFound(showtimeId,e,"showtimes/update");
        }
        catch (Exception e) {
            return errorHandler.badRequest("",e,"showtimes/update");
        }
    }
    /**
     * Deletes a showtime by its ID.
     * @param showtimeId identifier of the showtime to delete
     * @return HTTP 200 on successful deletion,
     *         or error response if not found or deletion fails
     */
    @DeleteMapping(path ="showtimes/{showtimeId}")
    public ResponseEntity<?>  deleteShowtime(@PathVariable Integer showtimeId){
        try {
            ResponseEntity<?> showtimeToDelete = getShowtimeById(showtimeId);
            if (showtimeToDelete.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new EntityNotFoundException("Could not find a showtime with id :"+showtimeId);

            showtimeService.deleteShowtime(showtimeId);
            logger.info("Successfully deleted showtime with ID {}", showtimeId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NoSuchElementException | EntityNotFoundException e) {
            return errorHandler.notFound(showtimeId,e,"showtimes");
        }
        catch (Exception e) {
            return errorHandler.badRequest("",e,"showtimes");
        }

    }
}
