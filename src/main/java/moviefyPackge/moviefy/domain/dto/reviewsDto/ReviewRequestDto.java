package moviefyPackge.moviefy.domain.dto.reviewsDto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


/**
 * Carries review submission data from the client.
 * Fields:
 * - userId: The unique identifier of the user submitting the review; Required.
 * - rating: The numeric rating given by the user (1-10); Required.
 * - comment: The textual feedback. Required; must be meaningful and within the specified length.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewRequestDto {

    @NotNull
    private UUID userId;

    @NotNull
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    private Integer rating;

    @NotBlank(message = "A review can not be empty")
    @Size(min = 5, max = 500, message = "A review must contain 5 to 500 characters")
    private String comment;

}
