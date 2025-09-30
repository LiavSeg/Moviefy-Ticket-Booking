package moviefyPackge.moviefy.domain.dto.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor; //import lombok.AllArgsConstructor; //
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO representing the shared user fields used in multiple user-related operations.

 * This class serves as a base DTO for input and output structures, and includes
 * core identification fields such as username and email.

 * Fields:
 * - username: String representing the user's unique name. Must be between 3 and 50 characters.
 * - email: String representing the user's email address in a valid format.

 * Validation annotations are applied to enforce input correctness
 * when receiving data from the client.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

}
