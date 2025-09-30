package moviefyPackge.moviefy.domain.dto.xmlModuleDto;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;
import java.util.List;


@Data
@XmlRootElement(name = "showtimes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShowtimeXmlWrapper {

    @XmlElement(name = "showtime")
    private List<ShowtimeXmlDto> showtimes;


    public ShowtimeXmlWrapper() {}

}
