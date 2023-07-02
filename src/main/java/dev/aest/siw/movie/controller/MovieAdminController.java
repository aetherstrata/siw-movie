package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.entity.Artist;
import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.model.MovieFormData;
import dev.aest.siw.movie.service.ArtistService;
import dev.aest.siw.movie.service.MovieFileService;
import dev.aest.siw.movie.service.MovieService;
import dev.aest.siw.movie.validation.MovieFormValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MovieAdminController
{
    private final MovieService movieService;
    private final MovieFileService movieFileService;
    private final MovieFormValidator movieFormValidator;

    private final ArtistService artistService;

    @GetMapping("/admin/movies/new")
    public String addNewMovie(Model model){
        model.addAttribute("form", new MovieFormData());
        return "admin/formAddMovie";
    }

    @PostMapping("/admin/movies/new")
    public String addNewMovie(
            @Valid @ModelAttribute("form") MovieFormData formData,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile[] images){
        this.movieFormValidator.validate(formData, bindingResult);
        if (bindingResult.hasErrors()) return "admin/formAddMovie";
        Movie movie = formData.toMovie();
        movieService.addImages(movie, images);
        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/admin/movies/{id}/update")
    public String updateMovieInfo(
            @PathVariable("id") final Long id,
            Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie == null) return MovieController.NOT_FOUND;
        model.addAttribute("movie_id", movie.getId());
        model.addAttribute("form", MovieFormData.of(movie));
        return "admin/updateMovie";
    }

    @PostMapping("/admin/movies/{id}/update")
    public String performMovieUpdate(
            @PathVariable("id") final Long id,
            @Valid @ModelAttribute("form") MovieFormData formData,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile[] images){
        Movie movie = movieService.getMovieWithImages(id);
        if (movie == null) return MovieController.NOT_FOUND;
        if (bindingResult.hasErrors()) return "admin/updateMovie";
        movieService.addImages(movie, images);
        movieService.updateMovie(movie, formData);
        return "redirect:/movies/%d".formatted(id);
    }

    @GetMapping("/admin/movies/{id}/updateDirector")
    public String updateMovieDirector(
            @PathVariable("id") final Long id,
            Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie == null) return MovieController.NOT_FOUND;
        model.addAttribute("movie", movie);
        model.addAttribute("artists", artistService.getAll().stream().filter(a -> !a.equals(movie.getDirector())).toList());
        return "/admin/manageMovieDirector";
    }

    @PostMapping(value="/admin/movies/{movieId}/setDirector/{directorId}")
    public String setDirectorToMovie(
            @PathVariable("movieId") final Long movieId,
            @PathVariable("directorId") final Long directorId) {
        Movie movie = movieService.getMovie(movieId);
        if (movie == null) return MovieController.NOT_FOUND;
        Artist director = artistService.getArtist(directorId);
        if (director == null) return ArtistController.NOT_FOUND;
        movie.setDirector(director);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/%d/updateDirector".formatted(movieId);
    }

    @PostMapping(value="/admin/movies/{movieId}/removeDirector")
    public String removeDirectorFromMovie(
            @PathVariable("movieId") final Long movieId) {
        Movie movie = movieService.getMovie(movieId);
        if (movie == null) return MovieController.NOT_FOUND;
        movie.setDirector(null);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/%d/updateDirector".formatted(movieId);
    }

    @GetMapping("/admin/movies/{id}/updateActors")
    public String updateMovieActors(
            @PathVariable("id") final Long id,
            Model model) {
        Movie movie = movieService.getDetailedMovie(id);
        if (movie == null) return MovieController.NOT_FOUND;
        model.addAttribute("movie", movie);
        model.addAttribute("available", artistService.getAvailableActorsFor(movie));
        return "/admin/manageMovieActors";
    }

    @PostMapping("/admin/movies/{movieId}/addActor/{actorId}")
    public String addActorToMovie(
            @PathVariable("movieId") final Long movieId,
            @PathVariable("actorId") final Long actorId) {
        Movie movie = movieService.getDetailedMovie(movieId);
        if (movie == null) return MovieController.NOT_FOUND;
        Artist actor = artistService.getArtist(actorId);
        if (actor == null) return ArtistController.NOT_FOUND;
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
        if (movie == null) return MovieController.NOT_FOUND;
        Artist actor = artistService.getArtist(actorId);
        if (actor == null) return ArtistController.NOT_FOUND;
        Set<Artist> actors = movie.getActors();
        actors.remove(actor);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/%d/updateActors".formatted(movieId);
    }

    @PostMapping("/admin/movies/{id}/delete")
    public String deleteMovie(
            @PathVariable("id") final Long id){
        Movie movie = movieService.getMovieWithImages(id);
        if (movie == null) return MovieController.NOT_FOUND;
        movieService.deleteMovie(movie);
        return "redirect:/movies";
    }
}
