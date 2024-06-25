package ro.uoradea.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.uoradea.model.enums.Type;

/**
 * This test class tests the class Location from Model
 */
public class MovieTest {

    Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
    }

    @org.junit.jupiter.api.Test
    void getSetName() {
        movie.setName("Deadpool");
        assertEquals(movie.getName(),"Deadpool");
        movie.setName("");
        assertEquals(movie.getName(),"");
    }

    @org.junit.jupiter.api.Test
    void getSetTip() {
        movie.setTip(Type.Action);
        assertEquals(movie.getTip(),Type.Action);
        movie.setTip(Type.Drama);
        assertEquals(movie.getTip(),Type.Drama);
    }

    @Test
    void getSetRating() {
        movie.setRating(9.5F);
        assertEquals(movie.getRating(),9.5F);
        movie.setRating(0F);
        assertEquals(movie.getRating(),0F);
    }

    @Test
    void getSetUserId() {
        User user = new User();
        user.setFirstName("Luca");
        movie.setUser(user);
        assertEquals(movie.getUser(),user);
        User userr = new User();
        movie.setUser(userr);
        assertEquals(movie.getUser(),userr);
    }

}
