package ro.ubb.tt.bll.validator;

import org.springframework.stereotype.Component;
import ro.ubb.tt.bll.exceptions.ExceptionMessages;
import ro.ubb.tt.bll.exceptions.InvalidCredentialsException;
import ro.ubb.tt.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValidatorUser implements Validator<User> {

    @Override
    public void validate(User user) throws InvalidCredentialsException {

        List<String> errors = new ArrayList<>();
        System.out.println(user.getEmail() + " " + user.getPassword());
        if(user.getEmail() == null || user.getEmail().trim().equals(""))
            errors.add(ExceptionMessages.invalidEmail);

        if(user.getPassword() == null || user.getPassword().trim().equals(""))
            errors.add(ExceptionMessages.invalidPassword);

        String errorMessage = errors
                .stream()
                .reduce("", String::concat);

        if(!errorMessage.isEmpty()) {
            throw new InvalidCredentialsException(errorMessage);
        }

    }
}