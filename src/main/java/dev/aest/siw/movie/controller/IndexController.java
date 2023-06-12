package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Credentials;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController
{
    @GetMapping("/")
    public String indexPage(Model model){
        return "index";
    }
}
