package ro.ubb.tt.bll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ubb.tt.bll.exceptions.InvalidCredentialsException;
import ro.ubb.tt.bll.validator.Validator;
import ro.ubb.tt.model.User;

@SpringBootTest(classes = BLLTest.class)
@RunWith(SpringRunner.class)
public class ValidatorUserTest {

    @Autowired
    Validator<User> validator;

    User user;

    @Before
    public void initData(){
        user = new User();
    }

    @Test
    public void testValidator(){

        Throwable exception;
        exception = Assert.assertThrows(InvalidCredentialsException.class,
                ()->{ validator.validate(user);} );
        Assert.assertEquals(exception.getMessage(), "The email is not valid!The password cannot be empty!");


        user.setEmail("test");
        user.setPassword("notanemptypassword");
        exception = Assert.assertThrows(InvalidCredentialsException.class,
                ()->{ validator.validate(user);} );
        Assert.assertEquals(exception.getMessage(), "The email is not valid!");

        user.setPassword("");
        exception = Assert.assertThrows(InvalidCredentialsException.class,
                ()->{ validator.validate(user);} );
        Assert.assertEquals(exception.getMessage(), "Parola este invalida!");
    }
}