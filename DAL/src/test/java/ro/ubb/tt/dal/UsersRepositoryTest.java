package ro.ubb.tt.dal;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ubb.tt.model.User;

@SpringBootTest(classes = DALTest.class)
@RunWith(SpringRunner.class)
public class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void testFindByEmail(){

        //valid email
        User user= usersRepository.findByEmail("luca@gmail.com");
        Assert.assertNotNull(user);
        Assert.assertEquals("Balogh",user.getLastName());


        //invalid email
        user= usersRepository.findByEmail("luca.com");
        Assert.assertNull(user);
    }
}
