package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.dto.*;
import dev.aest.siw.movie.entity.Artist;
import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.service.ArtistService;
import dev.aest.siw.movie.service.MovieService;
import dev.aest.siw.movie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RestApiController
{
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final ArtistService artistService;

    @GetMapping("movies")
    public List<MovieDto> getAllMovies(){
        List<MovieDto> movies = movieService.getAllMovies().stream().map(MovieDto::of).toList();
        if (movies.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no movies in the database");
        return movies;
    }

    @GetMapping("movie/{id}")
    public DetailedMovieDto getMovie(@PathVariable("id") final Long id){
        DetailedMovieWithReviewsDto movieDto = DetailedMovieWithReviewsDto.of(movieService.getFullMovie(id), reviewService.getReviewsForMovie(id));
        if (movieDto == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This movie does not exist");
        return movieDto;
    }

    @GetMapping("artists")
    public List<ArtistDto> getAllArtists(){
        List<ArtistDto> artists = artistService.getAll().stream().map(ArtistDto::of).toList();
        if (artists.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no artists in the database");
        return artists;
    }

    @GetMapping("artist/{id}")
    public ArtistAndRelatedMoviesDto getArtist(@PathVariable("id") final Long id){
        Artist artist = artistService.getFullArtist(id);
        if (artist == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This artist does not exist");
        return  ArtistAndRelatedMoviesDto.of(
                artist,
                movieService.getDirectedMoviesByArtist(artist),
                movieService.getStarredMoviesByArtist(artist));
    }
}
