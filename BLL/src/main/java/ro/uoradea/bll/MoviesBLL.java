package ro.uoradea.bll;

import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.uoradea.bll.AI.KMeans;
import ro.uoradea.bll.exceptions.MovieCreationError;
import ro.uoradea.bll.exceptions.MovieNotFound;
import ro.uoradea.dal.MoviesRepository;
import ro.uoradea.model.Movie;

@Component
@Slf4j
public class MoviesBLL {

    private MoviesRepository moviesRepository;

    @Autowired
    public void setMoviesRepository(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public List<Movie> getAllMoviesForAUser(int userId){
        List<Movie> movies = moviesRepository.findAllByUserId(userId);
        movies.sort(Comparator.comparingInt(Movie::getId).reversed());

        return movies;
    }

    public List<Movie> getAllMovies(){return  moviesRepository.findAll();}

    public List<Movie> getBestMovies(int user_id){
        return KMeans.getTop10(moviesRepository.findAll(), moviesRepository.findAllByUserId(user_id), user_id);
    }

    @Transactional
    public Movie addMovie(Movie movie) throws MovieCreationError {
        if (movieExists(movie))
            throw new MovieCreationError("The movie already exists!");

        Movie l = movie;
        l = moviesRepository.saveAndFlush(l);
        movie.setId(l.getId());

        return movie;
    }

    @Transactional
    public Movie updateMovie(Movie movie) throws MovieNotFound {
        Movie foundMovie = moviesRepository.findById(movie.getId()).orElseThrow(() -> new MovieNotFound(String.format("The movie with id %s was not found!", movie.getId())));
        foundMovie.setName(movie.getName());
        foundMovie.setTip(movie.getTip());
        foundMovie.setRating(movie.getRating());

        return moviesRepository.saveAndFlush(foundMovie);
    }

    @Transactional
    public void deleteMovie(int id){
        moviesRepository.deleteById(id);
    }

    @Transactional
    public Movie searchMovie(String movieName, int userId){
        return moviesRepository.findByNameAndUserId(movieName, userId);
    }

    private boolean movieExists(Movie movie) {
        return moviesRepository.findByNameAndTipAndUserId(
                movie.getName(), movie.getTip(), movie.getUser().getId()
        ).isPresent();
    }
}
