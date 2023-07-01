package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.entity.Artist;
import dev.aest.siw.movie.model.ArtistFormData;
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
        model.addAttribute("form", new ArtistFormData());
        return "admin/formAddArtist";
    }

    @PostMapping("/admin/artists/new")
    public String addNewArtist(
            @Valid @ModelAttribute("form") ArtistFormData formData,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile image){
        if (bindingResult.hasErrors()) return "admin/formAddArtist";
        Artist artist = formData.toArtist();
        if (image != null && !image.isEmpty()) artist.setImageUrl(artistFileService.save(image));
        artistService.saveArtist(artist);
        return "redirect:/artists";
    }

    @GetMapping("/admin/artists/{id}/update")
    public String updateArtistInfo(
            @PathVariable("id") final Long id,
            Model model){
        Artist artist = artistService.getArtist(id);
        if (artist == null) return ArtistController.NOT_FOUND;
        model.addAttribute("artist_id", artist.getId());
        model.addAttribute("form", ArtistFormData.of(artist));
        return "admin/updateArtist";
    }

    @PostMapping("/admin/artists/{id}/update")
    public String performArtistUpdate(
            @PathVariable("id") final Long id,
            @Valid @ModelAttribute("form") ArtistFormData formData,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile image){
        Artist artist = artistService.getArtist(id);
        if (artist == null) return ArtistController.NOT_FOUND;
        if (bindingResult.hasErrors()) return "admin/updateArtist";
        if (image != null && !image.isEmpty()){
            if (artist.getImageUrl() != null){
                File oldFile = new File("." + artist.getImageUrl());
                if (oldFile.exists()) oldFile.delete();
            }
            artist.setImageUrl(artistFileService.save(image));
        }
        artist.setName(formData.getName());
        artist.setSurname(formData.getSurname());
        artist.setDateOfBirth(formData.getDateOfBirth());
        artist.setDateOfDeath(formData.getDateOfDeath());
        artistService.saveArtist(artist);
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
