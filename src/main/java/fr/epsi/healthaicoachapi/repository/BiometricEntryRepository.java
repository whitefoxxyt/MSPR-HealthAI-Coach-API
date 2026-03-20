package fr.epsi.healthaicoachapi.repository;

import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiometricEntryRepository extends JpaRepository<BiometricEntry, Long> {
    List<BiometricEntry> findByUserId(Long userId);
}

