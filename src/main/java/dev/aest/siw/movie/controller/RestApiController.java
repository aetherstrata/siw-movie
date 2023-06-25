package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.dto.ArtistAndRelatedMoviesDto;
import dev.aest.siw.movie.dto.ArtistDto;
import dev.aest.siw.movie.dto.DetailedMovieDto;
import dev.aest.siw.movie.dto.DetailedMovieWithReviewsDto;
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
    public List<DetailedMovieDto> getAllMovies(){
        return movieService.getAllFullMovies().stream().map(DetailedMovieDto::of).toList();
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
        return  ArtistAndRelatedMoviesDto.of(
                artistService.getFullArtist(id),
                movieService.getDirectedMoviesByArtistId(id),
                movieService.getStarredMoviesByArtistId(id));
    }
}
