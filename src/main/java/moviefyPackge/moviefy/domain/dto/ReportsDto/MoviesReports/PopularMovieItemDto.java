package moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
/**
 * Represents a popular movie entry in the popular movies report.
 * Fields:
 * - movieId: Unique identifier of the movie.
 * - movieTitle: Title of the movie.
 * - bookingsCount: Total number of bookings made for this movie.
 * - ticketsSold: Total number of tickets sold for this movie.
 * - totalIncome: Total income generated from this movie (in currency units).
 */
@SuperBuilder
@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "movieId", "movieTitle", "bookingsCount", "ticketsSold", "totalIncome" })
public class PopularMovieItemDto{
    @XmlElement(name = "movieId", required = true)
    private Integer movieId;

    @XmlElement(name = "movieTitle", required = true)
    private String movieTitle;
    @XmlElement(name = "bookingsCount")
    private Integer bookingsCount;

    @XmlElement(name = "ticketsSold")
    private Integer ticketsSold;

    @XmlElement(name = "totalIncome")
    private Integer totalIncome;

    public PopularMovieItemDto(Integer movieId, String movieTitle,
                               Long bookingsCount, Long ticketsSold, Integer  totalIncome) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.bookingsCount = bookingsCount.intValue();
        this.ticketsSold = ticketsSold.intValue();
        this.totalIncome = totalIncome;
    }
}