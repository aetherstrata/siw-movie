package dev.aest.siw.movie.validation;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.repository.MovieRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MovieValidator implements Validator
{
    private MovieRepository movieRepository;

    public MovieValidator(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public boolean supports(@NotNull Class<?> type) {
        return Movie.class.equals(type);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        var movie = (Movie)target;

        if (movie.getTitle() != null &&
            movie.getYear() != null &&
            movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())){
            errors.reject("movie.duplicate", "Another movie with these properties already exists");
        }
    }
}
