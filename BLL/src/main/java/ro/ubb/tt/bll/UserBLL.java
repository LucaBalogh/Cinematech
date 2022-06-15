package ro.ubb.tt.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubb.tt.bll.exceptions.ExceptionMessages;
import ro.ubb.tt.bll.exceptions.InvalidCredentialsException;
import ro.ubb.tt.bll.validator.ValidatorUser;
import ro.ubb.tt.model.User;
import ro.ubb.tt.dal.UsersRepository;

import java.util.List;

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
