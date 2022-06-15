package ro.ubb.tt.dal;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ubb.tt.model.Location;

import java.util.List;

@SpringBootTest(classes = DALTest.class)
@RunWith(SpringRunner.class)
public class LocationRepositoryTest {

    @Autowired
    private LocationsRepository locationsRepository;

    @Test
    public void testFindAllByUserStoryId(){

        //there are 2 rows
        List<Location> result= locationsRepository.findAllByUserId(1);
        Assert.assertEquals(2, result.size());

        //invalid id
        result= locationsRepository.findAllByUserId(-2);
        Assert.assertEquals(0, result.size());
    }
}
