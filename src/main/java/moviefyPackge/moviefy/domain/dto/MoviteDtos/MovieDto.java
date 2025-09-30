package moviefyPackge.moviefy.domain.dto.MoviteDtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor //VERY important when you use JSON files, it will use this to create the object and use setters and getters to fill the file
@Builder
public class MovieDto {
    private Integer id;
    private String title;
    private String genre;
    private Integer duration;
//    private Double rating;
    private LocalDate releaseDate;
    private String language;
    private String description;
    private String imageUrl;
    private String trailerUrl;
}