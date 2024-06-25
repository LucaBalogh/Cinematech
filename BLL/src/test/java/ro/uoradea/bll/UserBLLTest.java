package ro.uoradea.bll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.uoradea.bll.exceptions.ExceptionMessages;
import ro.uoradea.bll.exceptions.InternalServerException;
import ro.uoradea.bll.exceptions.InvalidCredentialsException;
import ro.uoradea.bll.validator.ValidatorUser;
import ro.uoradea.dal.UsersRepository;
import ro.uoradea.model.User;

@SpringBootTest(classes = UserBLL.class)
@RunWith(SpringRunner.class)
public class UserBLLTest {
    @MockBean
    ValidatorUser validatorUser;
    @MockBean
    private UsersRepository usersRepository;
    @InjectMocks
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
        } catch (Exception e) {
            this.foundException = true;
        }

        Assert.assertFalse(foundException);
    }

}
