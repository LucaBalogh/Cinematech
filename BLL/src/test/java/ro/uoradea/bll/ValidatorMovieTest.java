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
import ro.uoradea.model.Movie;
import ro.uoradea.model.enums.Type;

@SpringBootTest(classes = BLLTest.class)
@RunWith(SpringRunner.class)
public class ValidatorMovieTest {

    @Autowired
    Validator<Movie> validator;

    Movie movie;

    @Before
    public void initData(){
        movie = new Movie();
        movie.setTip(Type.Action);
    }

    @Test
    public void testValidator(){
        Throwable exception;

        movie.setName(null);
        movie.setRating(-10f);
        
        exception = Assert.assertThrows(InvalidCredentialsException.class,
                ()->{ validator.validate(movie);} );
        Assert.assertEquals(exception.getMessage(), "The name is not valid!The rating can't be lower than 0!");


        movie.setRating(10f);

        exception = Assert.assertThrows(InvalidCredentialsException.class,
                ()->{ validator.validate(movie);} );
        Assert.assertEquals(exception.getMessage(), "The name is not valid!");

        movie.setName("Test");
        movie.setRating(-10f);

        exception = Assert.assertThrows(InvalidCredentialsException.class,
                ()->{ validator.validate(movie);} );
        Assert.assertEquals(exception.getMessage(), "The rating can't be lower than 0!");
    }
}
