package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.entity.Review;
import dev.aest.siw.movie.model.PageInfo;
import dev.aest.siw.movie.model.ReviewFormData;
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

@Controller
@RequiredArgsConstructor
public class MovieController
{
    public static final String NOT_FOUND = "movies/notFound";

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final ReviewValidator reviewValidator;
    private final UserService userService;

    @GetMapping("/movies")
    public String getMovieHome(
            Pageable pageable,
            Model model) {
        Page<Movie> moviePage = movieService.getMoviesPage(pageable);
        model.addAttribute("movies", moviePage.stream().toList());
        model.addAttribute(PageInfo.ATTRIBUTE_NAME, new PageInfo(moviePage));
        return "movies/index";
    }

    @GetMapping("/movies/{id}")
    public String getMovieDetailsPage(
            @PathVariable long id,
            Pageable pageable,
            Model model) {
        Movie movie = movieService.getDetailedMovie(id);
        if (movie == null) return NOT_FOUND;
        Page<Review> reviewPage = reviewService.getReviewPage(movie, pageable);
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviewPage.stream().toList());
        model.addAttribute("has_review", reviewService.userHasReview(movie, userService.getCurrentUser()));
        model.addAttribute(PageInfo.ATTRIBUTE_NAME, new PageInfo(reviewPage));
        return "movies/movieDetails";
    }

    @GetMapping("/movies/{id}/images")
    public String getMovieImagesPage(
            @PathVariable("id") final Long id,
            Model model){
        Movie movie = movieService.getMovieWithImages(id);
        if (movie == null) return NOT_FOUND;
        model.addAttribute("movie", movie);
        return "movies/movieImages";
    }

    @GetMapping("/movies/{id}/addReview")
    public String getAddReviewPage(
            @PathVariable final Long id,
            Model model){
        Movie movie = movieService.getMovie(id);
        if (movie == null) return NOT_FOUND;
        model.addAttribute("form", new ReviewFormData());
        return "movies/formAddReview";
    }

    @PostMapping("/movies/{id}/addReview")
    public String addReview(
            @PathVariable final Long id,
            @Valid @ModelAttribute("form") ReviewFormData formData,
            BindingResult bindingResult){
        Movie movie = movieService.getMovie(id);
        if (movie == null) return NOT_FOUND;
        Review review = formData.toReview();
        review.setMovie(movie);
        review.setUser(userService.getCurrentUser());
        this.reviewValidator.validate(review, bindingResult);
        if (bindingResult.hasErrors()) return "movies/formAddReview";
        reviewService.saveReview(review);
        return "redirect:/movies/" + id;
    }
}
