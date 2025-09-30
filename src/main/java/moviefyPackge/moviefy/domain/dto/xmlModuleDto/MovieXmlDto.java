package moviefyPackge.moviefy.domain.dto.xmlModuleDto;

import moviefyPackge.moviefy.services.Util.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieXmlDto {
    private String title;
    private String genre;
    private Integer duration;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate releaseDate;
    private String language;
    private String description;
    private String imageUrl;
    private String trailerUrl;
}