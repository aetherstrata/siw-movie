package dev.aest.siw.movie.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegistrationFormData
{
    @NotBlank(message = "{username.blank}")
    @Pattern(regexp = "^[A-Za-z0-9_.]+$", message = "{username.invalid}")
    private String username;

    @NotBlank(message = "{email.blank}")
    @Email(message = "{email.invalid}")
    private String email;

    @NotBlank(message = "{password.blank}")
    @Pattern(regexp = "^(?=(.*[A-Z])+)(?=(.*[a-z])+)(?=(.*[0-9])+)(?=(.*[!@#$%^&*()_+=.])+).{10,}$", message = "{password.weak}")
    private String password;

    private String name;
}
