package moviefyPackge.moviefy.security;

import moviefyPackge.moviefy.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Security configuration for the Moviefy application.
 * Defines custom authentication setup, endpoint access rules,
 * CSRF policy, and user role-based authorization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Provides a custom UserDetailsService implementation for Spring Security.
     * @param userRepository the repository used to load user details
     * @return custom UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new MoviefyUserDetailsService(userRepository);
    }
    /**
     * Configures the main Spring Security filter chain.
     * CSRF is disabled to support stateless authentication with session cookies.
     * @param http HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register","/users/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/all").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .build();
    }

    /**
     * Provides the AuthenticationManager used during login.
     * @param config the authentication configuration provided by Spring
     * @return AuthenticationManager
     * @throws Exception if unable to retrieve authentication manager
     */
    @Bean
    public AuthenticationManager authManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the password encoder used for hashing passwords.
     * @return BCrypt-based password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
