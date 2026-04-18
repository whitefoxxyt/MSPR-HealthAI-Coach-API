package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.ExerciseEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExerciseEntryRepository extends JpaRepository<ExerciseEntry, Long> {

    long countByStatus(String status);

    long countByCreatedAtAfter(LocalDateTime since);

    @Query("SELECT MAX(e.createdAt) FROM ExerciseEntry e")
    LocalDateTime findLatestCreatedAt();

    @Query("SELECT e.workoutType, COUNT(e) FROM ExerciseEntry e WHERE e.workoutType IS NOT NULL GROUP BY e.workoutType ORDER BY COUNT(e) DESC")
    List<Object[]> countByWorkoutType();

    @Query("SELECT COUNT(e) FROM ExerciseEntry e WHERE e.heartRateAvg IS NOT NULL AND e.heartRateAvg < 100")
    long countLowIntensity();

    @Query("SELECT COUNT(e) FROM ExerciseEntry e WHERE e.heartRateAvg IS NOT NULL AND e.heartRateAvg BETWEEN 100 AND 149")
    long countModerateIntensity();

    @Query("SELECT COUNT(e) FROM ExerciseEntry e WHERE e.heartRateAvg IS NOT NULL AND e.heartRateAvg >= 150")
    long countHighIntensity();

    @Query(value = "SELECT DATE(created_at) AS day, COUNT(*) AS cnt FROM exercise_entries WHERE created_at >= :since GROUP BY DATE(created_at) ORDER BY day", nativeQuery = true)
    List<Object[]> countByDaySince(@Param("since") LocalDateTime since);
}
