package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.service.ArtistFileService;
import dev.aest.siw.movie.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequiredArgsConstructor
public class ArtistAdminController
{
    private final ArtistFileService artistFileService;
    private final ArtistService artistService;

    @GetMapping("/admin/artists/new")
    public String addNewArtist(Model model){
        model.addAttribute("artist", new Artist());
        return "admin/formAddArtist";
    }

    @PostMapping("/admin/artists/new")
    public String addNewArtist(
            @Valid @ModelAttribute("artist") Artist artist,
            BindingResult movieBinding,
            @RequestParam("image") MultipartFile image){
        if (artist != null && !movieBinding.hasErrors()){
            if (image != null && !image.isEmpty()) artist.setImageUrl(artistFileService.save(image));
            artistService.saveArtist(artist);
            return "redirect:/artists";
        }
        return "admin/formAddArtist";
    }

    @GetMapping("/admin/artists/{id}/update")
    public String updateArtistInfo(
            @PathVariable("id") final Long id,
            Model model){
        Artist artist = artistService.getArtist(id);
        if (artist == null) return "artists/notFound";
        model.addAttribute("artist", artist);
        return "admin/updateArtist";
    }

    @PostMapping("/admin/artists/{id}/update")
    public String performArtistUpdate(
            @PathVariable("id") final Long id,
            @Valid @ModelAttribute("artist") Artist artist,
            BindingResult artistBinding,
            @RequestParam("image") MultipartFile image){
        Artist dbArtist = artistService.getArtist(id);
        if (dbArtist == null || !dbArtist.getId().equals(artist.getId())) return "artists/notFound";
        if (artistBinding.hasErrors()) return "admin/updateArtist";
        if (image != null && !image.isEmpty()){
            if (dbArtist.getImageUrl() != null){
                File oldFile = new File(dbArtist.getImageUrl());
                if (oldFile.exists()) oldFile.delete();
            }
            dbArtist.setImageUrl(artistFileService.save(image));
        }
        dbArtist.setName(artist.getName());
        dbArtist.setSurname(artist.getSurname());
        dbArtist.setDateOfBirth(artist.getDateOfBirth());
        dbArtist.setDateOfDeath(artist.getDateOfDeath());
        artistService.saveArtist(dbArtist);
        return "redirect:/artists/%d".formatted(id);
    }

    @PostMapping("/admin/artists/{id}/delete")
    public String deleteMovie(
            @PathVariable("id") final Long id){
        Artist artist = artistService.getArtist(id);
        if (artist == null) return "artists/notFound";
        artistService.deleteArtist(artist);
        return "redirect:/artists";
    }
}
