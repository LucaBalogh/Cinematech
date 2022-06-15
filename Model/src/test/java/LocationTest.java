import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.tt.model.Location;
import ro.ubb.tt.model.User;
import ro.ubb.tt.model.enums.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This test class tests the class Location from Model
 */
public class LocationTest {

    Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
    }

    @org.junit.jupiter.api.Test
    void getSetCity() {
        location.setCity("Oradea");
        assertEquals(location.getCity(),"Oradea");
        location.setCity("");
        assertEquals(location.getCity(),"");
    }

    @org.junit.jupiter.api.Test
    void getSetCountry() {
        location.setCountry("Romania");
        assertEquals(location.getCountry(),"Romania");
        location.setCountry("");
        assertEquals(location.getCountry(),"");
    }

    @org.junit.jupiter.api.Test
    void getSetTip() {
        location.setTip(Type.Mountain);
        assertEquals(location.getTip(),Type.Mountain);
        location.setTip(Type.Sea);
        assertEquals(location.getTip(),Type.Sea);
    }

    @Test
    void getSetRating() {
        location.setRating(9.5F);
        assertEquals(location.getRating(),9.5F);
        location.setRating(0F);
        assertEquals(location.getRating(),0F);
    }

    @Test
    void getSetUserId() {
        User user = new User();
        user.setFirstName("Luca");
        location.setUser(user);
        assertEquals(location.getUser(),user);
        User userr = new User();
        location.setUser(userr);
        assertEquals(location.getUser(),userr);
    }

}
