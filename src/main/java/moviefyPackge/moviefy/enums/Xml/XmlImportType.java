package moviefyPackge.moviefy.enums.Xml;

import moviefyPackge.moviefy.domain.dto.xmlModuleDto.MovieXmlWrapper;
import moviefyPackge.moviefy.domain.dto.xmlModuleDto.ShowtimeXmlWrapper;
import lombok.Getter;
@Getter
public enum XmlImportType {

    MOVIES("XSD/EntitiesImportValidation/movies.xsd", MovieXmlWrapper.class),
    SHOWTIMES("XSD/EntitiesImportValidation/showtimes.xsd", ShowtimeXmlWrapper.class);

    private final String xdsFileName;
    private final Class<?> wrapperClass;

    XmlImportType(String xdsFileName, Class<?> wrapperClass1) {
        this.xdsFileName = xdsFileName;
        this.wrapperClass = wrapperClass1;
    }

}
