package moviefyPackge.moviefy.security;

import moviefyPackge.moviefy.domain.Entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
/**
 * Custom implementation of Spring Security's UserDetails.
 * Wraps a UserEntity and exposes authentication-related attributes
 * such as username, password, and granted authorities.
 * Used by the security framework to establish authentication context.
 */
public class MoviefyUserDetails implements UserDetails {
    private final UserEntity user;

    /**
     * Constructs a MoviefyUserDetails from a given user entity.
     * @param user the user being wrapped
     */
    public MoviefyUserDetails(UserEntity user){this.user=user;}

    /**
     * Returns granted authorities based on user role.
     * @return ROLE_ADMIN if user is admin, otherwise ROLE_USER
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.isAdmin()?"ROLE_ADMIN":"ROLE_USER";
        return List.of(new SimpleGrantedAuthority(role));
    }

    /** Default methods overriding*/

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
