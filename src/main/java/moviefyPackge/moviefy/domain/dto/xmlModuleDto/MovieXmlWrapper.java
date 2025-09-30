package moviefyPackge.moviefy.domain.dto.xmlModuleDto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "movies")
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieXmlWrapper {

    @XmlElement(name = "movie")
    private List<MovieXmlDto> movies;

}