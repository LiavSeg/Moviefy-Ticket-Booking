package moviefyPackge.moviefy.domain.dto.StatsDtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeRangeDto {
    String label;
    double count;
}
