package moviefyPackge.moviefy.services;


import moviefyPackge.moviefy.domain.Entities.XmlEntity;
import moviefyPackge.moviefy.domain.dto.xmlModuleDto.XmlRequestDto;
import moviefyPackge.moviefy.domain.dto.xmlModuleDto.XmlResponseDto;
import moviefyPackge.moviefy.enums.Xml.XmlImportType;
import moviefyPackge.moviefy.enums.Xml.XmlOpType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface XmlService {
    XmlEntity createXmlEntity(XmlEntity xmlEntity);
    XmlEntity importFromXml(MultipartFile xmlFile,XmlImportType type);
    String exportBookingReport(XmlRequestDto reportRequestDto,String format);
    String convertXmlToHtml(String type);
    String exportPopularMoviesReport(XmlRequestDto reportRequestDto,String format);

}
