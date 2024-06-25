package ro.uoradea.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.uoradea.bll.UserBLL;
import ro.uoradea.bll.exceptions.InvalidCredentialsException;
import ro.uoradea.model.User;

@RestController
@CrossOrigin
@RequestMapping(UserController.BASE_URL)
@Slf4j
public class UserController {

    protected static final String BASE_URL = "/cinematech";

    private final UserBLL userBLL;

    public UserController(UserBLL userBLL) {
        this.userBLL = userBLL;
    }

    /**
     * Executes the login operation for an user.
     * @param user - User
     * @return ResponseEntity<UserDTO> - if the entered credentials are correct
     * @throws InvalidCredentialsException - if the entered credentials are incorrect
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<User> login(@RequestBody User user) throws InvalidCredentialsException {
        log.info("Log in the user " + user);
        User userFound;
        try {
            userFound = userBLL.login(user);
        } catch (InvalidCredentialsException ex) {
            throw new InvalidCredentialsException(ex.getMessage());
        }
        return new ResponseEntity<>(userFound, HttpStatus.OK);
    }

    /**
     * Retrieves logged user.
     * @param email - String
     * @return ResponseEntity<User>
     */
    @RequestMapping(value = "/get-user/{email}", method = RequestMethod.GET)
    public ResponseEntity<User> getLoggedUser(@PathVariable String email) {
        log.info("Get the user with email  " + email);
        return new ResponseEntity<>(userBLL.getUserByEmail(email), HttpStatus.OK);
    }
}
