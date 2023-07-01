package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.entity.Credentials;
import dev.aest.siw.movie.model.RegistrationFormData;
import dev.aest.siw.movie.service.CredentialsService;
import dev.aest.siw.movie.validation.DuplicateUsernameValidator;
import dev.aest.siw.movie.validation.DuplicateEmailValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController
{
    private final CredentialsService credentialsService;

    private final DuplicateUsernameValidator duplicateUsernameValidator;
    private final DuplicateEmailValidator duplicateEmailValidator;

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("form", new RegistrationFormData());
        return "auth/formRegister";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("form") final RegistrationFormData formData,
            BindingResult bindingResult,
            Model model) {
        this.duplicateEmailValidator.validate(formData, bindingResult);
        this.duplicateUsernameValidator.validate(formData, bindingResult);
        if (bindingResult.hasErrors()) return "auth/formRegister";
        Credentials credentials = credentialsService.registerNewUser(formData);
        model.addAttribute("credentials", credentials);
        return "auth/successfulRegister";
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
