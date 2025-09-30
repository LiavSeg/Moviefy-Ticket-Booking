package moviefyPackge.moviefy.domain.dto.xmlModuleDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class XmlRequestDto {
    @NotNull
    private String opType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer movieId;

}
