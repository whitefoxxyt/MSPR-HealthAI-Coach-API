package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.DietRecommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DietRecommendationRepository extends JpaRepository<DietRecommendation, Long> {

    Page<DietRecommendation> findAll(Pageable pageable);

    long countByCreatedAtAfter(LocalDateTime since);

    @Query("SELECT MAX(d.createdAt) FROM DietRecommendation d")
    LocalDateTime findLatestCreatedAt();

    @Query("SELECT d.diseaseType, COUNT(d) FROM DietRecommendation d WHERE d.diseaseType IS NOT NULL GROUP BY d.diseaseType ORDER BY COUNT(d) DESC")
    List<Object[]> countByDiseaseType();

    @Query("SELECT d.physicalActivityLevel, COUNT(d) FROM DietRecommendation d WHERE d.physicalActivityLevel IS NOT NULL GROUP BY d.physicalActivityLevel ORDER BY COUNT(d) DESC")
    List<Object[]> countByPhysicalActivityLevel();

    @Query("SELECT d.severity, COUNT(d) FROM DietRecommendation d WHERE d.severity IS NOT NULL GROUP BY d.severity ORDER BY COUNT(d) DESC")
    List<Object[]> countBySeverity();

    @Query("SELECT d.dietRecommendation, COUNT(d) FROM DietRecommendation d WHERE d.dietRecommendation IS NOT NULL GROUP BY d.dietRecommendation ORDER BY COUNT(d) DESC")
    List<Object[]> countByDietRecommendation();

    @Query("SELECT AVG(d.adherenceToDietPlan) FROM DietRecommendation d WHERE d.adherenceToDietPlan IS NOT NULL")
    Double avgAdherence();

    @Query("SELECT AVG(d.nutrientImbalanceScore) FROM DietRecommendation d WHERE d.nutrientImbalanceScore IS NOT NULL")
    Double avgNutrientImbalance();
}
