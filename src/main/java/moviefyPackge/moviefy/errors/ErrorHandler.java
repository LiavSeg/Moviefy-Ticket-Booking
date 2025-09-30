package moviefyPackge.moviefy.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
/**
 * Handles structured error responses for common HTTP error types.
 * This class provides helper methods to build and return detailed error responses,
 * including status codes, timestamps, and paths, with consistent logging.
 * It is intended for use inside controllers or services where errors
 * should be returned as formatted JSON responses.
 */
public class ErrorHandler {
    private final Logger logger;
    /**
     * Constructs an ErrorHandler for the specified class, used for logging context.
     * @param c the class for which the logger will be scoped
     */
    public ErrorHandler(Class<?> c) {
        logger = LoggerFactory.getLogger(c);
    }

    /**
     * Returns a 404 Not Found response with error details and logs the error.
     * @param resource the requested resource (used for path building)
     * @param e the exception that caused the error
     * @param path the endpoint path
     * @return a ResponseEntity containing a structured ErrorResponse and HTTP 404
     */
    public <T> ResponseEntity<?> notFound(T resource,Exception e,String path) {
        logger.error("Requested resource could not be found: Response Code: 404. Details: {}", e.getMessage());
        String currPath = path+"/"+resource;
        if (resource == "")
            currPath = path;
        ErrorResponse errorResponse = new ErrorResponse(OffsetDateTime.now(),
                HttpStatus.NOT_FOUND.value(),"NOT FOUND",currPath,e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    /**
     * Returns a 409 Conflict response with error details and logs the conflict.
     * @param resource the conflicting resource (used for path building)
     * @param e the exception that caused the conflict
     * @param path the endpoint path
     * @return a ResponseEntity containing a structured ErrorResponse and HTTP 409
     */
    public <T> ResponseEntity<?> conflict(T resource,Exception e,String path) {
        String currPath = path+"/"+resource;
        if (resource == "")
            currPath = path;
        logger.error("Conflict detected: Response Code: 409. Details: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(OffsetDateTime.now(),
                HttpStatus.CONFLICT.value(),"CONFLICT",currPath,e.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    /**
     * Returns a 400 Bad Request response with error details and logs the error.
     * @param resource the relevant resource (used for path building)
     * @param e the exception that caused the bad request
     * @param path the endpoint path
     * @return a ResponseEntity containing a structured ErrorResponse and HTTP 400
     */
    public <T> ResponseEntity<?> badRequest(T resource,Exception e,String path) {
        String currPath = path+"/"+resource;
        if (resource == "")
            currPath = path;
        logger.error("Bad request: Response Code: 400. Details: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),"BAD_REQUEST",currPath,e.getMessage());
        System.out.print(errorResponse);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

}
