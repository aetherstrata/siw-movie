package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.model.Credentials;
import dev.aest.siw.movie.model.User;
import dev.aest.siw.movie.service.CredentialsService;
import dev.aest.siw.movie.service.UserService;
import dev.aest.siw.movie.validation.CredentialsValidator;
import dev.aest.siw.movie.validation.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController
{
    private final CredentialsService credentialsService;
    private final CredentialsValidator credentialsValidator;
    private final UserService userService;
    private final UserValidator userValidator;

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "auth/formRegister";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") final User user,
            BindingResult userBinding,
            @Valid @ModelAttribute("credentials") final Credentials credentials,
            BindingResult credentialsBinding,
            Model model) {
        this.userValidator.validate(user, userBinding);
        this.credentialsValidator.validate(credentials, credentialsBinding);
        if(!userBinding.hasErrors() && !credentialsBinding.hasErrors()) {
            user.setNickname(credentials.getUsername());
            credentials.setUser(user);
            userService.saveUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "auth/successfulRegister";
        }
        return "auth/formRegister";
    }

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) final Boolean loginError,
            Model model){
        model.addAttribute("error", loginError);
        return "auth/formLogin";
    }

    @GetMapping("/success")
    public String authenticationSuccess(){
        return "redirect:/";
    }

    @GetMapping("/oauth2-success")
    public String oauth2AuthSuccess(){
        return "redirect:/";
    }
}
