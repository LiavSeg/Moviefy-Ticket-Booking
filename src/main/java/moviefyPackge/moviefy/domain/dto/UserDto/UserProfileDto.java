package moviefyPackge.moviefy.domain.dto.UserDto;

import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingDto;
import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String userName;
    private String email;
    private boolean isAdmin;
    private List<BookingDto> bookingsList;
    private List<ReviewResponseDto> reviewsList;
}
