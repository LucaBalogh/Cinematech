package ro.uoradea.controllers;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.uoradea.bll.MoviesBLL;
import ro.uoradea.bll.exceptions.InternalServerException;
import ro.uoradea.bll.exceptions.MovieCreationError;
import ro.uoradea.bll.exceptions.MovieNotFound;
import ro.uoradea.model.Movie;

@RestController
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(MovieController.BASE_URL)
@Slf4j
public class MovieController {

    protected static final String BASE_URL = "/cinematech/movies";

    private final MoviesBLL moviesBLL;

    public MovieController(MoviesBLL moviesBLL) {
        this.moviesBLL = moviesBLL;
    }

    /**
     * Retrieves all the movies associated to an user.
     * @param userId - int
     * @return ResponseEntity<List<Movie>> - all
     */
    @RequestMapping(value = "/get-all-by-user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getAllMoviesByUserId(@PathVariable int userId) {
        log.info("Get all movies for the user with id " + userId);
        return new ResponseEntity<>(moviesBLL.getAllMoviesForAUser(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/create")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) throws MovieCreationError {
        log.info("Creating a new movie with " + movie);
        return new ResponseEntity<>(moviesBLL.addMovie(movie), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update")
    public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie) throws MovieNotFound {
        log.info("Updating the selected movie with " + movie);
        return new ResponseEntity<>(moviesBLL.updateMovie(movie), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable int movieId){
        log.info("Deleting the movie with id " + movieId);
        moviesBLL.deleteMovie(movieId);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/search/{userId}/{movieName}")
    public ResponseEntity<Movie> getMovieByName(@PathVariable String movieName, @PathVariable int userId) {
        log.info("Searching for the movie with name " + movieName + " of the user with id " + userId);
        return new ResponseEntity<>(moviesBLL.searchMovie(movieName, userId), HttpStatus.OK);
    }
}
