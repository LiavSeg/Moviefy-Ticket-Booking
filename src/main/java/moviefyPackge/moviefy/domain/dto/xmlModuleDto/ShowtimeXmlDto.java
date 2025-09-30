package moviefyPackge.moviefy.domain.dto.xmlModuleDto;

import moviefyPackge.moviefy.services.Util.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ShowtimeXmlDto {
    
    private Integer movieId;
    @XmlElement(name = "theater")
    private String theater;

    @XmlElement(name = "startTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime startTime;

    @XmlElement(name = "endTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime endTime;

    @XmlElement(name = "price")
    private Double price;
}
