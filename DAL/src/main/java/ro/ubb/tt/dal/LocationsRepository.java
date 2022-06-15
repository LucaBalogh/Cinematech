package ro.ubb.tt.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.ubb.tt.model.Location;
import ro.ubb.tt.model.User;
import ro.ubb.tt.model.enums.Type;

import java.util.List;

@Repository
public interface LocationsRepository extends JpaRepository<Location, Integer> {

    @Query("FROM Location location WHERE location.user.id = :userId")
    List<Location> findAllByUserId(int userId);

    @Modifying
    @Query("update Location loc set loc.city = :city," +  "loc.country = :country," + " loc.tip = :tip," + "loc.rating = :rating," + "loc.user = :user where loc.id = :id")
    void update(@Param(value = "id") int id, @Param(value = "city") String city, @Param(value = "country") String country, @Param(value = "tip") Type tip, @Param(value = "rating") Float rating, @Param(value = "user") User user);
}
