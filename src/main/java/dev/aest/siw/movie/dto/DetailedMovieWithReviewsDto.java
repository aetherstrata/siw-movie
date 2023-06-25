package dev.aest.siw.movie.dto;

import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DetailedMovieWithReviewsDto extends DetailedMovieDto
{
    private List<ReviewDto> reviews;

    private DetailedMovieWithReviewsDto(Movie movie, Collection<Review> reviews){
        super(movie);
        this.reviews = reviews.stream().map(ReviewDto::of).toList();
    }

    public static DetailedMovieWithReviewsDto of(Movie movie, Collection<Review> reviews){
        if (movie == null) return null;
        return new DetailedMovieWithReviewsDto(movie, reviews);
    }
}
