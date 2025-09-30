package moviefyPackge.moviefy.domain.dto.ReportsDto;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import moviefyPackge.moviefy.services.Util.LocalDateTimeAdapter;
/**
 * Represents a date-time range used for filtering/generating reports.
 * Fields:
 * - from: Start of the range (inclusive). Must be a valid ISO-8601 timestamp.
 * - to: End of the range (inclusive). Must be a valid ISO-8601 timestamp.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "from", "to" })
public class DateRangeDto {
        @XmlElement(name = "from", required = true)
        @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
        private LocalDateTime from;
        @XmlElement(name = "to", required = true)
        @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
        private LocalDateTime to;
}
