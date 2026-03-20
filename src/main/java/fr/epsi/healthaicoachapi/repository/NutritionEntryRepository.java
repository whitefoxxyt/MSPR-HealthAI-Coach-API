package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.NutritionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionEntryRepository extends JpaRepository<NutritionEntry, Long> {
    List<NutritionEntry> findByUserId(Long userId);
}

