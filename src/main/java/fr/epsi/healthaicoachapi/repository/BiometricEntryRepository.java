package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BiometricEntryRepository extends JpaRepository<BiometricEntry, Long> {

    long countByStatus(String status);

    long countByCreatedAtAfter(LocalDateTime since);

    @Query("SELECT MAX(b.createdAt) FROM BiometricEntry b")
    LocalDateTime findLatestCreatedAt();

    @Query("SELECT b FROM BiometricEntry b WHERE b.heartRateMax > :threshold OR b.bmi > :bmiMax OR (b.bmi IS NOT NULL AND b.bmi < :bmiMin)")
    List<BiometricEntry> findOutliers(int threshold, BigDecimal bmiMax, BigDecimal bmiMin);
}
