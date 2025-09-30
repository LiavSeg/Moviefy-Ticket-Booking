package moviefyPackge.moviefy.domain.dto.xmlModuleDto;

import moviefyPackge.moviefy.enums.Xml.XmlStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class XmlResponseDto {
    private String fileName;
    private String  opType;
    private LocalDateTime timestamp;
    private XmlStatus status;

}
