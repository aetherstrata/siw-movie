package dev.aest.siw.movie.dto;

import dev.aest.siw.movie.entity.Artist;
import dev.aest.siw.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArtistAndRelatedMoviesDto extends ArtistDto
{
    private List<MovieDto> directed;
    private List<MovieDto> starred;

    private ArtistAndRelatedMoviesDto(Artist artist, Collection<Movie> directed, Collection<Movie> starred){
        super(artist);
        this.directed = directed.stream().map(MovieDto::of).toList();
        this.starred = starred.stream().map(MovieDto::of).toList();
    }

    public static ArtistAndRelatedMoviesDto of(Artist artist, Collection<Movie> directed, Collection<Movie> starred){
        if (artist == null) return null;
        return new ArtistAndRelatedMoviesDto(artist, directed, starred);
    }
}
