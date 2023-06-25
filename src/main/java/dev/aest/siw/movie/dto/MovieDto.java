package dev.aest.siw.movie.dto;

import dev.aest.siw.movie.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto
{
    private Long id;
    private String title;
    private Integer year;
    private String synopsis;

    protected MovieDto(Movie movie){
        id = movie.getId();
        title = movie.getTitle();
        year = movie.getYear();
        synopsis = movie.getSynopsis();
    }

    public static MovieDto of(Movie movie){
        if (movie == null) return null;
        return new MovieDto(movie);
    }
}
