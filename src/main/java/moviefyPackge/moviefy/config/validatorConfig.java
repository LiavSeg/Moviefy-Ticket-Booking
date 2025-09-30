package moviefyPackge.moviefy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import jakarta.validation.Validator;
/**
 * Provides a Validator bean to enable automatic validation of request DTOs.
 *
 * This setup allows Spring to process annotations like @NotNull, @Email, @Size, etc.
 * on controller inputs and other validated components.
 */
@Configuration
public class validatorConfig {

    /**
     * Returns the default Validator used by Spring for bean validation.
     * This is required for @Valid and related annotations to work properly.
     */
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
