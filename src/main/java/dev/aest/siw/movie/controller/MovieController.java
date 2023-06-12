package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.service.MovieService;
import dev.aest.siw.movie.service.ReviewService;
import dev.aest.siw.movie.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MovieController
{
    private final MovieService movieService;
    private final UserService userService;
    private final ReviewService reviewService;

    public MovieController(MovieService movieService,
                           UserService userService,
                           ReviewService reviewService) {
        this.movieService = movieService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/movies")
    public String movieHome(
            @RequestParam(required = false) Optional<Integer> page,
            Model model) {
        Page<Movie> moviePage = movieService.getMoviePage(page.orElse(0));
        model.addAttribute("total", moviePage.getTotalElements());
        model.addAttribute("hasPrev", moviePage.hasPrevious());
        model.addAttribute("hasNext", moviePage.hasNext());
        model.addAttribute("movies", moviePage.stream().toList());
        return "movies/index";
    }

    @GetMapping("/movies/{id}")
    public String movieDetails(
            @PathVariable long id,
            @RequestParam(required = false) Optional<Integer> page,
            Principal principal,
            Model model) {
        Movie movie = movieService.getMovie(id);
        Page<Review> reviewsPage = reviewService.getReviewPage(movie, page.orElse(0));
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviewsPage.stream().filter(review ->
                !review.getUser().equals(userService.getCurrentUser(principal))));
        return "movies/movieDetails";
    }
}
