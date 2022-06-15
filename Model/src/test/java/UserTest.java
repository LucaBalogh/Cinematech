import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.tt.model.Location;
import ro.ubb.tt.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void getSetLastName() {
        user.setLastName("lastname");
        assertEquals(user.getLastName(),"lastname");
        user.setLastName("");
        assertEquals(user.getLastName(),"");
    }

    @Test
    void getSetFirstName() {
        user.setFirstName("firstname");
        assertEquals(user.getFirstName(),"firstname");
        user.setFirstName("");
        assertEquals(user.getFirstName(),"");
    }

    @Test
    void getSetEmail() {
        user.setEmail("email");
        assertEquals(user.getEmail(),"email");
        user.setEmail("");
        assertEquals(user.getEmail(),"");
    }

    @Test
    void getSetPassword() {
        user.setPassword("pass");
        assertEquals(user.getPassword(),"pass");
        user.setPassword("");
        assertEquals(user.getPassword(),"");
    }
}