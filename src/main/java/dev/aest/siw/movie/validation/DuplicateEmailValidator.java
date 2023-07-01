package dev.aest.siw.movie.validation;

import dev.aest.siw.movie.model.RegistrationFormData;
import dev.aest.siw.movie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class DuplicateEmailValidator implements Validator
{
    private final UserRepository repository;

    @Override
    public boolean supports(Class<?> type) {
        return RegistrationFormData.class.equals(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var formData = (RegistrationFormData)target;

        if (repository.findByEmail(formData.getEmail()).isPresent()){
            errors.reject("email.duplicate");
        }
    }
}