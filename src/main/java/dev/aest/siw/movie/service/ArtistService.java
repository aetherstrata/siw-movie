package dev.aest.siw.movie.service;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.Movie;
import dev.aest.siw.movie.repository.ArtistRepository;

import io.micrometer.common.KeyValues;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService
{
    private static final int MAX_PAGE_SIZE = 50;

    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public List<Artist> getAll() {
        return this.artistRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Artist> getAllActors(){
        return artistRepository.findByStarredMoviesNotEmpty();
    }

    @Transactional(readOnly = true)
    public List<Artist> getAllDirectors(){
        return artistRepository.findByDirectedMoviesNotEmpty();
    }

    @Transactional(readOnly = true)
    public Page<Artist> getArtistsPage(Pageable pageable){
        return artistRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Artist> getArtistsPage(int page, int size){
        return getArtistsPage(PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE)));
    }

    @Transactional(readOnly = true)
    public Page<Artist> getArtistsPage(int page){
        return getArtistsPage(page, MAX_PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    public List<Artist> getAllActorsByMovie(Movie movie) {
        return this.artistRepository.findByStarredMoviesContains(movie);
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
        return this.artistRepository.findFullById(id).orElse(null);
    }

    @Transactional
    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }

    @Transactional
    public void deleteArtist(Artist artist){
        artistRepository.delete(artist);
    }
}
