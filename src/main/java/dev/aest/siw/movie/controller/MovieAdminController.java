package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.repository.ArtistRepository;
import dev.aest.siw.movie.service.ArtistService;
import dev.aest.siw.movie.service.MovieFileService;
import dev.aest.siw.movie.service.MovieService;
import dev.aest.siw.movie.validation.MovieValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MovieAdminController
{
    private final MovieService movieService;
    private final MovieFileService movieFileService;
    private final MovieValidator movieValidator;

    private final ArtistRepository artistRepository;
    private final ArtistService artistService;

    @GetMapping("/admin/movies/new")
    public String addNewMovie(Model model){
        model.addAttribute("movie", new Movie());
        return "admin/formAddMovie";
    }

    @PostMapping("/admin/movies/new")
    public String addNewMovie(
            @Valid @ModelAttribute("movie") Movie movie,
            BindingResult movieBinding,
            @RequestParam("image") MultipartFile[] images){
        this.movieValidator.validate(movie, movieBinding);
        if (!movieBinding.hasErrors()){
            Arrays.stream(images).forEach(image -> {
                if (image != null && !image.isEmpty())
                    movie.getImageUrls().add(movieFileService.save(image));
            });
            movieService.saveMovie(movie);
            return "redirect:/movies";
        }
        return "admin/formAddMovie";
    }

    @GetMapping("/admin/movies/{id}/update")
    public String updateMovieInfo(
            @PathVariable("id") final Long id,
            Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie == null) return "movies/notFound";
        model.addAttribute("movie", movie);
        return "admin/updateMovie";
    }

    @PostMapping("/admin/movies/{id}/update")
    public String performMovieUpdate(
            @PathVariable("id") final Long id,
            @Valid @ModelAttribute("movie") Movie movie,
            BindingResult movieBinding,
            @RequestParam("image") MultipartFile[] images){
        Movie dbMovie = movieService.getMovieWithImages(id);
        if (dbMovie == null || !dbMovie.getId().equals(movie.getId())) return "movies/notFound";
        if (movieBinding.hasErrors()) return "admin/updateMovie";
        Arrays.stream(images).forEach(image -> {
            if (image != null && !image.isEmpty()){
                dbMovie.getImageUrls().add(movieFileService.save(image));
            }
        });
        dbMovie.setTitle(movie.getTitle());
        dbMovie.setSynopsis(movie.getSynopsis());
        dbMovie.setYear(movie.getYear());
        movieService.saveMovie(dbMovie);
        return "redirect:/movies/%d".formatted(id);
    }

    @GetMapping("/admin/movies/{id}/updateDirector")
    public String updateMovieDirector(
            @PathVariable("id") final Long id,
            Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie == null) return "movies/notFound";
        model.addAttribute("movie", movie);
        model.addAttribute("artists", artistRepository.findAll().stream().filter(a -> !Objects.equals(a, movie.getDirector())).toList());
        return "/admin/manageMovieDirector";
    }

    @PostMapping(value="/admin/movies/{movieId}/setDirector/{directorId}")
    public String setDirectorToMovie(
            @PathVariable("movieId") final Long movieId,
            @PathVariable("directorId") final Long directorId) {
        Movie movie = movieService.getMovie(movieId);
        Artist director = artistService.getArtist(directorId);
        if (movie == null) return "movies/notFound";
        if (director == null) return "artists/notFound";
        movie.setDirector(director);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/%d/updateDirector".formatted(movieId);
    }

    @PostMapping(value="/admin/movies/{movieId}/removeDirector")
    public String removeDirectorFromMovie(
            @PathVariable("movieId") final Long movieId) {
        Movie movie = movieService.getMovie(movieId);
        if (movie == null) return "movies/notFound";
        movie.setDirector(null);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/%d/updateDirector".formatted(movieId);
    }

    @GetMapping("/admin/movies/{id}/updateActors")
    public String updateMovieActors(
            @PathVariable("id") final Long id,
            Model model) {
        Movie movie = movieService.getDetailedMovie(id);
        if (movie == null) return "movies/notFound";
        List<Artist> availableArtists = artistRepository.findByStarredMoviesNotContains(movie);
        model.addAttribute("movie", movie);
        model.addAttribute("available", availableArtists);
        return "/admin/manageMovieActors";
    }

    @PostMapping("/admin/movies/{movieId}/addActor/{actorId}")
    public String addActorToMovie(
            @PathVariable("movieId") final Long movieId,
            @PathVariable("actorId") final Long actorId) {
        Movie movie = movieService.getDetailedMovie(movieId);
        Artist actor = artistService.getArtist(actorId);
        if (movie == null) return "movies/notFound";
        if (actor == null) return "artists/notFound";
        Set<Artist> actors = movie.getActors();
        actors.add(actor);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/%d/updateActors".formatted(movieId);
    }

    @PostMapping("/admin/movies/{movieId}/removeActor/{actorId}")
    public String removeActorFromMovie(
            @PathVariable("movieId") final Long movieId,
            @PathVariable("actorId") final Long actorId) {
        Movie movie = movieService.getDetailedMovie(movieId);
        Artist actor = artistService.getArtist(actorId);
        if (movie == null) return "movies/notFound";
        if (actor == null) return "artists/notFound";
        Set<Artist> actors = movie.getActors();
        actors.remove(actor);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/%d/updateActors".formatted(movieId);
    }

    @PostMapping("/admin/movies/{id}/delete")
    public String deleteMovie(
            @PathVariable("id") final Long id){
        Movie movie = movieService.getMovie(id);
        if (movie == null) return "movies/notFound";
        movieService.deleteMovie(movie);
        return "redirect:/movies";
    }
}
