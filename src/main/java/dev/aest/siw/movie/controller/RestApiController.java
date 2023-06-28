package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.dto.*;
import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.service.ArtistService;
import dev.aest.siw.movie.service.MovieService;
import dev.aest.siw.movie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return movieService.getAllMovies().stream().map(MovieDto::of).toList();
    }

    @GetMapping("movie/{id}")
    public DetailedMovieDto getMovie(@PathVariable("id") final Long id){
        return DetailedMovieWithReviewsDto.of(movieService.getFullMovie(id), reviewService.getReviewsForMovie(id));
    }

    @GetMapping("artists")
    public List<ArtistDto> getAllArtists(){
        return artistService.getAll().stream().map(ArtistDto::of).toList();
    }

    @GetMapping("artist/{id}")
    public ArtistAndRelatedMoviesDto getArtist(@PathVariable("id") final Long id){
        Artist artist = artistService.getFullArtist(id);
        return  ArtistAndRelatedMoviesDto.of(
                artist,
                movieService.getDirectedMoviesByArtist(artist),
                movieService.getStarredMoviesByArtist(artist));
    }
}
