package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.ExerciseEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseEntryRepository extends JpaRepository<ExerciseEntry, Long> {
    List<ExerciseEntry> findByUserId(Long userId);
}

