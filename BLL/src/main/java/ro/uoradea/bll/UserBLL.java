package ro.uoradea.bll;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.uoradea.bll.exceptions.ExceptionMessages;
import ro.uoradea.bll.exceptions.InvalidCredentialsException;
import ro.uoradea.bll.validator.ValidatorUser;
import ro.uoradea.dal.UsersRepository;
import ro.uoradea.model.User;

@Component
public class UserBLL {
    private UsersRepository usersRepository;
    private ValidatorUser validatorUser;

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setValidatorUser(ValidatorUser validatorUser) {
        this.validatorUser = validatorUser;
    }


    public User login(User user) throws InvalidCredentialsException {

        validatorUser.validate(user);
        User userFound = usersRepository.findByEmail(user.getEmail());
        if(userFound == null)
            throw new InvalidCredentialsException(ExceptionMessages.nonExistentUser);
        if(!userFound.getPassword().equals(user.getPassword()))
            throw new InvalidCredentialsException(ExceptionMessages.incorrectPassword);

        return userFound;
    }

    public User getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public List<User> getAllUsers(){return usersRepository.findAll();}
}
