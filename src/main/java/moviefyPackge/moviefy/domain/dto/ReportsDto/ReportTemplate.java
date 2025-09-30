package moviefyPackge.moviefy.domain.dto.ReportsDto;

import moviefyPackge.moviefy.services.Util.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
/**
 * Fields:
 * - generatedAt: Timestamp indicating when the report was generated.
 * - range: The requested date range used for generating the report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "generatedAt", "range" })
public class ReportTemplate {

    /**
     * Timestamp indicating when the report was generated.
     * Automatically initialized to the current server time.
     */
    @Builder.Default
    @XmlElement(name = "generatedAt", required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime generatedAt = LocalDateTime.now();

    @XmlElement(name = "range", required = true)
    private DateRangeDto range;
}
