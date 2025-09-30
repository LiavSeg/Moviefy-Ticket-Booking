package moviefyPackge.moviefy.security;

import moviefyPackge.moviefy.domain.Entities.UserEntity;
import moviefyPackge.moviefy.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * Responsible for loading a user from the database by username.
 * Used by Spring Security to authenticate login credentials.
 */
public class MoviefyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    /**
     * Constructs the service with access to the user repository.
     * @param userRepository repository used to retrieve users by username
     */
    public MoviefyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user by username and wraps it in a UserDetails object.
     * @param username the username used during authentication
     * @return UserDetails representation of the user
     * @throws UsernameNotFoundException if the user is not found in the system
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.getByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new MoviefyUserDetails(user);
    }
}
