package moviefyPackge.moviefy.domain.Entities;


import moviefyPackge.moviefy.enums.Xml.XmlStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
@Table(name = "xml_logs")
public class XmlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long xmlReportId;

    @Column(nullable = false, length = 50)
    private String fileName;

    @Column(nullable = false)
    private String opType;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private XmlStatus status;

    @Column(nullable = false)
    String storagePath;
}

