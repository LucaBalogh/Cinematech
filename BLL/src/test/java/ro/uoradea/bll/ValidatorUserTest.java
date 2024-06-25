package ro.uoradea.bll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.uoradea.bll.exceptions.InvalidCredentialsException;
import ro.uoradea.bll.validator.Validator;
import ro.uoradea.model.User;

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


        user.setEmail(null);
        user.setPassword("notanemptypassword");

        exception = Assert.assertThrows(InvalidCredentialsException.class,
                ()->{ validator.validate(user);} );
        Assert.assertEquals(exception.getMessage(), "The email is not valid!");

        user.setEmail("test");
        user.setPassword("");

        exception = Assert.assertThrows(InvalidCredentialsException.class,
                ()->{ validator.validate(user);} );
        Assert.assertEquals(exception.getMessage(), "The password cannot be empty!");
    }
}