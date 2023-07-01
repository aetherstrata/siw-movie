package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.service.ArtistFileService;
import dev.aest.siw.movie.service.MovieFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class FileController
{
    private final MovieFileService movieFileService;
    private final ArtistFileService artistFileService;

    @GetMapping("/uploads/movies/images/{filename:.+}")
    public ResponseEntity<Resource> getMovieCover(@PathVariable("filename") String filename) {
        Resource file = movieFileService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(file.getFilename()))
                .body(file);
    }

    @GetMapping("/uploads/artists/images/{filename:.+}")
    public ResponseEntity<Resource> getArtistCover(@PathVariable("filename") String filename) {
        Resource file = artistFileService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(file.getFilename()))
                .body(file);
    }
}
