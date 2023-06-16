package dev.aest.siw.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArtistController
{
    @GetMapping("/artists")
    public String artistHomepage(Model model){
        return "artists/index";
    }
}
