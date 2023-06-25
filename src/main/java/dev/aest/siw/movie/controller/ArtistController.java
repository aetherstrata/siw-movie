package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Artist;
import dev.aest.siw.movie.model.PageInfo;
import dev.aest.siw.movie.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ArtistController
{
    private final ArtistService artistService;

    @GetMapping("/artists")
    public String artistHomepage(
            Pageable pageable,
            Model model){
        Page<Artist> artistPage = artistService.getArtistsPage(pageable);
        model.addAttribute("artists", artistPage.stream().toList());
        model.addAttribute(PageInfo.ATTRIBUTE_NAME, new PageInfo<>(artistPage));
        return "artists/index";
    }

    @GetMapping("/artists/{id}")
    public String artistDetails(
            @PathVariable("id") final Long id,
            Model model
    ){
        Artist artist = artistService.getFullArtist(id);
        if (artist == null){
            return "artists/notFound";
        }
        model.addAttribute("artist", artist);
        return "artists/artistDetails";
    }
}
