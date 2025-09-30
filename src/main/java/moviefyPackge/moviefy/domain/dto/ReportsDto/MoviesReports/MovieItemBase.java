package moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base representation of a movie for use in XML reports.
 * Fields:
 * - movieId: Unique identifier of the movie.
 * - movieTitle: Human-readable title of the movie.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD) // This is the crucial fix
public class MovieItemBase {
    @XmlElement(name = "movieId", required = true)
    private Integer movieId;

    @XmlElement(name = "movieTitle", required = true)
    private String movieTitle;
}
