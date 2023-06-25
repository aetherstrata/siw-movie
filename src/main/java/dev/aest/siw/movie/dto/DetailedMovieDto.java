package dev.aest.siw.movie.dto;

import dev.aest.siw.movie.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DetailedMovieDto extends MovieDto
{
    private List<String> imageUrls;
    private ArtistDto director;
    private List<ArtistDto> actors;

    protected DetailedMovieDto(Movie movie) {
        super(movie);
        this.imageUrls = movie.getImageUrls();
        this.director = ArtistDto.of(movie.getDirector());
        this.actors = movie.getActors().stream().map(ArtistDto::of).toList();
    }

    public static DetailedMovieDto of(Movie movie) {
        if (movie == null) return null;
        return new DetailedMovieDto(movie);
    }
}
