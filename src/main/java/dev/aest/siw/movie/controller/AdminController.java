package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.service.ArtistFileService;
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

@Controller
@RequiredArgsConstructor
public class AdminController
{
    private final MovieService movieService;
    private final MovieFileService movieFileService;
    private final MovieValidator movieValidator;

    private final ArtistFileService artistFileService;
    private final ArtistService artistService;

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
}
