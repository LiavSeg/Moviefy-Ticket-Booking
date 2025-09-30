package moviefyPackge.moviefy.domain.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.UUID;
/**
 * DTO representing the response returned to the client
 * after user registration, login, or profile retrieval.

 * Inherits common user fields (username and email) from UserDto.
 * This class is used strictly for server-to-client communication,
 * and does not include sensitive fields like passwords.

 * Fields:
 * - userId: UUID representing the unique identifier of the user.
 * - isAdmin: Boolean flag indicating whether the user has administrative privileges.

 * This DTO does not include validation annotations since it represents
 * data originating from validated and persisted entities.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class UserResponseDto{
    private UUID userId;
    private String username;
    private String email;

    @JsonProperty("isAdmin")//Force the JSON property name to "isAdmin" so the frontend can rely on it.
    private boolean isAdmin;
}

