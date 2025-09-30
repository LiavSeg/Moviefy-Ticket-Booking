package moviefyPackge.moviefy.domain.Entities;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data // Equals, toString, etc..
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table (name = "movies")
public class MovieEntity {
    @Id
    @SequenceGenerator(
            name = "movie_id_sequence",
            sequenceName = "movie_id_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movie_id_sequence"
    )
    private Integer id;

    @NotNull(message = "Movie title can't be empty")
    @NotBlank(message = "Movie title can't be blank")
    private String title;

    @NotNull(message = "Movie genre can't be empty")
    @Size(min = 1, max = 50)
    private String genre;

    @Positive(message = "Movie duration must be a positive integer")
    private Integer duration;

    @Min(message = "Movie rating's minimum value is 0", value = 0)
    @Max(message = "Movie rating's maximum value is 10", value = 10)
    private Double rating;

    @Column(name = "release_date")
    private LocalDate releaseDate;
    private String language;
    @Column(columnDefinition = "TEXT")
    private String description;

    @URL(message = "Image URL must be a valid URL")
    private String imageUrl;

    @URL(message = "Trailer URL must be a valid URL")
    @Column(name = "trailer_url")
    private String trailerUrl;

}

