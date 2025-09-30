package moviefyPackge.moviefy.domain.dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPassChangeDto {

    @NotBlank(message = "Current password is invalid")
    @Size(min = 8, message =  "Current password is invalid")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must contain at least 8 characters")
    private String newPassword;

}
