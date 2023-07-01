package dev.aest.siw.movie.model;

import dev.aest.siw.movie.entity.Review;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewFormData
{
    @NotBlank(message = "{review.title.blank}")
    @Size(max = 127, message = "{review.title.size}")
    private String title;

    @NotNull
    @Min(value = 1, message = "{review.score.min}")
    @Max(value = 5, message = "{review.score.max}")
    private Integer score;

    @NotBlank(message = "{review.text.blank}")
    @Size(max = 4095, message = "{review.text.size}")
    private String text;

    public Review toReview(){
        Review review = new Review();
        review.setTitle(title);
        review.setScore(score);
        review.setText(text);
        return review;
    }
}
