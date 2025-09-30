package moviefyPackge.moviefy.utils;

import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.repositories.MovieRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BannerDataInitializer {

    private final MovieRepository movieRepository;


    public BannerDataInitializer(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    private void initMovies() {
        saveIfNotExists(MovieEntity.builder()
                .title("The Fantastic 4: First Steps")
                .description("Marvel's First Family must defend Earth from Galactus while maintaining their family bond in a retro-futuristic 1960s world.")
                .duration(125)
                .genre("Sci-Fi")
                .imageUrl("https://image.tmdb.org/t/p/w500/x26MtUlwtWD26d0G0FXcppxCJio.jpg")
                .language("English")
                .releaseDate(LocalDate.of(2025, 7, 23))
                .trailerUrl("https://www.youtube.com/watch?v=18QQWa5MEcs")
                .build());

        saveIfNotExists(MovieEntity.builder()
                .title("Superman")
                .description("Superman, a journalist in Metropolis, embarks on a journey to reconcile his Kryptonian heritage with his human upbringing as Clark Kent.")
                .duration(132)
                .genre("Action")
                .imageUrl("https://image.tmdb.org/t/p/w500/ombsmhYUqR4qqOLOxAyr5V8hbyv.jpg")
                .language("English")
                .releaseDate(LocalDate.of(2025, 7, 9))
                .trailerUrl("https://www.youtube.com/watch?v=Ox8ZLF6cGM0")
                .build());

        saveIfNotExists(MovieEntity.builder()
                .title("Happy Gilmore 2")
                .description("Happy Gilmore returns to the golf course years after retirement to support his daughter's dreams, facing new rivals and old challenges.")
                .duration(98)
                .genre("Comedy")
                .imageUrl("https://image.tmdb.org/t/p/w500/ynT06XivgBDkg7AtbDbX1dJeBGY.jpg")
                .language("English")
                .releaseDate(LocalDate.of(2025, 7, 25))
                .trailerUrl("https://www.youtube.com/watch?v=YKzRPFvky9Y")
                .build());

        saveIfNotExists(MovieEntity.builder()
                .title("F1")
                .description("Racing legend Sonny Hayes comes out of retirement to lead a struggling Formula 1 team and mentor a young driver.")
                .duration(112)
                .genre("Drama")
                .imageUrl("https://image.tmdb.org/t/p/w500/6H6p82aWQFEKEuVUiZll6JxV8Ft.jpg")
                .language("English")
                .releaseDate(LocalDate.of(2025, 6, 25))
                .trailerUrl("https://www.youtube.com/watch?v=69ffwl-8pCU")
                .build());

        saveIfNotExists(MovieEntity.builder()
                .title("How to Train Your Dragon")
                .description("Viking Hiccup forms a secret bond with a dragon, challenging the tradition of dragon hunting on the isle of Berk.")
                .duration(104)
                .genre("Fantasy")
                .imageUrl("https://image.tmdb.org/t/p/w500/53dsJ3oEnBhTBVMigWJ9tkA5bzJ.jpg")
                .language("English")
                .releaseDate(LocalDate.of(2025, 6, 6))
                .trailerUrl("https://www.youtube.com/watch?v=22w7z_lT6YM")
                .build());
    }

    private void saveIfNotExists(MovieEntity movie) {
        if (movieRepository.getByTitle(movie.getTitle()).isEmpty()) {
            movieRepository.save(movie);
        }
    }
}
