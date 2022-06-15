package ro.ubb.tt.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.tt.bll.LocationBLL;
import ro.ubb.tt.bll.exceptions.InternalServerException;
import ro.ubb.tt.model.Location;

import java.util.List;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(LocationController.BASE_URL)
public class LocationController {

    protected static final String BASE_URL = "traveltech/locations";

    private final LocationBLL locationBLL;

    public LocationController(LocationBLL locationBLL) {
        this.locationBLL = locationBLL;
    }

    /**
     * Retrieves all the locations associated to an user.
     * @param userId - int
     * @return ResponseEntity<List<Location>> - all
     */
    @RequestMapping(value = "/get-all-by-user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> getAllLocationsByUserId(@PathVariable int userId) {
        return new ResponseEntity<>(locationBLL.getAllLocationsForAUser(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/create")
    public ResponseEntity<Location> createLocation(@RequestBody Location loc) {
        return new ResponseEntity<>(locationBLL.addLocation(loc), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/{locId}")
    public ResponseEntity<Location> updateLocation(@PathVariable int locId, @RequestBody Location loc) throws InternalServerException {
        return new ResponseEntity<>(locationBLL.updateLocation(loc), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{locId}")
    public ResponseEntity<String> deleteLocation(@PathVariable int locId) {
        locationBLL.deleteLocation(locId);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

}
