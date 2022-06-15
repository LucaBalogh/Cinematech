package ro.ubb.tt.bll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ubb.tt.bll.exceptions.ExceptionMessages;
import ro.ubb.tt.bll.exceptions.InternalServerException;
import ro.ubb.tt.bll.exceptions.InvalidCredentialsException;
import ro.ubb.tt.model.User;

@SpringBootTest(classes = UserBLL.class)
@RunWith(SpringRunner.class)
public class UserBLLTest {

    @Autowired
    UserBLL userBLL;

    User user;
    boolean foundException;

    @Before
    public void initData(){
        foundException = false;
    }

    @Test
    public void login() {

        //invalid user
        Throwable exception = Assert.assertThrows(InvalidCredentialsException.class, ()->{
            userBLL.login(user); } );
        Assert.assertEquals(exception.getMessage(), ExceptionMessages.invalidEmail + ExceptionMessages.invalidPassword);


        //invalid password

        user.setEmail("luca@gmail.com");
        user.setPassword("notmypass");
        exception = Assert.assertThrows(InternalServerException.class, ()->{
            userBLL.login(user); } );
        Assert.assertEquals(exception.getMessage(), ExceptionMessages.incorrectPassword);

        //correct
        user.setPassword("luca");
        try{
            userBLL.login(user);
        } catch (InvalidCredentialsException e) {
            this.foundException = true;
        }

        Assert.assertFalse(foundException);

    }

}
