package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Credentials;
import dev.aest.siw.movie.model.User;
import dev.aest.siw.movie.service.CredentialsService;
import dev.aest.siw.movie.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class AuthController
{
    private static final String authorizationRequestBaseUri = "oauth2/authorization/";
    private static final Map<String, String> oauth2AuthenticationUrls = new HashMap<>(){{
        put("GitHub", authorizationRequestBaseUri + "github");
    }};

    private final CredentialsService credentialsService;
    private final UserService userService;

    public AuthController(CredentialsService credentialsService, UserService userService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "auth/formRegister";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult userBindingResult,
                               @Valid @ModelAttribute("credentials") Credentials credentials,
                               BindingResult credentialsBindingResult,
                               Model model) {
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "auth/successfulRegister";
        }
        return "auth/formRegister";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("urls", oauth2AuthenticationUrls);
        return "auth/formLogin";
    }

    @GetMapping("/success")
    public String authenticationSuccess(Model model){
        return credentialsService.isAdminUser() ? "redirect:admin/index" : "redirect:index";
    }
}
