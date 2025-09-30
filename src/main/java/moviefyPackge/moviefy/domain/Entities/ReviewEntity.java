package moviefyPackge.moviefy.domain.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

//    @NotNull(message = "Movie is required for the review")
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    @Column(nullable = false)
    private Integer rating;

    @NotBlank(message = "Review is required")
    @Column(nullable = false)
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
