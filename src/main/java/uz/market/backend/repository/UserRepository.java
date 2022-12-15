package uz.market.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.market.backend.domain.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
 @Query("select  u from User u where  u.username =: username")
    User findByLogin(@Param("username") String username);
 boolean existsByUsername(String username);
}
