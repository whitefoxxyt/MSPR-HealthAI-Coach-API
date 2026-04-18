package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.NutritionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NutritionEntryRepository extends JpaRepository<NutritionEntry, Long> {
    List<NutritionEntry> findByUserId(Long userId);

    long countByStatus(String status);

    long countByCreatedAtAfter(LocalDateTime since);

    @Query("SELECT MAX(n.createdAt) FROM NutritionEntry n")
    LocalDateTime findLatestCreatedAt();

    @Query("SELECT n FROM NutritionEntry n WHERE n.calories > :maxCalories OR n.calories < 0")
    List<NutritionEntry> findCalorieOutliers(BigDecimal maxCalories);

    @Query("SELECT n.mealType, AVG(n.calories) FROM NutritionEntry n WHERE n.mealType IS NOT NULL GROUP BY n.mealType")
    List<Object[]> avgCaloriesByMealType();

    @Query(value = "SELECT DATE(created_at) AS day, COUNT(*) AS cnt FROM nutrition_entries WHERE created_at >= :since GROUP BY DATE(created_at) ORDER BY day", nativeQuery = true)
    List<Object[]> countByDaySince(@Param("since") LocalDateTime since);

    @Query("SELECT DISTINCT n.source FROM NutritionEntry n WHERE n.source IS NOT NULL ORDER BY n.source")
    List<String> findDistinctSources();

    @Query("""
        SELECT n FROM NutritionEntry n
        WHERE n.status IN :statuses
          AND n.source = COALESCE(:source, n.source)
          AND n.createdAt >= COALESCE(:dateFrom, n.createdAt)
          AND n.createdAt <= COALESCE(:dateTo, n.createdAt)
    """)
    List<NutritionEntry> findFiltered(@Param("statuses") List<String> statuses,
                                       @Param("source") String source,
                                       @Param("dateFrom") LocalDateTime dateFrom,
                                       @Param("dateTo") LocalDateTime dateTo);

    @Query("""
        SELECT n.id FROM NutritionEntry n
        WHERE n.status IN :statuses
          AND n.source = COALESCE(:source, n.source)
          AND n.createdAt >= COALESCE(:dateFrom, n.createdAt)
          AND n.createdAt <= COALESCE(:dateTo, n.createdAt)
    """)
    List<Long> findFilteredIds(@Param("statuses") List<String> statuses,
                                @Param("source") String source,
                                @Param("dateFrom") LocalDateTime dateFrom,
                                @Param("dateTo") LocalDateTime dateTo);
}
