package moviefyPackge.moviefy.controllers;
import moviefyPackge.moviefy.domain.dto.MoviteDtos.MovieDto;
import moviefyPackge.moviefy.errors.ErrorHandler;
import moviefyPackge.moviefy.mappers.Mapper;
import moviefyPackge.moviefy.services.MovieService;
import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.utils.LoggerWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
/**
 * Exposes movie management endpoints for creating, retrieving,
 * updating, deleting, and searching movies.
 * Uses a mapper to convert between MovieEntity and MovieDto and
 * a centralized ErrorHandler for consistent error responses.
 */
@RestController
public class MovieController {

    private final MovieService movieService;
    private final Mapper<MovieEntity, MovieDto> movieMapper;
    private final ErrorHandler errorHandler;
    private final String currPath = "/movies";
    private final LoggerWrapper<MovieController> logger ;

    /**
     * Initializes the controller with required collaborators.
     * @param movieService business logic for movie operations
     * @param movieMapper mapper for MovieEntity and MovieDto
     */
    public MovieController(MovieService movieService, Mapper<MovieEntity,MovieDto> movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.errorHandler = new ErrorHandler(MovieController.class);
        this.logger = new LoggerWrapper<>(MovieController.class,"Movies");
        logger.startLog();

    }

    /**
     * Creates a new movie.
     * @param movie movie details as a DTO
     * @return HTTP 200 with the created MovieDto on success,
     *         or an error response on failure
     */
    @PostMapping(path = "/movies")
    public ResponseEntity<?> createMovie(@RequestBody MovieDto movie) {
        try{
            logger.LogInfo("Creating a new Movie entity");
            MovieEntity movieEntity = movieMapper.mapFrom(movie);
            logger.fromEntity();
            MovieEntity savedMovieEntity = movieService.createMovie(movieEntity);
            logger.costume("Created a new Movie entity - ID: "+savedMovieEntity.getId());
            logger.success();
            logger.fromEntity();
            return new ResponseEntity<>(movieMapper.mapTo(savedMovieEntity),HttpStatus.OK);
        }
        catch(Exception e){
            return errorHandler.badRequest("",e,currPath);
        }
    }

    /**
     * Returns all movies. Intended for administrative use.
     * @return HTTP 200 with a list of MovieDto on success,
     *         or an error response on failure
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/movies/all")
    public ResponseEntity<?> getAllMovies() {
        try{
            logger.LogInfo("Fetching all movies on DB - initiated by admin only");
            List<MovieEntity> movies = movieService.findAll();
            logger.costume("All movies fetched - mapping to a dto list");
            logger.fromEntity();
            List<MovieDto> moviesDto = movies.stream().map(movieMapper::mapTo).collect(Collectors.toList());
            logger.success();
        return new ResponseEntity<>(moviesDto,HttpStatus.OK);
    }
        catch(Exception e){
            return errorHandler.badRequest("",e,currPath);
        }
    }

    /**
     * Returns a movie by its identifier.
     * @param id movie identifier
     * @return HTTP 200 with the MovieEntity on success,
     *         HTTP 404 if the movie does not exist,
     *         or an error response on failure
     */
    @GetMapping(path = "/movies/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try{
            logger.LogInfo("GET request for movie "+id);

            MovieEntity movie = movieService.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Movie id "+id+" does not exist in DB"));
            logger.LogInfo((movie.getTrailerUrl()));

            return new ResponseEntity<>(movieMapper.mapTo(movie),HttpStatus.OK);
        }
        catch (NoSuchElementException e){
            return errorHandler.notFound(id,e,String.format("/movies/%d",id));
        }
        catch(Exception e){
            return errorHandler.badRequest("",e,currPath);
        }
    }

    /**
     * Returns movies filtered and sorted by optional query parameters.
     * @param tab optional tab key indicating a category or sort preset
     * @param genre optional genre filter
     * @return list of MovieDto matching the filter criteria
     */
    @GetMapping(path = "/movies")
    public List<MovieDto> getMovies(
            @RequestParam(required = false) String tab,
            @RequestParam(required = false) String genre
    ) {
        List<MovieEntity> sortedMoviesList = movieService.getSortedList(tab,genre);
        List<MovieDto> moviesDto = new ArrayList<>();
        for (MovieEntity movie: sortedMoviesList)
            moviesDto.add(movieMapper.mapTo(movie));

      return moviesDto;
    }

    /**
     * Updates a movie identified by its title.
     *
     * @param title movie title used to locate the record
     * @param movieDto updated movie details
     * @return HTTP 200 on success,
     *         HTTP 404 if no movie with the given title is found,
     *         or an error response on failure
     */
    @PostMapping(path = "/movies/update/{title}")
    public ResponseEntity<?> updateMovie(@PathVariable String title, @RequestBody MovieDto movieDto) {
        ResponseEntity<?> response = getAllMovies();  // Call the method
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST)
            return response;

        List<MovieDto> allMovies = (List<MovieDto>)response.getBody();
        try {
            for (MovieDto movieDto1 : allMovies) {
                if (movieDto1.getTitle().equals(title)) {
                    movieDto.setId(movieDto1.getId());
                    MovieEntity movieEntity = movieMapper.mapFrom(movieDto);
                    movieService.createMovie(movieEntity);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        catch(NoSuchElementException e){
            return errorHandler.notFound(title,e,currPath);
        }
        catch(Exception e){
            return errorHandler.badRequest("",e,currPath);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * PATCH /movies/update/{movieId}?field=trailerUrl&value=https://...
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/movies/update/{movieId}")
    public ResponseEntity<?> updateMovieVariable(
            @PathVariable Integer movieId,
            @RequestParam String field,
            @RequestParam String value) {

        MovieDto updated = movieService.patchMovie(field,movieId,value);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a movie by its identifier.
     * @param id movie identifier
     * @return HTTP 200 on success,
     *         HTTP 404 if the movie does not exist,
     *         or an error response on failure
     */
    @DeleteMapping(path = "/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") Integer id ) {
        try {
            logger.LogInfo("Delete movie "+id);
            movieService.deleteMovieById(id);
            logger.success();
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(NoSuchElementException e){
            return errorHandler.notFound(id,e,currPath);
        }
        catch(Exception e){
            return errorHandler.badRequest("",e,currPath);
        }
    }
    /**
     * Searches movies by free-text terms.
     * @param terms search expression provided by the client
     * @return HTTP 200 with a list of MovieDto that match the terms,
     *         possibly an empty list if no matches are found,
     *         or an error response on failure
     */
    @GetMapping(path = "/search/movies/{terms}")
    public ResponseEntity<?> getAllMoviesSearch(@PathVariable String terms) {
        try{
            logger.LogInfo("Client initiated a query, term = "+terms);
            logger.costume("System is searching");
            List<MovieEntity> movies = movieService.searchByTerm(terms);
            logger.fromEntity();
            List<MovieDto> moviesDto =
                    movies.isEmpty()?
                            List.of():
                            movies.stream().map(movieMapper::mapTo).collect(Collectors.toList());
            if (moviesDto.isEmpty())
                logger.costume("No matches were found");
            else
                logger.costume(moviesDto.size()+" matches were found ");
            logger.success();
            return new ResponseEntity<>(moviesDto,HttpStatus.OK);
        }
        catch(Exception e){
            return errorHandler.badRequest("",e,currPath);
        }
    }

    /**
     * Returns the IDs of movies by their titles, in the same order as provided.
     * @param titles list of movie titles
     * @return list of movie IDs
     */
    @PostMapping("/movies/ids-for-banner")
    public List<Integer> getIdsByTitles(@RequestBody List<String> titles) {
        return movieService.getIdsByTitles(titles);
    }
}


