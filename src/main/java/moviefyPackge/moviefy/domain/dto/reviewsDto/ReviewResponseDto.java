package moviefyPackge.moviefy.domain.dto.reviewsDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Review data returned to the client.
 * Fields:
 * - username: Display name of the user who submitted the review.
 * - movieTitle: Title of the movie that was reviewed.
 * - rating: User's numeric score for the movie (1–5).
 * - comment: The actual review text written by the user.
 * - createdAt: The timestamp when the review was submitted.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewResponseDto {
    private String username;
    private String movieTitle;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
