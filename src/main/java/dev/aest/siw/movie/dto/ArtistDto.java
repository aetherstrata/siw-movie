package dev.aest.siw.movie.dto;

import dev.aest.siw.movie.model.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto
{
    private Long id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;

    protected ArtistDto(Artist artist) {
        id = artist.getId();
        name = artist.getName();
        surname = artist.getSurname();
        dateOfBirth = artist.getDateOfBirth();
        dateOfDeath = artist.getDateOfDeath();
    }

    public static ArtistDto of(Artist artist) {
        if (artist == null) return null;
        return new ArtistDto(artist);
    }
}
