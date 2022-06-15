package ro.ubb.tt.bll.validator;

import org.springframework.stereotype.Component;
import ro.ubb.tt.bll.exceptions.ExceptionMessages;
import ro.ubb.tt.bll.exceptions.InvalidCredentialsException;
import ro.ubb.tt.model.Location;
import ro.ubb.tt.model.enums.Type;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValidatorLocation implements Validator<Location> {

    @Override
    public void validate(Location loc) throws InvalidCredentialsException {

        List<String> errors = new ArrayList<>();
        System.out.println(loc.getCity() + " " + loc.getCountry());
        if(loc.getCity() == null || loc.getCity().trim().equals(""))
            errors.add(ExceptionMessages.invalidCity);

        if(loc.getCountry() == null || loc.getCountry().trim().equals(""))
            errors.add(ExceptionMessages.invalidCountry);

        if(loc.getRating() < 0f)
            errors.add(ExceptionMessages.invalidRating);

        if(loc.getTip() != Type.City && loc.getTip() != Type.Mountain && loc.getTip() != Type.Sea)
            errors.add(ExceptionMessages.invalidType);

        String errorMessage = errors
                .stream()
                .reduce("", String::concat);

        if(!errorMessage.isEmpty()) {
            throw new InvalidCredentialsException(errorMessage);
        }

    }
}