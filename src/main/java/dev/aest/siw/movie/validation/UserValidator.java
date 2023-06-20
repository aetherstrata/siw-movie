package dev.aest.siw.movie.validation;

import dev.aest.siw.movie.model.User;
import dev.aest.siw.movie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator
{
    private final UserRepository repository;

    @Override
    public boolean supports(Class<?> type) {
        return User.class.equals(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var user = (User)target;

        if (repository.findByEmail(user.getEmail()).isPresent()){
            errors.reject("email.duplicate", "This email has already been taken.");
        }
    }
}