package ro.uoradea.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ro.uoradea.bll.exceptions.MovieCreationError;
import ro.uoradea.bll.exceptions.MovieNotFound;
import ro.uoradea.dal.MoviesRepository;
import ro.uoradea.model.Movie;
import ro.uoradea.model.User;
import ro.uoradea.model.enums.Type;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MoviesBLL.class)
@ExtendWith(MockitoExtension.class)
class MoviesBLLTest {
    List<Movie> allMovies;
    Movie movie;
    @MockBean
    private MoviesRepository moviesRepository;
    @InjectMocks
    MoviesBLL moviesBLL;

    @BeforeEach
    void setUp() {
        // Mock some repository data in order not to have to connect to the real database
        User user = new User("A", "B", "a@gmail.com", "AB");
        user.setId(1);

        movie = new Movie("Deadpool", Type.Action, 10f, user);
        movie.setId(1);

        Movie movie2 = new Movie("A", Type.Drama, 9f, new User());
        movie2.setId(2);

        Movie movie3 = new Movie("B", Type.Adventure, 10f, new User());
        movie2.setId(3);

        allMovies = new ArrayList<>();
        allMovies.add(movie2);
        allMovies.add(movie3);
    }

    @Test
    void getAllMovies(){
        when(moviesRepository.findAll()).thenReturn(allMovies);

        Assertions.assertEquals(moviesBLL.getAllMovies().size(), 2);
    }

    @Test
    void getAllMoviesForUser_Empty(){
        Assertions.assertEquals(moviesBLL.getAllMoviesForAUser(2).size(), 0);
    }

    @Test
    void getAllMoviesForUser(){
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);

        when(moviesRepository.findAllByUserId(1)).thenReturn(movieList);

        Assertions.assertEquals(moviesBLL.getAllMoviesForAUser(1).size(),1);
    }

    @Test
    void getBestMovies(){
        when(moviesRepository.findAll()).thenReturn(allMovies);

        Assertions.assertEquals(moviesBLL.getBestMovies(1).size(),2);
    }

    @Test
    void addMovie_Success() throws MovieCreationError {
        when(moviesRepository.saveAndFlush(movie)).thenReturn(movie);

        Movie result = moviesBLL.addMovie(movie);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(movie.getName(), result.getName());
        Assertions.assertEquals(movie.getTip(), result.getTip());
        Assertions.assertEquals(movie.getRating(), result.getRating());
        Assertions.assertEquals(movie.getUser(), result.getUser());

        verify(moviesRepository).saveAndFlush(movie);
    }

    @Test
    void updateMovie_Success() throws MovieNotFound {
        movie.setName("New name");

        when(moviesRepository.findById(movie.getId())).thenReturn(Optional.ofNullable(movie));
        when(moviesRepository.saveAndFlush(movie)).thenReturn(movie);

        Movie result = moviesBLL.updateMovie(movie);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("New name", result.getName());

        verify(moviesRepository).saveAndFlush(movie);
    }

    @Test
    void updateMovie_NotFound(){
        when(moviesRepository.findById(movie.getId())).thenReturn(Optional.empty());

        assertThrows(MovieNotFound.class, () -> moviesBLL.updateMovie(movie));

        verify(moviesRepository, never()).saveAndFlush(any(Movie.class));
    }

    @Test
    void deleteMovie_Success(){
       when(moviesRepository.findById(movie.getId())).thenReturn(Optional.ofNullable(movie));

        moviesBLL.deleteMovie(movie.getId());

        verify(moviesRepository).deleteById(movie.getId());
    }

    @Test
    void searchMovie_Success(){
        when(moviesRepository.findByNameAndUserId(movie.getName(), movie.getUser().getId())).thenReturn(movie);

        Movie result = moviesBLL.searchMovie(movie.getName(), movie.getUser().getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(movie.getName(), result.getName());
        Assertions.assertEquals(movie.getTip(), result.getTip());
        Assertions.assertEquals(movie.getRating(), result.getRating());
        Assertions.assertEquals(movie.getUser(), result.getUser());
    }
}