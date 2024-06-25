package ro.uoradea.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.uoradea.model.Movie;
import ro.uoradea.model.User;
import ro.uoradea.model.enums.Type;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Integer> {
    @Query("FROM Movie movie WHERE movie.user.id = :userId")
    List<Movie> findAllByUserId(int userId);

    Movie findByNameAndUserId(String name, int userId);

    Optional<Movie> findByNameAndTipAndUserId(String name, Type tip, int userId);
}
