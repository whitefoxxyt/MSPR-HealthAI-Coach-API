package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BiometricEntryRepository extends JpaRepository<BiometricEntry, Long> {
    List<BiometricEntry> findByUserId(Long userId);

    long countByStatus(String status);

    long countByCreatedAtAfter(LocalDateTime since);

    @Query("SELECT MAX(b.createdAt) FROM BiometricEntry b")
    LocalDateTime findLatestCreatedAt();

    @Query("SELECT b FROM BiometricEntry b WHERE b.heartRateMax > :threshold OR b.bmi > :bmiMax OR (b.bmi IS NOT NULL AND b.bmi < :bmiMin)")
    List<BiometricEntry> findOutliers(int threshold, BigDecimal bmiMax, BigDecimal bmiMin);

    @Query("SELECT DISTINCT b.source FROM BiometricEntry b WHERE b.source IS NOT NULL ORDER BY b.source")
    List<String> findDistinctSources();

    @Query("""
        SELECT b FROM BiometricEntry b
        WHERE b.status IN :statuses
          AND b.source = COALESCE(:source, b.source)
          AND b.createdAt >= COALESCE(:dateFrom, b.createdAt)
          AND b.createdAt <= COALESCE(:dateTo, b.createdAt)
    """)
    List<BiometricEntry> findFiltered(@Param("statuses") List<String> statuses,
                                       @Param("source") String source,
                                       @Param("dateFrom") LocalDateTime dateFrom,
                                       @Param("dateTo") LocalDateTime dateTo);

    @Query("""
        SELECT b.id FROM BiometricEntry b
        WHERE b.status IN :statuses
          AND b.source = COALESCE(:source, b.source)
          AND b.createdAt >= COALESCE(:dateFrom, b.createdAt)
          AND b.createdAt <= COALESCE(:dateTo, b.createdAt)
    """)
    List<Long> findFilteredIds(@Param("statuses") List<String> statuses,
                                @Param("source") String source,
                                @Param("dateFrom") LocalDateTime dateFrom,
                                @Param("dateTo") LocalDateTime dateTo);
}
