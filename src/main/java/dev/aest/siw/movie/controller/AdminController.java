package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.service.MovieFileService;
import dev.aest.siw.movie.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController
{
    private final MovieFileService fileService;
    private final MovieService movieService;

    public AdminController(MovieFileService fileService, MovieService movieService) {
        this.fileService = fileService;
        this.movieService = movieService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model){
        return "admin/index";
    }


    @GetMapping("/admin/movies/new")
    public String addNewMovie(Model model){
        model.addAttribute("movie", new Movie());
        model.addAttribute("image", null);
        return "movies/formAddMovie";
    }

    @PostMapping("/admin/movies/new")
    public String addNewMovie(
            @Valid @ModelAttribute("movie") Movie movie,
            BindingResult movieBinding,
            @RequestParam("image") MultipartFile image){
        if (movie != null && !movieBinding.hasErrors()){
            if (image != null && !image.isEmpty()){
                movie.setImageUrl(fileService.save(image).toString());
            }
            movieService.saveMovie(movie);
            return "redirect:/movies";
        }
        return "movies/formAddMovie";
    }
}
