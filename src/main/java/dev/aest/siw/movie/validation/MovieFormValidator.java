package dev.aest.siw.movie.validation;

import dev.aest.siw.movie.model.MovieFormData;
import dev.aest.siw.movie.repository.MovieRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MovieFormValidator implements Validator
{
    private final MovieRepository movieRepository;

    @Override
    public boolean supports(@NotNull Class<?> type) {
        return MovieFormData.class.equals(type);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        var movie = (MovieFormData)target;

        if (movie.getTitle() != null && movie.getYear() != null &&
            movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())){
            errors.reject("movie.duplicate", "Another movie with these properties already exists");
        }
    }
}
