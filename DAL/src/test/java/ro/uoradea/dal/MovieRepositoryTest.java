package ro.uoradea.dal;


import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.uoradea.model.Movie;

@SpringBootTest(classes = DALTest.class)
@RunWith(SpringRunner.class)
public class MovieRepositoryTest {

    @Autowired
    private MoviesRepository moviesRepository;

    @Test
    public void testFindAllByUserStoryId(){

        //there are 2 rows
        List<Movie> result= moviesRepository.findAllByUserId(1);
        Assert.assertEquals(11, result.size());

        //invalid id
        result= moviesRepository.findAllByUserId(-2);
        Assert.assertEquals(0, result.size());
    }
}
