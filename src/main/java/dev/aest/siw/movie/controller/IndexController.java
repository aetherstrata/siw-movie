package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.service.CredentialsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController
{
    private final CredentialsService credentialsService;

    public IndexController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @GetMapping("/")
    public String indexPage(Model model){
        return credentialsService.isAdminUser() ? "admin/index" : "index";
    }
}
