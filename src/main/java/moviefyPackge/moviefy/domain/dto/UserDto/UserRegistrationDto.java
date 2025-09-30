package moviefyPackge.moviefy.domain.dto.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserRegistrationDto {
    //Username
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    //Email
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    //Password
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
