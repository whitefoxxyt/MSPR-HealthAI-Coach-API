package fr.epsi.healthaicoachapi.controller;

import fr.epsi.healthaicoachapi.entity.BiometricEntry;
import fr.epsi.healthaicoachapi.repository.BiometricEntryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biometrics")
@Tag(name = "Biometrics", description = "API de suivi biométrique")
@SecurityRequirement(name = "bearer-jwt")
public class BiometricController {

    private static final Logger log = LoggerFactory.getLogger(BiometricController.class);

    private final BiometricEntryRepository biometricEntryRepository;

    public BiometricController(BiometricEntryRepository biometricEntryRepository) {
        this.biometricEntryRepository = biometricEntryRepository;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Liste les données biométriques d'un utilisateur")
    public ResponseEntity<List<BiometricEntry>> getUserBiometrics(@PathVariable Long userId) {
        List<BiometricEntry> entries = biometricEntryRepository.findByUserId(userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère une entrée biométrique par ID")
    public ResponseEntity<BiometricEntry> getBiometricById(@PathVariable Long id) {
        return biometricEntryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crée une nouvelle entrée biométrique")
    public ResponseEntity<BiometricEntry> createBiometric(@RequestBody BiometricEntry entry) {
        try {
            BiometricEntry saved = biometricEntryRepository.save(entry);
            log.info("Biometric entry created for user: {}", entry.getUser().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Error creating biometric entry: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une entrée biométrique")
    public ResponseEntity<BiometricEntry> updateBiometric(@PathVariable Long id, @RequestBody BiometricEntry entryDetails) {
        return biometricEntryRepository.findById(id)
                .map(entry -> {
                    entry.setWeightKg(entryDetails.getWeightKg());
                    entry.setHeightCm(entryDetails.getHeightCm());
                    entry.setBmi(entryDetails.getBmi());
                    entry.setFatPercentage(entryDetails.getFatPercentage());
                    entry.setHeartRateRest(entryDetails.getHeartRateRest());
                    entry.setHeartRateAvg(entryDetails.getHeartRateAvg());
                    entry.setHeartRateMax(entryDetails.getHeartRateMax());
                    entry.setBloodPressure(entryDetails.getBloodPressure());
                    entry.setStatus(entryDetails.getStatus());
                    BiometricEntry updated = biometricEntryRepository.save(entry);
                    log.info("Biometric entry {} updated", id);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une entrée biométrique")
    public ResponseEntity<Void> deleteBiometric(@PathVariable Long id) {
        if (biometricEntryRepository.existsById(id)) {
            biometricEntryRepository.deleteById(id);
            log.info("Biometric entry {} deleted", id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

