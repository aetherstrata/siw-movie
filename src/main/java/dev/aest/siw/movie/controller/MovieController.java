package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.PageInfo;
import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.model.User;
import dev.aest.siw.movie.service.ArtistService;
import dev.aest.siw.movie.service.MovieService;
import dev.aest.siw.movie.service.ReviewService;
import dev.aest.siw.movie.service.UserService;
import dev.aest.siw.movie.validation.ReviewValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MovieController
{
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final ReviewValidator reviewValidator;
    private final UserService userService;

    @GetMapping("/movies")
    public String movieHome(
            Pageable pageable,
            Model model) {
        Page<Movie> moviePage = movieService.getMoviesPage(pageable);
        model.addAttribute("movies", moviePage.stream().toList());
        model.addAttribute(PageInfo.ATTRIBUTE_NAME, new PageInfo<>(moviePage));
        return "movies/index";
    }

    @PostMapping("/search/movies")
    public String searchMovies(
            @RequestParam("year") int year,
            Pageable pageable,
            Model model){
        Page<Movie> moviePage = movieService.getMoviesByYearPage(year, pageable);
        model.addAttribute("movies", moviePage.stream().toList());
        model.addAttribute(PageInfo.ATTRIBUTE_NAME, new PageInfo<>(moviePage));
        return "movies/searchResults";
    }

    @GetMapping("/movies/{id}")
    public String movieDetails(
            @PathVariable long id,
            Pageable pageable,
            Principal principal,
            Model model) {
        Movie movie = movieService.getDetailedMovie(id);
        if (movie == null){
            return "movies/notFound";
        }
        Page<Review> reviewPage = reviewService.getReviewPage(movie, pageable);
        Review currentUserReview = reviewService.getUserReview(userService.getCurrentUser(), movie);
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviewPage.stream().toList());
        model.addAttribute("currentUserReview", currentUserReview);
        model.addAttribute(PageInfo.ATTRIBUTE_NAME, new PageInfo<>(reviewPage));
        return "movies/movieDetails";
    }

    @GetMapping("/movies/{id}/addReview")
    public String addReview(
            @PathVariable final Long id,
            Principal principal,
            Model model){
        Movie movie = movieService.getMovie(id);
        if (movie == null){
            return "movies/notFound";
        }
        Review review = new Review();
        model.addAttribute("review", review);
        return "movies/formAddReview";
    }

    @PostMapping("/movies/{id}/addReview")
    public String addReview(
            @PathVariable final Long id,
            @Valid @ModelAttribute("review") Review review,
            BindingResult reviewBinding,
            Principal principal,
            Model model){
        Movie movie = movieService.getMovie(id);
        User user = userService.getCurrentUser();
        this.reviewValidator.validate(review, reviewBinding);
        if (movie == null || user == null || reviewBinding.hasErrors()) {
            return "movies/formAddReview";
        }
        review.setMovie(movie);
        review.setUser(user);
        reviewService.saveReview(review);
        return "redirect:/movies/" + id;
    }
}
