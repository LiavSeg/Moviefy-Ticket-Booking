package moviefyPackge.moviefy.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.List;
/**
 * Utility class for managing Spring Security context within HTTP sessions.
 * Used to programmatically set or clear authentication tokens for users
 * based on their role during registration, login, or logout.
 */
@UtilityClass
public class SecurityContextHelper {

    /**
     * Establishes an authenticated session for a user.
     * Sets the appropriate Spring Security context based on the user's role
     * and persists it in the HTTP session for future requests.
     * @param username the authenticated username
     * @param isAdmin whether the user has admin privileges
     * @param request the incoming HTTP request
     * @param response the HTTP response to attach session context
     */
    public void establishSessionAuth(String username, boolean isAdmin,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        var authorities = isAdmin
                ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                : List.of(new SimpleGrantedAuthority("ROLE_USER"));

        var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, request, response);
    }
    /**
     * Clears the current security context and removes session authentication.
     * Useful during logout or session invalidation.
     * @param request the current HTTP request
     * @param response the current HTTP response
     */
    public void clearSessionAuth(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.createEmptyContext(), request, response);
    }
}


