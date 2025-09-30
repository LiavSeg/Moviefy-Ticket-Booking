package moviefyPackge.moviefy.domain.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * Entity class representing a user in the system.
 * Each user can be either a customer or an admin (controlled via isAdmin flag).
 * The entity includes validation annotations to ensure proper input at creation/update.
 * Maps to the "users" table in the database.
 * Fields:
 * - userId: UUID primary key, generated automatically.
 * - userName: Unique username for login purposes.
 * - password: Hashed password (stored securely).
 * - email: Unique email address.
 * - isAdmin: Flag indicating if the user has admin privileges.
 */

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
@Table(name = "users")
public class UserEntity {

    //Primary Key (UUID) generated automatically
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(nullable = false, unique = true, length = 50)
    private String userName;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isAdmin;


}
