package moviefyPackge.moviefy.domain.Entities;

import moviefyPackge.moviefy.domain.Entities.validations.ValidStartEndTimes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "showtimes")
@ValidStartEndTimes
public class ShowtimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "showtime_id_sequence")
    private Integer id;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "movieId")
    private MovieEntity movie;
    @NotNull
    private String theater;
    @NotNull(message = "Showtime must have a start time")
    private LocalDateTime startTime;
    @NotNull(message = "Showtime must have an end time")
    private LocalDateTime endTime;


//    @PrePersist
//    public void endTimeCalculation(){
//        Integer duration = movie.getDuration();
//        if (startTime == null && duration !=null){
//            OffsetDateTime start = OffsetDateTime.parse(startTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
//            OffsetDateTime end = start.plusMinutes(duration);
//            this.endTime = end.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
//        }
//    }
}
