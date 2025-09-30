package moviefyPackge.moviefy.repositories;

import moviefyPackge.moviefy.domain.Entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,UUID> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<UserEntity> getByUserName(String userName);
    Optional<UserEntity> findByUserId(UUID userId);
    List<UserEntity> findAll();


}
