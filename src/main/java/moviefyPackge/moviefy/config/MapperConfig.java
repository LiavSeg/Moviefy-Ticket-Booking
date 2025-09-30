package moviefyPackge.moviefy.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a ModelMapper bean to enable automatic object mapping.
 * This setup allows Spring to convert between DTOs and Entities
 * without requiring manual field mapping.
 */
@Configuration
public class MapperConfig {

    /**
     * Returns the default ModelMapper instance used for object mapping.
     * This is required for mapping request/response DTOs to domain Entities.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
