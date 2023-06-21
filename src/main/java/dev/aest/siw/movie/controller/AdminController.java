package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.repository.ArtistRepository;
import dev.aest.siw.movie.repository.MovieRepository;
import dev.aest.siw.movie.repository.ReviewRepository;
import dev.aest.siw.movie.service.ArtistFileService;
import dev.aest.siw.movie.service.ArtistService;
import dev.aest.siw.movie.service.MovieFileService;
import dev.aest.siw.movie.service.MovieService;

import dev.aest.siw.movie.validation.MovieValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AdminController
{
    private final MovieService movieService;
    private final MovieFileService movieFileService;
    private final MovieValidator movieValidator;

    private final ArtistFileService artistFileService;
    private final ArtistService artistService;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final ArtistRepository artistRepository;

    @GetMapping("/admin")
    public String adminPage(Model model){
        return "admin/index";
    }


    @GetMapping("/admin/movies/new")
    public String addNewMovie(Model model){
        model.addAttribute("movie", new Movie());
        return "admin/formAddMovie";
    }

    @PostMapping("/admin/movies/new")
    public String addNewMovie(
            @Valid @ModelAttribute("movie") Movie movie,
            BindingResult movieBinding,
            @RequestParam("image") MultipartFile image){
        this.movieValidator.validate(movie, movieBinding);
        if (!movieBinding.hasErrors()){
            if (image != null && !image.isEmpty()){
                movie.setImageUrl(movieFileService.save(image).toString());
            }
            movieService.saveMovie(movie);
            return "redirect:/movies";
        }
        return "admin/formAddMovie";
    }

    @GetMapping("/admin/artists/new")
    public String addNewArtist(Model model){
        model.addAttribute("artist", new Artist());
        return "admin/formAddArtist";
    }

    @PostMapping("/admin/artists/new")
    public String addNewArtist(
            @Valid @ModelAttribute("artist") Artist artist,
            BindingResult movieBinding,
            @RequestParam("image") MultipartFile image){
        if (artist != null && !movieBinding.hasErrors()){
            if (image != null && !image.isEmpty()){
                artist.setImageUrl(artistFileService.save(image).toString());
            }
            artistService.saveArtist(artist);
            return "redirect:/artists";
        }
        return "admin/formAddArtist";
    }

    @PostMapping("/admin/deleteReview/{id}")
    public String deleteReview(
            @PathVariable("id") Long id){
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null){
            return "movies/reviewNotFound";
        }
        Long movieId = review.getMovie().getId();
        reviewRepository.delete(review);
        return "redirect:/movies/" + movieId;
    }

    @GetMapping("/admin/movies/{id}/update")
    public String updateMovieInfo(
            @PathVariable("id") final Long id,
            Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie == null){
            return "movies/notFound";
        }
        model.addAttribute("movie", movie);
        return "/admin/manageMovie";
    }

    @PostMapping("/admin/movies/{id}/update")
    public String performMovieUpdate(
            @PathVariable("id") final Long id,
            @Valid @ModelAttribute("movie") Movie movie,
            BindingResult movieBinding,
            @RequestParam("image") MultipartFile image){
        Movie dbMovie = movieService.getMovie(id);
        if (dbMovie == null || !dbMovie.getId().equals(movie.getId())){
            return "movies/notFound";
        }
        if (movieBinding.hasErrors()){
            return "admin/manageMovie";
        }
        if (image != null && !image.isEmpty()){
            if (dbMovie.getImageUrl() != null){
                File oldFile = new File(dbMovie.getImageUrl());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }
            dbMovie.setImageUrl(movieFileService.save(image).toString());
        }
        dbMovie.setTitle(movie.getTitle());
        dbMovie.setSynopsis(movie.getSynopsis());
        dbMovie.setYear(movie.getYear());
        movieService.saveMovie(dbMovie);
        return "redirect:/movies/" + id;
    }

    @GetMapping("/admin/movies/{id}/updateDirector")
    public String updateMovieDirector(
            @PathVariable("id") final Long id,
            Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie == null){
            return "movies/notFound";
        }
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
        if (movie == null){
            return "movies/notFound";
        }
        if (director == null){
            return "artists/notFound";
        }
        movie.setDirector(director);
        this.movieRepository.save(movie);
        return "redirect:/admin/movies/" + movieId + "/updateDirector";
    }

    @PostMapping(value="/admin/movies/{movieId}/removeDirector")
    public String removeDirectorFromMovie(
            @PathVariable("movieId") final Long movieId) {
        Movie movie = movieService.getMovie(movieId);
        if (movie == null){
            return "movies/notFound";
        }
        movie.setDirector(null);
        this.movieRepository.save(movie);
        return "redirect:/admin/movies/" + movieId + "/updateDirector";
    }

    @GetMapping("/admin/movies/{id}/updateActors")
    public String updateMovieActors(
            @PathVariable("id") final Long id,
            Model model) {
        return "/admin/manageMovieActors";
    }

    @PostMapping(value="/admin/movies/{movieId}/addActor/{actorId}")
    public String addActorToMovie(
            @PathVariable("movieId") final Long movieId,
            @PathVariable("actorId") final Long actorId) {
        Movie movie = movieService.getFullMovie(movieId);
        Artist actor = artistService.getArtist(actorId);
        if (movie == null){
            return "movies/notFound";
        }
        if (actor == null){
            return "artists/notFound";
        }
        Set<Artist> actors = movie.getActors();
        actors.add(actor);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/" + movieId + "/update";
    }

    @PostMapping(value="/admin/movies/{movieId}/removeActor/{actorId}")
    public String removeActorFromMovie(
            @PathVariable("movieId") final Long movieId,
            @PathVariable("actorId") final Long actorId) {
        Movie movie = movieService.getFullMovie(movieId);
        Artist actor = artistService.getArtist(actorId);
        if (movie == null){
            return "movies/notFound";
        }
        if (actor == null) {
            return "artists/notFound";
        }
        Set<Artist> actors = movie.getActors();
        actors.remove(actor);
        this.movieService.saveMovie(movie);
        return "redirect:/admin/movies/" + movieId + "/updateActors";
    }
}
