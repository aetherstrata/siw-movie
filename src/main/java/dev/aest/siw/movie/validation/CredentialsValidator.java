package dev.aest.siw.movie.validation;

import dev.aest.siw.movie.model.Credentials;
import dev.aest.siw.movie.repository.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CredentialsValidator implements Validator
{
    private final CredentialsRepository repository;

    @Override
    public boolean supports(Class<?> type) {
        return Credentials.class.equals(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var credentials = (Credentials)target;

        if (repository.findByUsername(credentials.getUsername()).isPresent()){
            errors.reject("username.duplicate", "This username has already been taken.");
        }
    }
}
