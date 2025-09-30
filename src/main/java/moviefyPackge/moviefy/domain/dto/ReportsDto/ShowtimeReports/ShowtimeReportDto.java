package moviefyPackge.moviefy.domain.dto.ReportsDto.ShowtimeReports;

import moviefyPackge.moviefy.domain.dto.ReportsDto.MoviesReports.MovieItemReport;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.OffsetDateTime;
import java.util.List;

@XmlRootElement(name = "showtimesReport")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShowtimeReportDto {
    private final String version = "1.0";
    private OffsetDateTime generatedAt;

    @XmlElement(name = "movie")
    private MovieItemReport movie;

    private int total;

    @XmlElement(name = "showtime")
    private List<ShowtimeItemReport> showtimes;
}