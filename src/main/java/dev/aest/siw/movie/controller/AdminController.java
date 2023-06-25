package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Review;
import dev.aest.siw.movie.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminController
{
    private final ReviewRepository reviewRepository;

    @GetMapping("/admin")
    public String adminPage(){
        return "admin/index";
    }

    @PostMapping("/admin/deleteReview/{id}")
    public String deleteReview(
            @PathVariable("id") Long id){
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return "movies/reviewNotFound";
        Long movieId = review.getMovie().getId();
        reviewRepository.delete(review);
        return "redirect:/movies/%d".formatted(movieId);
    }
}
