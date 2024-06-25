package ro.uoradea.controllers;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.uoradea.bll.MoviesBLL;
import ro.uoradea.model.Movie;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(DashboardController.BASE_URL)
@Slf4j
public class DashboardController {

    protected static final String BASE_URL = "/cinematech/dashboard";

    private final MoviesBLL moviesBLL;

    public DashboardController(MoviesBLL moviesBLL) {
        this.moviesBLL = moviesBLL;
    }

    /**
     * Retrieves all the movies.
     * @return ResponseEntity<List<Movie>> - all
     */
    @RequestMapping(value = "/get-all", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(moviesBLL.getAllMovies(), HttpStatus.OK);
    }

    /**
     * Retrieves top movies.
     * @return ResponseEntity<List<Movie>> - all
     */
    @RequestMapping(value = "/get-top/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getBestMovies(@PathVariable int userId) {
        log.info("Get the recommendations for the user with id " + userId);
        return new ResponseEntity<>(moviesBLL.getBestMovies(userId), HttpStatus.OK);
    }
}
