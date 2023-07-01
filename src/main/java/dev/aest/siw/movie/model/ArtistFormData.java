package dev.aest.siw.movie.model;

import dev.aest.siw.movie.entity.Artist;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ArtistFormData
{
    @NotBlank(message = "{artist.name.blank}")
    private String name;

    @NotBlank(message = "{artist.surname.blank}")
    private String surname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfDeath;

    private ArtistFormData(Artist artist){
        this.name = artist.getName();
        this.surname = artist.getSurname();
        this.dateOfBirth = artist.getDateOfBirth();
        this.dateOfDeath = artist.getDateOfDeath();
    }

    public static ArtistFormData of(Artist artist){
        if (artist == null) return null;
        return new ArtistFormData(artist);
    }

    public Artist toArtist(){
        Artist artist = new Artist();
        artist.setName(name);
        artist.setSurname(surname);
        artist.setDateOfBirth(dateOfBirth);
        artist.setDateOfDeath(dateOfDeath);
        return artist;
    }
}
