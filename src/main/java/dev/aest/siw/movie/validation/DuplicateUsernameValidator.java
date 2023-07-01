package dev.aest.siw.movie.validation;

import dev.aest.siw.movie.model.RegistrationFormData;
import dev.aest.siw.movie.repository.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class DuplicateUsernameValidator implements Validator
{
    private final CredentialsRepository repository;

    @Override
    public boolean supports(Class<?> type) {
        return RegistrationFormData.class.equals(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var formData = (RegistrationFormData)target;

        if (repository.findByUsername(formData.getUsername()).isPresent()){
            errors.reject("username.duplicate");
        }
    }
}
