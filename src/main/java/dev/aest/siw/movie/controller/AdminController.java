package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.entity.Review;
import dev.aest.siw.movie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminController
{
    private final ReviewService reviewService;

    @GetMapping("/admin")
    public String adminPage(){
        return "admin/index";
    }

    @PostMapping("/admin/deleteReview/{id}")
    public String deleteReview(
            @PathVariable("id") Long id){
        Review review = reviewService.getReview(id);
        if (review == null) return "movies/reviewNotFound";
        Long movieId = review.getMovie().getId();
        reviewService.deleteReview(review);
        return "redirect:/movies/%d".formatted(movieId);
    }
}
