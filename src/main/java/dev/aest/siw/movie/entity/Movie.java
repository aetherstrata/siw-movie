package dev.aest.siw.movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "movies", uniqueConstraints = @UniqueConstraint(columnNames = {"title", "year"}))
public final class Movie
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false)
    @NotNull
    @Min(1900)
    @Max(2023)
    private Integer year;

    @Column(length = 1023)
    private String synopsis;

    @ElementCollection
    @CollectionTable(name = "movie_images", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "image")
    private List<String> imageUrls = new ArrayList<>();

    @Formula("(SELECT i.image FROM movie_images i WHERE i.movie_id=id ORDER BY i.image LIMIT 1)")
    private String firstImage;

    @Formula("(SELECT COUNT(*) FROM movie_images i WHERE i.movie_id=id)")
    private Integer imageCount;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Artist director;

    @ManyToMany
    @JoinTable(
            name = "movie_actors",
            joinColumns = { @JoinColumn(name = "movie_id") },
            inverseJoinColumns = { @JoinColumn(name = "actor_id") }
    )
    private Set<Artist> actors;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPersist(){
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie other)) return false;

        if (!title.equals(other.title)) return false;
        return year.equals(other.year);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + year.hashCode();
        return result;
    }
}
