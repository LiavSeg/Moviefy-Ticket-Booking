package moviefyPackge.moviefy.errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Represents a structured error response returned by the API.
 *
 * This class is used to standardize error payloads across the system,
 * providing detailed context for debugging and client display.
 * It includes HTTP status, timestamp, path, and optional error details.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private OffsetDateTime timestamp;// The timestamp when the error occurred.
    private int status;// The HTTP status code (e.g., 404, 400, 500).
    private String error;//A short label for the error type (e.g., "NOT FOUND", "BAD_REQUEST").
    private String path;//The path or endpoint where the error occurred.
    private String details;//A detailed description or message explaining the error.
}
