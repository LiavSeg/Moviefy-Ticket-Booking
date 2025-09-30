package moviefyPackge.moviefy.repositories;

import moviefyPackge.moviefy.domain.Entities.ReviewEntity;
import moviefyPackge.moviefy.domain.Entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
/**
 * Repository interface for managing reviews in the system.
 * Provides basic CRUD operations and query methods to retrieve
 * reviews by user or movie, and check for existing reviews by user and movie.
 */
@Repository
public interface ReviewRepository extends CrudRepository<ReviewEntity,Long> {
    List<ReviewEntity> findAllByUser(UserEntity user);
    boolean existsByUser_userIdAndMovie_Id(UUID userId, Integer movieId);
    List<ReviewEntity> findAllByMovieId(Integer movieId);

}
