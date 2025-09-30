package moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports;

import moviefyPackge.moviefy.domain.dto.ReportsDto.ReportTemplate;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * DTO representing the full report of most popular movies.
 * Fields:
 * - moviesList: List of popular movies with booking and income statistics.
 */
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "popularMoviesReport")
public class PopularMovieRepDto extends ReportTemplate {

    @XmlElement(name = "movie")
    private List<PopularMovieItemDto> moviesList;
}
