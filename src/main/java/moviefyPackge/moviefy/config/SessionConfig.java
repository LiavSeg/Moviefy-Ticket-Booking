package moviefyPackge.moviefy.config;

import jakarta.servlet.SessionCookieConfig;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures session cookie behavior for local development.
 *
 * Disables the Secure flag on the JSESSIONID cookie so it can be sent over HTTP
 * (useful when running the app locally without HTTPS).
 * Also ensures the cookie is HttpOnly to prevent access from JavaScript.
 *
 * This setup is not meant for production.
 */
@Configuration
public class SessionConfig {

    /**
     * Customizes the session cookie to work over HTTP.
     * Required so the browser will actually send JSESSIONID
     * when developing locally without HTTPS.
     */
    @Bean
    public ServletContextInitializer sessionCookieCustomizer() {
        return servletContext -> {
            SessionCookieConfig config = servletContext.getSessionCookieConfig();
            config.setSecure(false);          // Needed for HTTP (localhost)
            config.setHttpOnly(true);         // Prevent JS access
            config.setName("JSESSIONID");     // Default name, explicitly set
        };
    }
}
