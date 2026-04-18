package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    long countByIsPremiumTrue();

    long countByLastActivityAfter(LocalDateTime since);

    @Query("SELECT u.objective, COUNT(u) FROM User u WHERE u.objective IS NOT NULL GROUP BY u.objective")
    List<Object[]> countByObjective();

    @Query("SELECT " +
           "SUM(CASE WHEN u.age BETWEEN 18 AND 25 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN u.age BETWEEN 26 AND 35 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN u.age BETWEEN 36 AND 50 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN u.age > 50 THEN 1 ELSE 0 END) " +
           "FROM User u WHERE u.age IS NOT NULL")
    Object[] ageDistribution();
}
