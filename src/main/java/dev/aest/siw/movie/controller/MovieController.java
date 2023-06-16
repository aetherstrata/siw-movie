package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.service.MovieService;
import dev.aest.siw.movie.service.ReviewService;
import dev.aest.siw.movie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MovieController
{
    private final MovieService movieService;
    private final UserService userService;
    private final ReviewService reviewService;

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

    @PostMapping("/movies/search")
    public String searchMovies(
            @RequestParam("year") int year,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            Model model){
        Page<Movie> moviePage = movieService.getMovieByYearPage(year, page.orElse(0));
        model.addAttribute("total", moviePage.getTotalElements());
        model.addAttribute("hasPrev", moviePage.hasPrevious());
        model.addAttribute("hasNext", moviePage.hasNext());
        model.addAttribute("movies", moviePage.stream().toList());
        return "movies/searchResults";
    }

    @GetMapping("/movies/{id}")
    public String movieDetails(
            @PathVariable long id,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            Principal principal,
            Model model) {
        Movie movie = movieService.getMovie(id);
        if (movie == null){
            return "movies/notFound";
        }
        Page<Review> reviewPage = reviewService.getReviewPage(movie, page.orElse(0));
        model.addAttribute("movie", movie);
        model.addAttribute("reviewsCount", reviewPage.getTotalElements());
        model.addAttribute("reviews", reviewPage.stream().toList());
        model.addAttribute("hasNext", reviewPage.hasNext());
        model.addAttribute("hasPrev", reviewPage.hasPrevious());
        model.addAttribute("userReviewPresent", reviewService.getUserReview(userService.getCurrentUser(principal), movie));
        return "movies/movieDetails";
    }

}
