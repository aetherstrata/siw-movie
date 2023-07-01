package dev.aest.siw.movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "user_id"}))
public final class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @NotNull
    @Min(value = 1, message = "{review.score.min}")
    @Max(value = 5, message = "{review.score.max}")
    private Integer score;

    @Column(nullable = false, length = 127)
    @NotBlank
    @Size(max = 127, message = "{review.title.size}")
    private String title;

    @Column(nullable = false, length = 4095)
    @NotBlank
    private String text;

    @Setter(value = AccessLevel.PRIVATE)
    private LocalDateTime createdAt;

    @Setter(value = AccessLevel.PRIVATE)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onPersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review other)) return false;

        if (!score.equals(other.score)) return false;
        if (!title.equals(other.title)) return false;
        return text.equals(other.text);
    }

    @Override
    public int hashCode() {
        int result = score.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }
}
