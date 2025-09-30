package moviefyPackge.moviefy.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the application.

 * This setup allows the frontend (running on http://localhost:5173) to make
 * cross-origin HTTP requests to the backend during development.
 * It enables cookies (JSESSIONID) to be sent with requests by allowing credentials.

 * Note:
 * - This configuration is intended for local development.
 * - For production, CORS should be restricted to known domains and methods.
 */
@Configuration
public class CorsConfig {

    /**
     * Defines CORS rules for the entire application.
     * Allows credentials and enables specific HTTP methods and headers
     * from the frontend running on localhost:5173.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
            registry.addMapping("/**")// Applies to all endpoints
                        .allowedOrigins("http://localhost:5173")// React dev server
                        .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")// Accept all headers
                        .allowCredentials(true)// Enable cookies (JSESSIONID)
                        .maxAge(3600);// Cache preflight response for 1 hour
            }
        };
    }
}
