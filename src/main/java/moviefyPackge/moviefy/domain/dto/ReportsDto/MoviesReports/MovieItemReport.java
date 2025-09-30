package moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * Basic movie details used in reports.
 * Fields:
 * - movieId: Unique identifier of the movie.
 * - title: Title of the movie.
 * - genre: Movie genre (e.g., Action, Drama).
 * - language: Original language of the movie.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieItemReport {
    private Integer movieId;
    private String title;
    private String genre;
    private String language;
}