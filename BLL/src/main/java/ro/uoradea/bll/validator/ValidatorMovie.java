package ro.uoradea.bll.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import ro.uoradea.bll.exceptions.ExceptionMessages;
import ro.uoradea.bll.exceptions.InvalidCredentialsException;
import ro.uoradea.model.Movie;
import ro.uoradea.model.enums.Type;

@Component
public class ValidatorMovie implements Validator<Movie> {

    @Override
    public void validate(Movie loc) throws InvalidCredentialsException {
        List<String> errors = new ArrayList<>();

        if(loc.getName() == null || loc.getName().trim().isEmpty())
            errors.add(ExceptionMessages.invalidName);

        if(loc.getRating() < 0f)
            errors.add(ExceptionMessages.invalidRating);

    if (!List.of(Type.Adventure, Type.Action, Type.Drama, Type.Comedy).contains(loc.getTip()))
        errors.add(ExceptionMessages.invalidType);

    String errorMessage = errors
                .stream()
                .reduce("", String::concat);

        if(!errorMessage.isEmpty()) {
            throw new InvalidCredentialsException(errorMessage);
        }

    }
}