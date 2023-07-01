package dev.aest.siw.movie.model;

import dev.aest.siw.movie.entity.Movie;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class MovieFormData
{
    @NotBlank(message = "{movie.title.blank}")
    private String title;

    @NotNull
    @Min(value = 1900, message = "{movie.year.min}")
    @Max(value = 2023, message = "{movie.year.max}")
    private Integer year;

    @Length(max = 1023, message = "{movie.synopsis.length}")
    private String synopsis;

    private MovieFormData(Movie movie){
        this.title = movie.getTitle();
        this.year = movie.getYear();
        this.synopsis = movie.getSynopsis();
    }

    public static MovieFormData of(Movie movie){
        if (movie == null) return null;
        return new MovieFormData(movie);
    }

    public Movie toMovie() {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setYear(year);
        movie.setSynopsis(synopsis);
        return movie;
    }
}
