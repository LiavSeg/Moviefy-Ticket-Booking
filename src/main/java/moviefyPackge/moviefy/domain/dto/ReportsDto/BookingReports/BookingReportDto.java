package moviefyPackge.moviefy.domain.dto.ReportsDto.BookingReports;

import moviefyPackge.moviefy.domain.dto.ReportsDto.ReportTemplate;
import jakarta.xml.bind.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Represents the root element of a full booking report (XML export).
 * Fields:
 * - total: Total number of bookings included in the report.
 * - bookings: List of individual booking items (each represented by BookingItemReport).
 * Inherits date range and generation timestamp from ReportTemplate.
 * The field and element ordering matches the XSD definition.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlRootElement(name = "bookingsReport")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "total", "bookings" })
public class BookingReportDto  extends ReportTemplate {

    @XmlElement(required = true)
    private int total;

    @XmlElement(name = "booking")
    private List<BookingItemReport> bookings;
}
