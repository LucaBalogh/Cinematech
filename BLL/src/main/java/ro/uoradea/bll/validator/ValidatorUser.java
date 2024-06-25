package ro.uoradea.bll.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import ro.uoradea.bll.exceptions.ExceptionMessages;
import ro.uoradea.bll.exceptions.InvalidCredentialsException;
import ro.uoradea.model.User;

@Component
public class ValidatorUser implements Validator<User> {

    @Override
    public void validate(User user) throws InvalidCredentialsException {
        List<String> errors = new ArrayList<>();
        
        if(user.getEmail() == null || user.getEmail().trim().isEmpty())
            errors.add(ExceptionMessages.invalidEmail);

        if(user.getPassword() == null || user.getPassword().trim().isEmpty())
            errors.add(ExceptionMessages.invalidPassword);

        String errorMessage = errors
                .stream()
                .reduce("", String::concat);

        if(!errorMessage.isEmpty()) {
            throw new InvalidCredentialsException(errorMessage);
        }

    }
}