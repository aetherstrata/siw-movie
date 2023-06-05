package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.service.MovieService;
import org.springframework.stereotype.Controller;

@Controller
public class MovieController
{
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
}
