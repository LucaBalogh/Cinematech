package ro.ubb.tt.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.ubb.tt.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    @Query("FROM User user WHERE user.email = :email")
    User findByEmail(String email);
}
