package ro.ubb.tt.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.tt.bll.LocationBLL;
import ro.ubb.tt.model.Location;

import java.util.List;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(DashboardController.BASE_URL)
public class DashboardController {

    protected static final String BASE_URL = "traveltech/dashboard";

    private final LocationBLL locationBLL;

    public DashboardController(LocationBLL locationBLL) {
        this.locationBLL = locationBLL;
    }

    /**
     * Retrieves all the locations.
     * @return ResponseEntity<List<Location>> - all
     */
    @RequestMapping(value = "/get-all", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> getAllLocations() {
        return new ResponseEntity<>(locationBLL.getAllLocations(), HttpStatus.OK);
    }

    /**
     * Retrieves top locations.
     * @return ResponseEntity<List<Location>> - all
     */
    @RequestMapping(value = "/get-top/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> getBestLocations(@PathVariable int userId) {
        return new ResponseEntity<>(locationBLL.getBestLocations(userId), HttpStatus.OK);
    }
}
