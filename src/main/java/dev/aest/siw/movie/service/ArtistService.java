package dev.aest.siw.movie.service;

import dev.aest.siw.movie.entity.Artist;
import dev.aest.siw.movie.entity.Movie;
import dev.aest.siw.movie.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService
{
    private final ArtistRepository artistRepository;
    private final ArtistFileService artistFileService;

    @Transactional(readOnly = true)
    public List<Artist> getAll() {
        return this.artistRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Artist> getArtistsPage(Pageable pageable){
        return artistRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Artist> getAvailableActorsFor(Movie movie){
        return this.artistRepository.findByStarredMoviesNotContains(movie);
    }

    @Transactional(readOnly = true)
    public Artist getArtist(Long id) {
        return this.artistRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Artist getFullArtist(Long id) {
        Artist artist = this.artistRepository.findById(id).orElse(null);
        if (artist == null) return null;
        Hibernate.initialize(artist.getStarredMovies());
        Hibernate.initialize(artist.getDirectedMovies());
        return artist;
    }

    @Transactional
    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }

    @Transactional
    public void deleteArtist(Artist artist){
        artistFileService.delete(artist.getImageUrl());
        artistRepository.delete(artist);
    }
}
