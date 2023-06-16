package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.repository.ArtistRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService
{
    private final ArtistRepository artistRepository;

    @Transactional
    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }
}
