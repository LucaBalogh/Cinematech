package ro.ubb.tt.bll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ro.ubb.tt.bll.LocationBLL;
import ro.ubb.tt.bll.exceptions.InternalServerException;
import ro.ubb.tt.dal.LocationsRepository;
import ro.ubb.tt.model.Location;
import ro.ubb.tt.model.User;
import ro.ubb.tt.model.enums.Type;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = LocationBLL.class)
@ExtendWith(MockitoExtension.class)
class LocationBLLTest {
    List<Location> allLocations;

    @MockBean
    private LocationsRepository locationsRepository;

    @InjectMocks
    LocationBLL locationBLL;

    @BeforeEach
    void setUp() {

        // Mock some repository data in order not to have to connect to the real database
        Location location1 = new Location();
        location1.setId(1);

        Location location2 = new Location();
        location1.setId(2);

        Location location3 = new Location();
        location1.setId(3);

        allLocations = new ArrayList<>();
        allLocations.add(location1);
        allLocations.add(location2);
        allLocations.add(location3);

        // mock the behaviour for get all
        Mockito.when(locationsRepository.findAll()).thenReturn(allLocations);
    }

    @Test
    void getAllLocations(){
        assert (locationBLL.getAllLocations().size() == 3);
    }

    @Test
    void getAllLocationsForUser(){
        assert (locationBLL.getAllLocationsForAUser(1).size() == 0);
    }
}